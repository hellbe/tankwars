package gameplay;

import org.newdawn.slick.geom.Vector2f;

public abstract class GameObject {
	Vector2f position, size;
	
	public void move ( float distance, float direction ){
		Vector2f vector = new Vector2f( direction );
		position.add( vector.scale( distance ));
	}
	
	public boolean collides( GameObject other ){
		if( this.position.distance( other.position ) < this.getRadius() + other.getRadius() ){
			return true;
		}
		return false;
	}
	
	private float getRadius(){
		return Math.min( this.size.x, this.size.y ) / 2;
	}
	
}