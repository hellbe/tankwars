package network;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TWGame extends StateBasedGame {
	
	public static final int MAINMENUSTATE = 0;
	public static final int GAMESTATE = 1;
	public static final int ENDSTATE = 4;
	
	public static String GAMELOG = "";
	
	public static boolean HOST = false;

	public TWGame() throws SlickException {
		super("TankWars");
	}

	public static void main(String[] args) throws SlickException {
		//Log.set( Log.LEVEL_DEBUG );
		System.setProperty("java.net.preferIPv4Stack" , "true"); // Fixes host discovery problems on OSX
		AppGameContainer app = new AppGameContainer( new TWGame() );
		app.setDisplayMode(800, 600, false);
		app.setAlwaysRender(true);
		app.start();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new TWMenuState(MAINMENUSTATE));
		this.addState(new TWGameClient(GAMESTATE));
		this.addState(new TWEndState(ENDSTATE));
	}

}