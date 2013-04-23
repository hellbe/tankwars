package network;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class TWGame extends BasicGame implements InputListener {

	TWClientWorld world;
	float x = 35f, y = 35f;
	int tileSize, xOffset = 0, yOffset = 0;
	
	TWServer server;
	TWClient client;

	public TWGame() {
		super("TankWars");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		server = new TWServer();
		client = new TWClient();
		world = new TWClientWorld( client );
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		world.update();
		world.sendPlayerStatus();
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		world.render();
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer( new TWGame() );
		app.setDisplayMode(480, 480, false);
		app.start();
	}
	
	public void keyPressed( int key, char c ){
		world.changePlayerStatus( key, true );
	}
	
	public void keyReleased(int key, char c){
		world.changePlayerStatus( key, false );
	}
	
}