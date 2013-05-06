package entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import component.BulletComponent;
import component.Collidable;
import component.ImageRenderComponent;

/**
 * BulleEntity class that spawns a collidable bullet that moves forward in a single line,
 * and that damages killable entities it hits.
 * @author Ludde
 *
 */
public class BulletEntity extends GameEntity {
	
	//projectile damage
	float pDamage;
	// the entity that initiated this bullet
	GameEntity bulletOwner;
	
	/**
	 * Bullet entity constructor,
	 * @param id - any string to identify the object
	 * @param pDamage - the desired projectile damage
	 * @param image - bullet image
	 * @param scale - bullet image scale if needed (else 1f)
	 * @param owner - the entity that initiated this bullet (e.i. tank)
	 * @throws SlickException
	 */
	public BulletEntity(String id, float pDamage, Image image, float scale, GameEntity owner) throws SlickException {
		super(id);
		this.bulletOwner=owner;
		this.pDamage=pDamage;
		setScale(scale);
		AddComponent(new ImageRenderComponent("BulletRenderComponent", image));
		AddComponent(new BulletComponent("BulletMovementComponent", pDamage,bulletOwner));
		AddComponent(new Collidable("BulletCollidable", this, image));
	}
	
}