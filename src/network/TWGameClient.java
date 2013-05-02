package network;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWPlayerStatus;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class TWGameClient extends BasicGameState {

	private int stateID = 0;
	public TWEntityContainer entities = new TWEntityContainer();
	public TiledMap map;
	public TWNetworkClient client;
	public TWPlayerStatus playerStatus;

	public TWGameClient() throws SlickException{
		new TWGameServer();
		this.client = new TWNetworkClient( this );
		this.playerStatus = new TWPlayerStatus( client.id );
	}

	public void keyPressed( int key, char c ){
		changePlayerStatus( key, true );
	}

	public void keyReleased(int key, char c){
		changePlayerStatus( key, false );
	}

	private void changePlayerStatus(int key, boolean pressed ) {
		if ( pressed ){
			switch ( key ){
			case Input.KEY_LEFT:
				playerStatus.turn = -1;
				break;
			case Input.KEY_RIGHT:
				playerStatus.turn = 1;
				break;
			case Input.KEY_UP:
				playerStatus.move = 1;
				break;
			case Input.KEY_DOWN:
				playerStatus.move = -1;
				break;
			case Input.KEY_SPACE:
				playerStatus.shoot = true;
				break;
			}
		} 
		else {
			switch ( key ){
			case Input.KEY_LEFT:
			case Input.KEY_RIGHT:
				playerStatus.turn = 0;
				break;
			case Input.KEY_UP:
			case Input.KEY_DOWN:
				playerStatus.move = 0;
				break;
			case Input.KEY_SPACE:
				playerStatus.shoot = false;
				break;
			}
		}
		playerStatus.change = true;
	}

	public void sendPlayerStatus() {
		if( playerStatus.change ){
			client.send( playerStatus );
			playerStatus.change = false;
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if( map != null ){
			map.render(0, 0);
		}
		for ( TWGameEntity entity : entities ){
			entity.draw();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		sendPlayerStatus();
	}

	@Override
	public int getID() {
		return stateID;
	}

}

