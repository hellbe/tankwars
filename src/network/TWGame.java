package network;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import client.EndState;
import client.MenuState;

import com.esotericsoftware.minlog.Log;

public class TWGame extends StateBasedGame {
	public static final int MAINMENUSTATE = 0;
<<<<<<< HEAD
	public static final int ENDSTATE = 4;
=======
	public static final int GAMESTATE = 1;
	public static final int ENDSTATE = 3;
	
	public static boolean HOST = false;
>>>>>>> Meny till gamestate fungerar

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
		this.addState(new TWMenuState(MAINMENUSTATE));
<<<<<<< HEAD
		this.addState(new TWGameClient(true, 1));
		this.addState(new TWGameClient(false, 2));
=======
		this.addState(new TWGameClient(GAMESTATE));
>>>>>>> Meny till gamestate fungerar
		this.addState(new TWEndState(ENDSTATE));
	}

}