package network;

import org.newdawn.slick.geom.Vector2f;

/**
 * the abstract shell for every entity in the game.
 * @author Ludwig, Peter, Simon
 *
 */
public abstract class TWGameEntity {

	/**
	 * position of the entity
	 */
	Vector2f position = new Vector2f(400,200);
	/**
	 * entity direction in (0-360 * n) degrees 
	 */
	Vector2f direction = new Vector2f(0);
	/**
	 * entity size
	 */
	Vector2f size = new Vector2f(1,1);
	/**
	 * entity speed
	 */
	float speed = 0;

	/**
	 * GameEntity constructor
	 */
	public TWGameEntity () { }

	/**
	 * Get the next move, based on the delta value
	 * @param delta 
	 * @return returns the next position for this entity
	 */
	public Vector2f getFutureMove ( float delta ){
		return position.copy().add( direction.copy().scale( delta * speed ));
	}

	/**
	 * move entity forward in the current trajectory
	 * @param delta
	 */
	public void move( float delta ){
		position = getFutureMove(delta);
	}

	/**
	 * Checks if this current entity is colliding with another entity
	 * @param other The entity to check collision with
	 * @return true if has collision
	 */
	public boolean collides( TWGameEntity other ){
		float distance = this.position.distance( other.position );
		float radiusSum = ( Math.min( this.size.x, this.size.y ) + Math.min( other.size.x, other.size.y ) ) / 2;
		if ( distance <= radiusSum ){
			return true;
		}
		return false;
	}

}
