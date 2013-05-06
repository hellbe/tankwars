package network;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;
import network.TWNetwork.TWPlayerStatus;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class TWGameClient extends BasicGameState {

	TWNetworkClient networkClient;
	TWGameServer gameServer;
	TWGameRenderer renderer;
	
	private int gameStateID = -1;
	TWEntityContainer entities = new TWEntityContainer();
	TWMap mapInfo;
	TWPlayerStatus playerStatus = new TWPlayerStatus();
	boolean host;

	public TWGameClient( int gameStateID ) throws SlickException {
		this.gameStateID = gameStateID;
	}

	@Override
	public void init( GameContainer container, StateBasedGame game ) throws SlickException {
		renderer = new TWGameRenderer( this );
		networkClient = new TWNetworkClient( this );
	}


	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		renderer.updateOffset();
		renderer.renderMap();
		for ( TWGameEntity entity : entities ){
			renderer.renderEntity( entity );
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		sendPlayerStatus();
	}

	@Override
	public int getID() {
		return gameStateID;
	}

	public void enter(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Entering state " + getID());
		if ( TWGame.HOST ){
			gameServer = new TWGameServer();
		}
		networkClient.connect();
	}

	public void leave(GameContainer container, StateBasedGame stateBasedGame) throws SlickException {
		System.out.println("Leaving state " + getID());
		if ( TWGame.HOST ){
			gameServer.endGame();
		}
		this.networkClient.disconnect();
	}

	public void keyPressed( int key, char c ){
		updatePlayerStatus( key, true );
	}

	public void keyReleased(int key, char c){
		updatePlayerStatus( key, false );
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

		if ( playerStatus.up && ! playerStatus.down ){
			playerStatus.move = 1;

			//Set turn
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
		else if ( ! playerStatus.up && playerStatus.down ){
			playerStatus.move = -1;

			//Set turn
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
			playerStatus.move = 0;

			//Set turn
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

	public Vector2f getPlayerEntityPosition(){
		TWPlayer player = entities.getPlayer( networkClient.id );
		if( player != null ){
			return player.position;
		}
		return new Vector2f();
	}


}

