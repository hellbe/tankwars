package network;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

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
		mapInfo = new TWMap("data/TankWars.tmx","data");
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
		// Make movements
		for( TWGameEntity entity : entities ){
			if ( ! isBlocked( entity.getFutureMove( delta ) ) ){
				entity.move(delta);
			}
		}
		// Update status on all entities
		for ( TWGameEntity entity : entities ){
			entity.update();
		}
		// Update the clients
		networkServer.updateClients( entities );
	}

	public void endGame() {
		networkServer.stop();
	}

}
