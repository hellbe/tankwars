package network;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class TWGameEntityPainter {
	private static Image bulletImage;
	private static Image tankImage;
	
	public TWGameEntityPainter(){
		try {
			tankImage = new Image("data/tank.png");
			bulletImage = new Image("data/bullet.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void draw( TWPlayer entity, Vector2f offset){
		tankImage.setRotation( (float) entity.direction.getTheta() );
		tankImage.drawCentered( entity.position.x - (int) offset.x , entity.position.y - (int) offset.y );
	}
	
	public void draw( TWBullet entity, Vector2f offset ){
		bulletImage.setRotation( (float) entity.direction.getTheta() );
		bulletImage.drawCentered( entity.position.x - (int) offset.x, entity.position.y - (int) offset.y );
	}

	public void draw(TWGameEntity entity, Vector2f offset) {
		if ( entity instanceof TWPlayer ){
			draw( (TWPlayer) entity, offset );
		} 
		else if ( entity instanceof TWBullet ){
			draw( (TWBullet) entity, offset );
		}
	}
}
