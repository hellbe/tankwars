package client;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import component.Collidable;
import component.ImageRenderComponent;
import entity.BlockEntity;
import entity.GameEntity;
import entity.TankEntity;
import gameplay.World;
import org.newdawn.slick.tiled.TiledMap;

public class PlayState extends BasicGameState {
 
    int stateID;
    World world;
    Client client;
    Kryo kryo;

    //list of map entities
    ArrayList<GameEntity> entities;
    
    //TiledMap attributes
    TiledMap map;
	Image player;
	float x = 35f, y = 35f;
	int tileSize, xOffset = 0, yOffset = 0;
	static boolean[][] blocked;
    
    public PlayState( int stateID ) {
       this.stateID = stateID;
    }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	//Connect to the server
    	connect();
     
		
		//init GameEntities
		entities = new ArrayList<GameEntity>();
    	
    	
    	//TiledMap background
        map = new TiledMap("config/TankWars.tmx","config/");
		tileSize = map.getTileHeight();

		blocked = new boolean[map.getWidth()][map.getHeight()];

		for ( int xAxis=0; xAxis < map.getWidth(); xAxis ++ ) {
			for ( int yAxis=0; yAxis < map.getHeight(); yAxis ++ ) {
				int tileID = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					blocked[xAxis][yAxis] = true;
					
					//add a block entity for each blocked tile
					entities.add(new BlockEntity("tileBlock", new Vector2f(xAxis*map.getTileWidth(),yAxis*map.getTileHeight()), new Vector2f(map.getTileWidth(),map.getTileHeight())));
				
					
				}
			}
		}
		
		
        //init player
        TankEntity player1 = new TankEntity("player1");
        player1.setPosition(new Vector2f(100, 100));
        entities.add(player1);
        
        //init collidable object
        GameEntity collisionObject = new GameEntity("object");
        collisionObject.AddComponent( new ImageRenderComponent("objectrender", new Image("/data/battletank.png")));
        collisionObject.AddComponent(new Collidable("collidable", collisionObject, new Vector2f(128,128)));
        collisionObject.setPosition(new Vector2f(500,300));
        entities.add(collisionObject);

    }
 
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    	
    	//TiledMap
    	if ( x > 240 ){
    		xOffset = 240 - (int) x;
    	}
    	if ( y > 240 ){
    		yOffset = 240 - (int) y; 
    	}
    	map.render( xOffset, yOffset );
    
    	//Render everything in the world
    	for (GameEntity e : entities) {
			e.render(gc, sbg, g);
		}
    	
    	
    	
    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException     {

    	//send updates to the server
    	//update returned values
    	for (GameEntity e : entities) {
			e.update(gc, sbg, delta);
		}
     	
    }

	@Override
	public int getID() {
		return stateID;
	}
	
	private void connect(){
		client = new Client();
		kryo = client.getKryo();
		kryo.register( World.class );
		client.start();
		try {
			client.connect(5000, "127.0.0.1", 55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
 
	public boolean isBlocked(float x, float y) {
		if( x > map.getWidth() * map.getTileWidth() || y > map.getHeight() * map.getTileHeight() || x < 0 || y < 0 ){
			return true;
		}
		int xBlock = (int) x / tileSize;
		int yBlock = (int) y / tileSize;
		return blocked[xBlock][yBlock];
	}
	
}