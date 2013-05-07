package network;

import java.util.ArrayList;

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
	 * gamelog ArrayList
	 */
	 static ArrayList<String> gameLog = new ArrayList<String>();
	/**
	 * keeps track if the current game is a host or not
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
		gameLog.add("Console log:");
		System.setProperty("java.net.preferIPv4Stack" , "true"); // Fixes host discovery problems on OSX
		AppGameContainer app = new AppGameContainer( new TWGame() );
		app.setDisplayMode(900, 700, false);
		app.setAlwaysRender(true);
		app.start();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new TWMenuState(MAINMENUSTATE, this));
		this.addState(new TWMapMenuState(MAPMENUSTATE, this));
		this.addState(new TWGameClient(GAMESTATE, this));
	}
	
	/**
	 * primitive method to add messages to the game log, 
	 * messages should fit only one row in the main console in order for the method to work properly. 
	 * @param message to add to the console
	 */
	public void addtoGameLog(String message) {
		if ( gameLog.size() > 5) {
			gameLog.clear();
			gameLog.add("Console log:");
		}
		gameLog.add(System.getProperty("line.separator") + message);
	}

	/**
	 * get the gamelog
	 * @return gameLog
	 */
	public ArrayList<String> getGameLog() {
		return gameLog;
	}
	
	/**
	 * method controlling the victory conditions
	 * @param player to check
	 * @return true if player has won
	 */
	public boolean hasWon(TWPlayer player) {
		return player.score == 10;
	}

}