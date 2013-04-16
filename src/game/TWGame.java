package game;

import network.TWClient;
import network.TWServer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class TWGame extends BasicGame{

	TWWorld world;
	Image player;
	float x = 35f, y = 35f;
	int tileSize, xOffset = 0, yOffset = 0;
	
	TWServer server;
	TWClient client;

	public TWGame() {
		super("TankWars");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		//Start the network
		server = new TWServer();
		client = new TWClient();
		world = new TWWorld(client);
		player = new Image("data/tank.png");
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		
		//Update the world from server
		world.update();
		
		//Send input to server
		
		Input input = gc.getInput();

		if (input.isKeyDown(Input.KEY_UP)) {
			player.setRotation(180);
			if ( ! world.isBlocked(x, y - delta * 0.1f) ) {
				y -= delta * 0.1f;
			}
		}
		else if (input.isKeyDown(Input.KEY_DOWN)) {
			player.setRotation(0);
			if ( ! world.isBlocked(x, y + delta * 0.1f)) {
				y += delta * 0.1f;
			}
		}
		else if (input.isKeyDown(Input.KEY_LEFT)) {
			player.setRotation(90);
			if ( ! world.isBlocked(x - delta * 0.1f, y)) {
				x -= delta * 0.1f;
			}
		}
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
			player.setRotation(-90);
			if ( ! world.isBlocked (x + delta * 0.1f, y) ) {
				x += delta * 0.1f;
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		if ( x > 240 ){
			xOffset = 240 - (int) x;
		}
		if ( y > 240 ){
			yOffset = 240 - (int) y; 
		}
		world.map.render( xOffset, yOffset );
		player.draw( (int) x + xOffset, (int) y + yOffset );
		
	}

	public static void main(String[] args) throws SlickException {
		
		AppGameContainer app = new AppGameContainer( new TWGame() );
		app.setDisplayMode(480, 480, false);
		app.start();
		
	}
	
	
}