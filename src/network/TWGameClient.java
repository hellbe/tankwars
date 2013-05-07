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

public class TWGameClient extends BasicGameState {

	private int gameStateID = -1;
	TWNetworkClient networkClient;
	TWGameServer gameServer;
	TWGameRenderer renderer;
	StateBasedGame game;

	TWMap mapInfo;
	TWEntityContainer entities = new TWEntityContainer();
	TWMessageContainer messages = new TWMessageContainer();
	TWPlayerStatus playerStatus = new TWPlayerStatus();
	boolean typing = false;

	public TWGameClient( int gameStateID ) throws SlickException {
		this.gameStateID = gameStateID;
	}

	@Override
	public void init( GameContainer gc, StateBasedGame game ) throws SlickException {
		this.game = game;
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
			renderer.renderMessageBox( gc, g, "test" );
		}
		renderer.renderScore(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
	
		sendPlayerStatus();
		
		for (TWPlayer player : entities.getPlayers()) {
			if (TWGame.hasWon(player)) {
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

	public void handleKeyPress( int key, boolean pressed ){
		if( key == Input.KEY_ENTER && pressed){
			typing = true;
		}
		if( ! typing ){
			updatePlayerStatus(key,pressed);
		}

	}

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

	public void sendPlayerStatus() {
		if( playerStatus.change ){
			networkClient.send( playerStatus );
			playerStatus.change = false;
		}
	}

	public void sendMessage( String message ){
		if ( message != ""){
			networkClient.send(message);
		}
		typing = false;
	}

	public Vector2f getPlayerEntityPosition(){
		TWPlayer player = entities.getPlayer( networkClient.id );
		if( player != null ){
			return player.position;
		}
		return new Vector2f();
	}

}

