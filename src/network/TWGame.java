package network;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
/**
 * main class of TankWars game; contains each gamestate and some static variables and methods
 * @author Ludde
 *
 */
public class TWGame extends StateBasedGame {
	
	/**
	 * the mainmenu gamestate
	 */
	public static final int MAINMENUSTATE = 0;
	/**
	 * the choose map gamestate
	 */
	public static final int MAPMENUSTATE = 1;
	/**
	 * the acutal gameplay state
	 */
	public static final int GAMESTATE = 2;
	/**
	 * The static log for main menu, should only be modified by the addtoGameLog-method
	 */
	private static String GAMELOG = "Game log:";
	/**
	 * number of rows currently saved in gamelog
	 */
	private static int LOGROWS = 0;
	
	/**
	 * keeps track if the current client is a host or not
	 */
	public static boolean host = false;
	/**
	 * name of the active map
	 */
	static String mapName;

	/**
	 * game constructor
	 * @throws SlickException
	 */
	public TWGame() throws SlickException {
		super("TankWars");
	}

	/**
	 * main method of game TankWars
	 * @param args
	 * @throws SlickException
	 */
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
	}
	
	/**
	 * primitive static method to add messages to the game log, 
	 * messages should fit only one row in the main console in order for the method to work properly. 
	 * @param message to add to the console
	 */
	public static void addtoGameLog(String message) {
		if (LOGROWS >= 5) {
			GAMELOG="";
			LOGROWS = 0;
		}
		GAMELOG+= (System.getProperty("line.separator") + message);
		LOGROWS+=1;
	}

	/**
	 * get the static gamelog
	 * @return TWGame.GAMELOG
	 */
	public static String getGAMELOG() {
		return GAMELOG;
	}
	
	/**
	 * static method controlling the victory conditions
	 * @param player to check
	 * @return true if player has won
	 */
	public static boolean hasWon(TWPlayer player) {
		return player.score==10;
	}

}