package client;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
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

<<<<<<< HEAD
	//TiledMap
	TiledMap map;
=======
>>>>>>> Lagt in musik och fixat till lite filar
	//list of map entities
	ArrayList<GameEntity> entities;

	public PlayState( int stateID ) {
		this.stateID = stateID;
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		//Connect to the server
		//connect();
		
		//init controls in order [player][up, left, down, right, shoot]
		// ( w , a , s , d , 1)
		// ( 'up', 'left', 'down', 'right', 'space')
		Integer[][] playerKeybinds = {
			{Input.KEY_W,Input.KEY_A,Input.KEY_S,Input.KEY_D, Input.KEY_1},
			{Input.KEY_UP,Input.KEY_LEFT,Input.KEY_DOWN,Input.KEY_RIGHT,Input.KEY_SPACE}
		};


		//init GameEntities
		entities = new ArrayList<GameEntity>();

		//TiledMap background
		map = new TiledMap("data/TankWars.tmx","data/");
		for ( int xAxis=0; xAxis < map.getWidth(); xAxis ++ ) {
			for ( int yAxis=0; yAxis < map.getHeight(); yAxis ++ ) {
				int tileID = map.getTileId(xAxis, yAxis, 0);
				String value = map.getTileProperty(tileID, "blocked", "false");
				if ("true".equals(value)){
					//add a block entity for each blocked tile
					entities.add(new BlockEntity("tileBlock", new Vector2f(xAxis*map.getTileWidth(),yAxis*map.getTileHeight()), new Vector2f(map.getTileWidth(),map.getTileHeight())));
				}
			}
		}


		//init player1
		TankEntity player1 = new TankEntity("player1", playerKeybinds[0]);
		player1.setPosition(new Vector2f(100, 100));
		entities.add(player1);

		//init player2
		TankEntity player2 = new TankEntity("player2", playerKeybinds[1]);
		player2.setPosition(new Vector2f(500, 300));
		entities.add(player2);

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		
		map.render( 0, 0 );

		//Render everything in the world
		for (GameEntity e : entities) {
			e.render(gc, sbg, g);


			//			//mapdebug
			//			if (e.getSize() != null) {
			//			g.drawRect(e.getPosition().x,e.getPosition().y,e.getSize().x,e.getSize().y);
			//			}

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

	//	private void connect(){
	//		client = new Client();
	//		kryo = client.getKryo();
	//		kryo.register( World.class );
	//		client.start();
	//		try {
	//			client.connect(5000, "127.0.0.1", 55555, 55556);
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//
	//	}

}