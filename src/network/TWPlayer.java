package network;

import network.TWNetwork.TWPlayerStatus;
import org.newdawn.slick.geom.Vector2f;

/**
 * A player game entity with information about the player's life and belonging
 * @author Simon
 */
public class TWPlayer extends TWGameEntity {

	int id;
	int hp = 100;
	int score = 0;
	long lastShot = 0;
	TWPlayerStatus playerStatus = new TWPlayerStatus();
	
	public TWPlayer() {}
	
	/**
	 * Create a player 
	 * @param id of the player's connection
	 */
	public TWPlayer( Integer id ) {
		this.id = id;
		size = new Vector2f(86,40);
	}
	
	@Override
	public Vector2f getFutureMove(float delta) {
		if ( playerStatus.move == 1 || playerStatus.move == -1 || playerStatus.move == 0){
			speed = playerStatus.move * 30 * delta;
		} if ( playerStatus.turn == 1 || playerStatus.turn == -1 || playerStatus.turn == 0 ){
			direction.add( playerStatus.turn * delta * 5);
		}
		return position.copy().add( direction.copy().scale( delta * speed ));
	}

	
}
