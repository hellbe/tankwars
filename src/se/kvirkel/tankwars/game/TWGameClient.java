package se.kvirkel.tankwars.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import se.kvirkel.tankwars.network.TWNetworkClient;
import se.kvirkel.tankwars.network.TWNetwork.TWEntityContainer;
import se.kvirkel.tankwars.network.TWNetwork.TWMap;
import se.kvirkel.tankwars.network.TWNetwork.TWMessageContainer;
import se.kvirkel.tankwars.network.TWNetwork.TWPlayerStatus;

/**
 * GameClient class; handles all the local calculations on the playing client
 * also forwards information to the connected NetworkClient
 * @author Ludwig, Peter, Simon
 *
 */
public class TWGameClient extends BasicGameState {
	/**
	 * stateID
	 */
	private int gameStateID = -1;
	/**
	 * the connected NetworkClient for this GameClient
	 */
	private TWNetworkClient networkClient;
	/**
	 * the connected GameServer
	 */
	private TWGameServer gameServer;
	/**
	 * the renderer for this client
	 */
	private TWGameRenderer renderer;
	/**
	 * the active game
	 */
	public TWGame game;
	/**
	 * contains information about the active map
	 */
	public TWMap mapInfo;
	/**
	 * contains every entity in this game
	 */
	public TWEntityContainer entities = new TWEntityContainer();
	/**
	 * contains the chathistory
	 */
	public TWMessageContainer messages = new TWMessageContainer();
	/**
	 * contains information about the player's planned moves to be sent to Server
	 */
	public TWPlayerStatus playerStatus = new TWPlayerStatus();
	/**
	 * true if player is currently typing a message
	 */
	public  boolean typing = false;
	/**
	 * gameclient constructor
	 * @param gameStateID the ID for this state (stored in TWGame)
	 * @throws SlickException
	 */
	public TWGameClient( int gameStateID, TWGame game ) throws SlickException {
		this.gameStateID = gameStateID;
		this.game = game;
	}

	/**
	 * Called when the state is initialized, loads resources for future use
	 */
	@Override
	public void init( GameContainer gc, StateBasedGame game ) throws SlickException {
		renderer = new TWGameRenderer( this, gc );
		networkClient = new TWNetworkClient( this );
	}

	/**
	 * Part of the clients game loop
	 * Called when the client wants to paint the content of the game 
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		/**
		 * Prevent render from running before enter() has been called and network connection
		 * is surely established
		 */
		if ( networkClient.id == null ){
			return;
		}
		/**
		 * Render the different layers of content
		 */
		renderer.updateOffset();
		renderer.renderMap();
		renderer.renderEntities( entities );
		renderer.renderHealthBars( entities.getPlayers(), g);
		renderer.renderMessages( g, messages );
		if ( typing ){
			renderer.renderMessageField( gc, g );
		}
		renderer.renderScore(g);
	}

	/**
	 * Part of the client's game loop
	 * Called when the client want to update the world, which is does by
	 * sending it's player status to the server
	 */
	@Override
	public void update(GameContainer container, StateBasedGame stateBasedgame, int delta) throws SlickException {
		sendPlayerStatus();
		for (TWPlayer player : entities.getPlayers()) {
			if (game.hasWon(player)) {
				game.enterState(TWGame.MAINMENUSTATE); 
			}
		}
	}

	/**
	 * Returns the game state ID to be able to navigate between states
	 */
	@Override
	public int getID() {
		return gameStateID;
	}

	/**
	 * Executes when the users enter the state. Connects to the network server,
	 * waits for the server to send a map and then loads the map
	 */
	public void enter(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		if ( TWGame.host ){
			gameServer = new TWGameServer( TWGame.mapName );
		}
		if ( ! networkClient.connect( TWGame.host )){
			noServerFound();
			return;
		}
		while ( mapInfo == null ){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		renderer.loadMap(mapInfo);
	}

	/**
	 * Executes when the client leaves the state
	 * Closes the gameServer properly and disconnects the client
	 */
	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		if ( TWGame.host ){
			gameServer.endGame();
		}
		this.networkClient.disconnect();
	}

	/**
	 * Called when a key is pressed down
	 */
	public void keyPressed( int key, char c ){
		handleKeyPress( key, true );
	}

	/**
	 * Called when a key is released
	 */
	public void keyReleased(int key, char c){
		handleKeyPress( key, false );
	}

	/**
	 * method to handle keypresses, 
	 * brings up the chatbox if enter is pressed; else updates the playerstatus
	 * @param key the pressed key
	 * @param pressed true if the key is down
	 */
	public void handleKeyPress( int key, boolean pressed ){
		if (key == Input.KEY_ENTER && pressed) {
			typing = true;
		}
		if ( ! typing) {
			updatePlayerStatus(key,pressed);
		}
	}

	/**
	 * Check if the playerstatus needs to be updated and therefore sent to server
	 * @param key key pressed
	 * @param pressed true if key is down
	 */
	public void updatePlayerStatus(int key, boolean pressed ) {
		switch ( key ){
		case Input.KEY_LEFT:
			playerStatus.left = pressed;
			break;
		case Input.KEY_RIGHT:
			playerStatus.right = pressed;
			break;
		case Input.KEY_UP:
			playerStatus.up = pressed;
			break;
		case Input.KEY_DOWN:
			playerStatus.down = pressed;
			break;
		case Input.KEY_SPACE:
			playerStatus.shoot = pressed;
			break;
		}
		if ( ! playerStatus.up && playerStatus.down ){
			playerStatus.move = -1;
			/**
			 * Set reversed turn when going backwards
			 */
			if ( playerStatus.right && ! playerStatus.left ){
				playerStatus.turn = -1;
			} else if ( ! playerStatus.right && playerStatus.left ){
				playerStatus.turn = 1;
			} else {
				playerStatus.turn = 0;
			}
		} else {
			if ( playerStatus.up && ! playerStatus.down ){
				playerStatus.move = 1;
			} else {
				playerStatus.move = 0;
			}
			/**
			 * Set normal movement
			 */
			if ( playerStatus.right && ! playerStatus.left ){
				playerStatus.turn = 1;
			} else if ( ! playerStatus.right && playerStatus.left ){
				playerStatus.turn = -1;
			} else {
				playerStatus.turn = 0;
			}
		}
		playerStatus.change = true;
	}

	/**
	 * send the desired playermoves contained in playerStatus to the networkClient if it has any changes
	 */
	public void sendPlayerStatus() {
		if (playerStatus.change) {
			networkClient.send( playerStatus );
			playerStatus.change = false;
		}
	}

	/**
	 * send a chatmessage to the networkClient
	 * @param message the chatmessage
	 */
	public void sendMessage( String message ){
		if ( message != ""){
			networkClient.send(message);
		}
		typing = false;
	}

	/**
	 * gets the local player's position from the entitylist
	 * @return
	 */
	public Vector2f getPlayerEntityPosition(){
		TWPlayer player = entities.getPlayer( networkClient.id );
		if( player != null ){
			return player.position;
		}
		return new Vector2f();
	}

	/**
	 * Returns the client's player id 
	 * @return the player id
	 */
	public int getPlayerId(){
		return networkClient.id;
	}

	/**
	 * Called if no network server was found, return to main menu and
	 * inform the user
	 */
	public void noServerFound() {
		game.gameLog.add("Could not detect any active network server!");
		game.enterState(TWGame.MAINMENUSTATE);
	}

}

