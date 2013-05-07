package network;

import java.util.ArrayList;
import java.util.Random;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMessageContainer;
import network.TWNetwork.TWMap;
import network.TWNetwork.TWPlayerStatus;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

public class TWGameServer {
	TiledMap map;
	TWMap mapInfo;
	boolean[][] mapBlockData;
	TWNetworkServer networkServer;
	TWServerUpdater updater;
	Thread thread;
	TWEntityContainer entities = new TWEntityContainer();
	TWMessageContainer messages = new TWMessageContainer();
	long lastMessageUpdate = 0;
	
	public TWGameServer( String mapName ) throws SlickException {
		// Start network server
		networkServer = new TWNetworkServer( this );
		
		// Load map data
		mapInfo = new TWMap(mapName,"data");
		map = new TiledMap( mapInfo.path , mapInfo.folder );
		loadMapBlockData();	
		
		// Start the server updater thread
		updater = new TWServerUpdater( this );
		thread = new Thread( updater );
		thread.start();
		
	}

	public void updatePlayerStatus( Integer id, TWPlayerStatus playerStatus ) {
		entities.getPlayer( id ).playerStatus = playerStatus;
	}

	public void loadMapBlockData() throws SlickException{
		mapBlockData = new boolean[ map.getWidth() ][ map.getHeight() ];
		for ( int xAxis=0; xAxis < map.getWidth(); xAxis ++ ) {
			for ( int yAxis=0; yAxis < map.getHeight(); yAxis ++ ) {
				int tileID = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					mapBlockData[xAxis][yAxis] = true;
				}
			}
		}
	}

	public boolean isBlocked( Vector2f position ) {
		if( position.x > map.getWidth() * map.getTileWidth() 
				|| position.y > map.getHeight() * map.getTileHeight() 
				|| position.x < 0 
				|| position.y < 0 ){
			return true;
		}
		int xBlock = (int) ( position.x / map.getTileWidth() );
		int yBlock = (int) ( position.y / map.getTileHeight() );
		return mapBlockData[xBlock][yBlock];
	}
	
	public void updateEntities(float delta) {
		ArrayList<TWPlayer> players = entities.getPlayers();
		ArrayList<TWBullet> bullets = entities.getBullets();
		
		// Tank movement
		for( TWPlayer player : players ){
			if ( ! isBlocked( player.getFutureMove( delta ))){
				player.move(delta);
			} 
		}
		
		// Bullet movement
		for ( TWBullet bullet : bullets ){
			if ( ! isBlocked( bullet.getFutureMove( delta ))){
				bullet.move(delta);
			}
			else {
				entities.remove(bullet);
			}
		}
		
		// Shoot!
		for ( TWPlayer player : entities.getPlayers() ){
			if ( player.playerStatus.shoot && System.currentTimeMillis() - player.lastShot > 300 ){
				entities.add( new TWBullet( player.id, player.position.copy().add( player.direction.copy().scale( 50)), player.direction.copy() ) );
				player.lastShot = System.currentTimeMillis();
			}
		}
		
		// Hits
		for ( TWPlayer player : players ){
			for ( TWGameEntity entity : entities ){
				if ( entity instanceof TWBullet && player.collides(entity) ){
					
					player.hp = player.hp - 20;
					entities.remove(entity);
					
					if ( player.hp == 0 ){
						player.position = getRandomNotBlockedPosition();
						player.direction = new Vector2f( getRandomAngle() );
						player.hp = 100;
						entities.getPlayer( ((TWBullet) entity ).playerId ).score ++;
					}
				}
			}
		}
		
		// Update the client entities
		networkServer.updateClients( entities );
		
		// Update the client messages
		if ( System.currentTimeMillis() - lastMessageUpdate > 1000 ){
			networkServer.updateClients( messages );
		}
		
	}

	private boolean playersCollide( TWPlayer player , ArrayList<TWPlayer> players) {
		for ( TWPlayer other : players ){
			if( player.id != other.id && player.collides( other) ){
				return true;
			}
		}
		return false;
	}

	public void endGame() {
		networkServer.stop();
	}
	
	public Vector2f getRandomNotBlockedPosition(){
		Random random = new Random();
		Vector2f position = new Vector2f();
		do {
			position.set( random.nextInt( map.getWidth() * map.getTileWidth() ), random.nextInt( map.getHeight() * map.getTileHeight() ) );
		} while ( isBlocked( position ));
		return position;
	}
	
	public int getRandomAngle(){
		Random random = new Random();
		return random.nextInt(359);
	}
	
	public void addPlayer(int id) {
		TWPlayer player = new TWPlayer( id );
		player.position = getRandomNotBlockedPosition();
		player.direction = new Vector2f( getRandomAngle() );
		entities.add(player);
	}

}
