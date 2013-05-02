package network;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TWGameEntityPainter {
	private static Image bulletImage;
	private static Image tankImage;
	
	public static void init(){
		//TODO: load all the resources
		try {
			tankImage = new Image("data/tank.png");
			bulletImage = new Image("data/bullet.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void draw( TWPlayer entity ){
		
	}
	
	public void draw( TWBullet entity ){
	}

	public static void draw(TWGameEntity entity) {
		tankImage.setRotation( (float) entity.direction.getTheta() );
		tankImage.drawCentered( entity.position.getX(), entity.position.getY() );
	}
}
