package entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import component.ImageRenderComponent;
import component.PowerUpComponent;

/**
 * An entity with power up - properties. Creates a powerupentity of desired type at a given position and size.
 * 
 * current types supported:
 * "doubledamage"
 * 
 * @author Ludde
 *
 */
public class PowerUpEntity extends GameEntity {

	Vector2f size;
	String imgPath = "data/dot.png";
	/**
	 * powerupentity constructor
	 * @param id
	 * @param spawn position
	 * @param power up type (see supported types in classdoc)
	 * @param size
	 * @throws SlickException
	 */
	public PowerUpEntity(String id, Vector2f pos, String type, Vector2f size) throws SlickException {
		super(id);
		this.size=size;
		
		AddComponent(new ImageRenderComponent("image", new Image(imgPath)));
		AddComponent(new PowerUpComponent("powComponent",type));
		setPosition(pos);
	}
	
}