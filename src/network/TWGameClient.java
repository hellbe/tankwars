package network;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;
import network.TWNetwork.TWPlayerStatus;
import network.TWNetwork.TWMessageContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * GameClient class; handles all the local calculations on the playing client
 * also forwards information to the connected NetworkClient
 * @author Ludde
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
	TWNetworkClient networkClient;
	/**
	 * the connected GameServer
	 */
	TWGameServer gameServer;
	/**
	 * the renderer for this client
	 */
	TWGameRenderer renderer;
	/**
	 * the active game
	 */
	TWGame game;

	/**
	 * contains information about the active map
	 */
	TWMap mapInfo;
	/**
	 * contains every entity in this game
	 */
	TWEntityContainer entities = new TWEntityContainer();
	/**
	 * contains the chathistory
	 */
	TWMessageContainer messages = new TWMessageContainer();
	/**
	 * contains information about the player's planned moves to be sent to Server
	 */
	TWPlayerStatus playerStatus = new TWPlayerStatus();
	/**
	 * true if player is currently typing a message
	 */
	boolean typing = false;

	/**
	 * gameclient constructor
	 * @param gameStateID the ID for this state (stored in TWGame)
	 * @throws SlickException
	 */
	public TWGameClient( int gameStateID, TWGame game ) throws SlickException {
		this.gameStateID = gameStateID;
		this.game = game;
	}

	
	@Override
	public void init( GameContainer gc, StateBasedGame game ) throws SlickException {
		renderer = new TWGameRenderer( this, gc );
		networkClient = new TWNetworkClient( this );
	}


	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		renderer.updateOffset();
		renderer.renderMap();
		renderer.renderEntities( entities );
		renderer.renderHealthBars( entities.getPlayers(), g);
		renderer.renderMessages( g, messages );
		if ( typing ){
			renderer.renderMessageField( gc, g, "test" );
		}
		renderer.renderScore(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame stateBasedgame, int delta) throws SlickException {
	
		sendPlayerStatus();
		
		for (TWPlayer player : entities.getPlayers()) {
			if (game.hasWon(player)) {
				//message to log is handled in TWNetworkClient disconnect() method due to synchronization issues.
				game.enterState(TWGame.MAINMENUSTATE); 
			}
		}
	}


	@Override
	public int getID() {
		return gameStateID;
	}

	public void enter(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Entering state " + getID());
		if ( TWGame.host ){
			gameServer = new TWGameServer( TWGame.mapName );
		}
		networkClient.connect( TWGame.host );
		while ( mapInfo == null ){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		renderer.loadMap(mapInfo);
	}

	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
		if ( TWGame.host ){
			gameServer.endGame();
		}
		this.networkClient.disconnect();
	}

	public void keyPressed( int key, char c ){
		handleKeyPress( key, true );
	}

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
		if( key == Input.KEY_ENTER && pressed){
			typing = true;
		}
		if( ! typing ){
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

		// handle the turn/move status to be sent to the server
		// right and forward = 1
		// left and back = -1
		// no change = 0
		if ( ! playerStatus.up && playerStatus.down ){
			playerStatus.move = -1;

			//Set reversed turn when going backwards
			if ( playerStatus.right && ! playerStatus.left ){
				playerStatus.turn = -1;
			}
			else if ( ! playerStatus.right && playerStatus.left ){
				playerStatus.turn = 1;
			} 
			else {
				playerStatus.turn = 0;
			}
		} 
		else {
			if ( playerStatus.up && ! playerStatus.down ){
				playerStatus.move = 1;
			} else {
				playerStatus.move = 0;
			}

			//Set normal turn
			if ( playerStatus.right && ! playerStatus.left ){
				playerStatus.turn = 1;
			}
			else if ( ! playerStatus.right && playerStatus.left ){
				playerStatus.turn = -1;
			} 
			else {
				playerStatus.turn = 0;
			}
		}

		playerStatus.change = true;
	}

	/**
	 * send the desired playermoves contained in playerStatus to the networkClient if it has any changes
	 */
	public void sendPlayerStatus() {
		if( playerStatus.change ){
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
	
	public int getPlayerId(){
		return networkClient.id;
	}


	public void noServerFound() {
		game.gameLog.add("Could not detect any active network server!");
		game.enterState(TWGame.MAINMENUSTATE);
	}

}

