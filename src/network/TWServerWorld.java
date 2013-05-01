package network;

import network.TWNetwork.PlayerStatus;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class TWServerWorld {

	public TiledMap map;
	public boolean[][] blocked;

	public TWServerWorld() throws SlickException{
		//map = new TiledMap("data/TankWars.tmx","data");
		//loadBlocked();
	}

	public void updatePlayerStatus(PlayerStatus player) {
		System.out.println("Server: got player "+player.id+" who has turn:"+player.turn+", move: "+player.move+" and shoot: "+player.shoot);
	}

	public void update(float delta) {
		// TODO Auto-generated method stub

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

}
