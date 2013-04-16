package game;

import java.util.ArrayList;

import network.TWClient;
import network.TWNetwork.WorldData;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import entity.GameEntity;

public class TWWorld {
	
	public TWEntities entities;
	public TiledMap map;
	public boolean[][] blocked;
	public TWClient client;
	
	public TWWorld(){
		map = new TiledMap("data/TankWars.tmx","data");
		loadBlocked();
	}
	
	public TWWorld( TWClient client ){
		this.client = client;
		map = client.getMap();
		loadBlocked();
	}
	
	public void update(){
		entities = client.getEntities();
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

