package network;

import org.newdawn.slick.geom.Vector2f;
/**
 * Bullet class with bullet specific information
 * @author Simon
 */
public class TWBullet extends TWGameEntity {
	/**
	 * The id of the player that shot the bullet
	 */
	int playerId;
	/**
	 * Constructor
	 */
	public TWBullet() { }
	
	/**
	 * Create a bullet
	 * @param playerId the id of the player that shot the bullet
	 * @param position where bullet starts
	 * @param direction of the bullet
	 */
	public TWBullet(int playerId, Vector2f position, Vector2f direction) {
		this.speed = 40;
		this.size = new Vector2f(25,25);
		this.playerId = playerId;
		this.position = position;
		this.direction = direction;
	}
	
}
