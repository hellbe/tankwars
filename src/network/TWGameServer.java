package network;

import java.util.ArrayList;
import java.util.Random;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;
import network.TWNetwork.TWPlayerStatus;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

public class TWGameServer {
	public TiledMap map;
	public TWMap mapInfo;
	public boolean[][] mapBlockData;
	TWNetworkServer networkServer;
	TWServerUpdater updater;
	Thread thread;
	TWEntityContainer entities = new TWEntityContainer();
	
	public TWGameServer() throws SlickException {
		// Start network server
		networkServer = new TWNetworkServer( this );
		
		// Load map data
		mapInfo = new TWMap("data/TankWarsMap2.tmx","data");
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
						
						Random random = new Random();
						player.position.set(random.nextInt(map.getWidth()),random.nextInt(map.getHeight()));
						while (isBlocked(player.position)) {
							player.position.set(random.nextInt(map.getWidth()),random.nextInt(map.getHeight())); //ska vara random på kartan som inte är blockad
						}

						player.direction.set(0,1);
						player.hp = 100;
						entities.getPlayer( ((TWBullet) entity ).playerId ).score ++;
					}
				}
			}
		}
		
		// Update the clients
		networkServer.updateClients( entities );
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

}
