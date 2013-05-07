package network;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TWGame extends StateBasedGame {
	
	public static final int MAINMENUSTATE = 0;
	public static final int MAPMENUSTATE = 1;
	public static final int GAMESTATE = 2;
	public static final int ENDSTATE = 4;
	
	private static String GAMELOG = "Game log:";
	private static int LOGROWS=0;
	
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
		this.addState(new TWMapMenuState(MAPMENUSTATE));
		this.addState(new TWGameClient(GAMESTATE));
		this.addState(new TWEndState(ENDSTATE));
	}
	
	public static void addtoGameLog(String message) {
		if (LOGROWS >= 5) {
			GAMELOG="";
			LOGROWS = 0;
		}
		GAMELOG+= (System.getProperty("line.separator") + message);
		LOGROWS+=1;
	}

	public static String getGAMELOG() {
		return GAMELOG;
	}

}