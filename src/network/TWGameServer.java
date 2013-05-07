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

/**
 * Game server class. Handles almost every game calculations based on the NetworkServer input.
 * @author Ludwig, Peter, Simon
 *
 */
public class TWGameServer {
	
	/** The active map */
	TiledMap map;
	
	/** information about the current map */
	TWMap mapInfo;
	
	/** Contains information about what map tiles are blocked or not */
	boolean[][] mapBlockData;
	
	/** The network server */
	TWNetworkServer networkServer;
	
	/** the server updater */
	TWServerUpdater updater;
	
	/** container for every entity on the map */
	TWEntityContainer entities = new TWEntityContainer();
	
	/** container for chat messages */
	TWMessageContainer messages = new TWMessageContainer();
	
	/** constaints the message sending so we dont update the chat more than nessesary */
	long lastMessageUpdate = 0;
	
	public TWGameServer(String mapName) throws SlickException {
		networkServer = new TWNetworkServer( this );
		mapInfo = new TWMap( mapName, "data" );
		map = new TiledMap( mapInfo.path , mapInfo.folder );
		loadMapBlockData();	
		updater = new TWServerUpdater( this );
		new Thread( updater ).start();
	}

	/**
	 * update the status for the specific play
	 * @param id the player to update
	 * @param playerStatus the status to set on the player
	 */
	public void updatePlayerStatus( Integer id, TWPlayerStatus playerStatus ) {
		entities.getPlayer( id ).playerStatus = playerStatus;
	}

	/**
	 * Load map properties. Sets each tile blocked or not
	 * @throws SlickException
	 */
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

	/**
	 * A method to check if the position is currently blocked on the map
	 * @param position the position to check
	 * @return true if position is blocked
	 */
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
	/**
	 * updates every entity on the map based on speed, shots and hits; then sends the updated list to NetworkServer
	 * @param delta
	 */
	public void updateEntities(float delta) {
		ArrayList<TWPlayer> players = entities.getPlayers();
		ArrayList<TWBullet> bullets = entities.getBullets();
		/**
		 * Process tank movements
		 */
		for( TWPlayer player : players ){
			if ( ! isBlocked( player.getFutureMove( delta ))){
				player.move(delta);
			} 
		}
		/**
		 * Process bullet movements and removal if they hit walls 
		 */
		for ( TWBullet bullet : bullets ){
			if ( ! isBlocked( bullet.getFutureMove( delta ))){
				bullet.move(delta);
			}
			else {
				entities.remove(bullet);
			}
		}
		/**
		 * Process bullet creation
		 */
		for ( TWPlayer player : entities.getPlayers() ){
			if ( player.playerStatus.shoot && System.currentTimeMillis() - player.lastShot > 300 ){
				entities.add(new TWBullet(player.id, player.position.copy().add( player.direction.copy().scale(50)),
						player.direction.copy() ) );
				player.lastShot = System.currentTimeMillis();
			}
		}
		/**
		 * Process player getting hit
		 */
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
		/**
		 * Update the clients' entities
		 */
		networkServer.updateClients( entities );
		/**
		 * Update the clients' message list
		 */
		if ( System.currentTimeMillis() - lastMessageUpdate > 1000 ){
			networkServer.updateClients( messages );
		}
	}

	/**
	 * end the game and server.
	 */
	public void endGame() {
		networkServer.stop();
	}
	
	/**
	 * method used for getting a random non-blocked position
	 * @return random valid position
	 */
	public Vector2f getRandomNotBlockedPosition(){
		Random random = new Random();
		Vector2f position = new Vector2f();
		do {
			position.set( random.nextInt( map.getWidth() * map.getTileWidth() ), 
					random.nextInt( map.getHeight() * map.getTileHeight() ) );
		} while ( isBlocked( position ));
		return position;
	}
	
	/**
	 * returns a random angle in degrees (rotation)
	 * @return angle
	 */
	public int getRandomAngle(){
		return new Random().nextInt(359);
	}
	
	/**
	 * add a player to the game
	 * @param id unique id of the player to add
	 */
	public void addPlayer(int id) {
		TWPlayer player = new TWPlayer( id );
		player.position = getRandomNotBlockedPosition();
		player.direction = new Vector2f( getRandomAngle() );
		entities.add(player);
	}

}
