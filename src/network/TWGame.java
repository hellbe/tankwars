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

import com.esotericsoftware.minlog.Log;

public class TWGame extends BasicGame{

	float x = 35f, y = 35f;
	int tileSize, xOffset = 0, yOffset = 0;

	TWGameServer server;
	TWGameClient client;

	public TWGame() {
		super("TankWars");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		server = new TWGameServer();
		client = new TWGameClient();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		client.update();
		client.sendPlayerStatus();
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		client.render();
	}

	public static void main(String[] args) {
		Log.set( Log.LEVEL_DEBUG );
		try {
			AppGameContainer app = new AppGameContainer( new TWGame() );
			app.setDisplayMode(480, 480, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void keyPressed( int key, char c ){
		client.changePlayerStatus( key, true );
	}

	public void keyReleased(int key, char c){
		client.changePlayerStatus( key, false );
	}

}