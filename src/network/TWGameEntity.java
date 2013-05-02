package network;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class TWGameEntity {
	
	Vector2f position;
	Vector2f direction;
	Vector2f size;
	float speed;
	
	public TWGameEntity () throws SlickException {
		Image image = new Image("data/dot.png");
		size = new Vector2f( image.getHeight(), image.getWidth() );
		image.setCenterOfRotation( size.getX() / 2, size.getY() / 2 );
		direction = new Vector2f(0);
		position = new Vector2f(50,50);
		speed = 0;
	}
	
	public Vector2f getFutureMove ( float delta ){
		return position.copy().add( direction.copy().scale( delta * speed ));
	}
	
	public void move( float delta ){
		position = getFutureMove(delta);
	}
	
	public void move( long delta, int rotation ){
		position = getFutureMove(delta);
		if ( rotation == 1 || rotation == -1){
			direction.add( rotation * delta );
		}
	}
	
	public void draw () throws SlickException{
		Image image = new Image("data/dot.png");
		image.setRotation( (float) direction.getTheta() );
		image.drawCentered( position.getX(), position.getY() );
	}
	
	
	
}
