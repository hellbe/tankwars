package se.kvirkel.tankwars.game;

import org.newdawn.slick.geom.Vector2f;

import se.kvirkel.tankwars.network.TWNetwork.TWPlayerStatus;

/**
 * Playerclass, contains useful information about every player
 * @author Ludwig, Peter, Simon
 */
public class TWPlayer extends TWGameEntity {

	/** player id */
	public int id;
	
	/**
	 * player hp
	 */
	public int hp = 100;
	
	/**
	 * player score
	 */
	public int score = 0;
	
	/**
	 * the time of the last shot, used when calculating next possible shot
	 */
	public long lastShot = 0;
	
	/** 
	 * status container
	 */
	public TWPlayerStatus playerStatus = new TWPlayerStatus();
	
	/**
	 * TWPlayer constructor
	 */
	public TWPlayer() {}
	
	/**
	 * TWPlayer constructor, also sets the player size
	 * @param id the player id
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
