package network;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.minlog.Log;

public class TWGame extends StateBasedGame {

	public TWGame() throws SlickException {
		super("TankWars");
	}

	public static void main(String[] args) throws SlickException {
		//Log.set( Log.LEVEL_DEBUG );
		AppGameContainer app = new AppGameContainer( new TWGame() );
		app.setDisplayMode(800, 600, false);
		app.setAlwaysRender(true);
		app.start();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new TWGameClient(true));
	}

}