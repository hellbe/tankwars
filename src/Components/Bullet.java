package components;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
/**
 * Bullet dictionary class; contains relevant information about the bullet
 * @author Ludde
 *
 */
public class Bullet {
	
	private Vector2f position;
	private float direction;
	private Image image;
	
	/**
	 * bullet constructor
	 * @param spawn the point of which the bullet will spawn
	 * @param direction the direction of the bullet (0-360)
	 * @param image the imageresource to be loaded
	 * @throws SlickException
	 */
	public Bullet(Vector2f spawn, float direction, Image image) throws SlickException{
		this.position=new Vector2f(spawn);
		this.direction=direction;
		this.image=new Image(image.getResourceReference());
		this.image.rotate(direction);
		
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f spawn) {
		this.position = spawn;
	}

	public float getDirection() {
		return direction;
	}

	public void setDirection(float direction) {
		this.direction = direction;
	}

	public Image getImage() {
		return image;
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
}