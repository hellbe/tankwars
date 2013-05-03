package network;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;
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
	TWEntityContainer entities = new TWEntityContainer();
	TiledMap map;
	TWMap mapInfo;
	TWNetworkClient client;
	TWPlayerStatus playerStatus = new TWPlayerStatus();
	TWGameEntityPainter painter = new TWGameEntityPainter();
	boolean host;
	
	public TWGameClient(boolean host) throws SlickException{ 
		this.host = host;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		painter.init();
		if ( host ){
			new TWGameServer();
		}
		this.client = new TWNetworkClient( this );
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if( map == null ){
			if( mapInfo != null ){
				map = new TiledMap( mapInfo.path, mapInfo.folder );
			}
		} else {
			map.render(0, 0);
		}
		for ( TWGameEntity entity : entities ){
			painter.draw( entity );
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
				if ( playerStatus.turn == 0 )
					playerStatus.turn = -1;
				break;
			case Input.KEY_RIGHT:
				if ( playerStatus.turn == 0 )
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
				if ( playerStatus.turn == -1 )
					playerStatus.turn = 0;
				break;
			case Input.KEY_RIGHT:
				if ( playerStatus.turn == 1 )
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
		if( playerStatus.change && playerStatus.id != 0 ){
			client.send( playerStatus );
			playerStatus.change = false;
		}
	}


}

