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

	private int stateID = 0;
	TWEntityContainer entities = new TWEntityContainer();
	TiledMap map;
	TWMap mapInfo;
	TWNetworkClient networkClient;
	TWPlayerStatus playerStatus = new TWPlayerStatus();
	TWGameEntityPainter painter;
	boolean host;

	public TWGameClient( boolean host ) throws SlickException {
		this.host = host;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		painter = new TWGameEntityPainter();
		if ( host ){
			new TWGameServer();
		}
		this.networkClient = new TWNetworkClient( this );
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Vector2f position = getPlayerEntityPosition();
		System.out.println(position);
		Vector2f offset = new Vector2f();
		if ( position == null ){
			position = new Vector2f(0,0);
		}
		if( position.x > 400 ){
			offset.x = position.x - 400f;
			if ( position.x > map.getWidth() * map.getTileWidth() - 400 ){
				offset.x = map.getWidth() * map.getTileWidth() - 800;
			}
		}
		if( position.y > 300  ){
			offset.y = position.y - 300f;
			if ( position.y > map.getHeight() * map.getTileHeight() - 300 ){
				offset.y = map.getHeight() * map.getTileHeight() - 600;
			}
		}
		if( map == null ){
			if( mapInfo != null ){
				map = new TiledMap( mapInfo.path, mapInfo.folder );
			}
		} else {
			map.render( - (int) offset.x, - (int) offset.y);
		}
		for ( TWGameEntity entity : entities ){
			painter.draw( entity, offset );
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
		} else {
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
		if( playerStatus.change && playerStatus.id != 0 ){
			networkClient.send( playerStatus );
			playerStatus.change = false;
		}
	}
	
	public Vector2f getPlayerEntityPosition(){
		for ( TWGameEntity entity : entities ){
			if( entity instanceof TWPlayer ){
				if( ((TWPlayer) entity ).playerStatus.id == networkClient.id ){
					return entity.position;
				}
			}
		}
		return null;
	}


}

