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

public class TWGameServer {
	public TiledMap map;
	public TWMap mapInfo;
	public boolean[][] blocked;
	TWNetworkServer server;
	TWServerUpdater updater;
	Thread thread;
	
	ArrayList<TWPlayerStatus> players = new ArrayList<TWPlayerStatus>();
	TWEntityContainer entities = new TWEntityContainer();
	
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
	

	public void updatePlayerStatus(TWPlayerStatus player) {
		System.out.println("Server: got player "+player.id+" who has turn:"+player.turn+", move: "+player.move+" and shoot: "+player.shoot);
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
		// TODO Auto-generated method stub
		
	}


	public void addPlayer(TWPlayerStatus playerStatus) {
		players.add( playerStatus );
	}


	public void render() {
		server.updateClients( entities );
		
	}
	
	
}
