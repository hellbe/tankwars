package network;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public abstract class TWGameEntity {

	Vector2f position = new Vector2f(400,200);
	Vector2f direction = new Vector2f(0);
	Vector2f size = new Vector2f(1,1);
	float speed = 0;
	int	state = 0;
	/**
	 * 0 = standing still (normal)
	 * 1 = creating/starting
	 * 2 = moving
	 * 3 = exploding/dying/dissolving
	 */

	public TWGameEntity () {
	}

	public Vector2f getFutureMove ( float delta ){
		return position.copy().add( direction.copy().scale( delta * speed ));
	}

	public void move( float delta ){
		position = getFutureMove(delta);
	}

	public boolean collides( TWGameEntity other ){
		float distance = this.position.distance( other.position );
		float radiusSum = ( Math.min( this.size.x, this.size.y ) + Math.min( other.size.x, other.size.y ) ) / 2;
		if ( distance <= radiusSum ){
			return true;
		}
		return false;
	}
	
	public abstract void update();


}
