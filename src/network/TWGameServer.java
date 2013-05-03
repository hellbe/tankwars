package network;
import java.io.IOException;
import java.util.ArrayList;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;
import network.TWNetwork.TWPlayerStatus;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;

import entity.BlockEntity;
import entity.TankEntity;

public class TWGameServer implements Runnable {
	public TiledMap map;
	public TWMap mapInfo;
	public boolean[][] blocked;
	TWNetworkServer server;
	TWServerUpdater updater;
	Thread thread;
	TWEntityContainer entities = new TWEntityContainer();
	ArrayList<TWPlayerStatus> newPlayers = new ArrayList<TWPlayerStatus>();
	ArrayList<TWPlayer> players = new ArrayList<TWPlayer>();
	
	public TWGameServer() throws SlickException {
		// Start network server
		server = new TWNetworkServer( this );
		
		// Load map data
		mapInfo = new TWMap("data/TankWars.tmx","data");
		map = new TiledMap( mapInfo.path , mapInfo.folder );
		loadBlocked();	
		
		// Start the server updater thread
		updater = new TWServerUpdater( this );
		thread = new Thread( updater );
		thread.start();
		
	}

	@Override
	public void run() { }
	
	public void updatePlayerStatus(TWPlayerStatus playerStatus ) {
		System.out.println( "Updating player:"+playerStatus.id);
		players.get( playerStatus.id - 1 ).playerStatus = playerStatus;
	}

	public void loadBlocked() throws SlickException{
		blocked = new boolean[map.getWidth()][map.getHeight()];
		for ( int xAxis=0; xAxis < map.getWidth(); xAxis ++ ) {
			for ( int yAxis=0; yAxis < map.getHeight(); yAxis ++ ) {
				int tileID = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					blocked[xAxis][yAxis] = true;
				}
			}
		}
	}

	public boolean isBlocked(float x, float y) {
		if( x > map.getWidth() * map.getTileWidth() || y > map.getHeight() * map.getTileHeight() || x < 0 || y < 0 ){
			return true;
		}
		int xBlock = (int) x / map.getTileWidth();
		int yBlock = (int) y / map.getTileHeight();
		return blocked[xBlock][yBlock];
	}
	
	public void update(float delta) {
		// Make movements
		for( TWGameEntity entity : entities ){
			entity.move(delta);
		}
		// Update status on all entities
		for ( TWGameEntity entity : entities ){
			entity.update();
		}
		// Update the clients
		server.updateClients( entities );
	}

	public void addPlayer(TWPlayerStatus playerStatus){
		players.add( new TWPlayer( playerStatus) );
		entities.add( players.get( players.size() - 1 ) );
	}


}
