package entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import component.BulletMovement;
import component.Collidable;
import component.ImageRenderComponent;

public class BulletEntity extends Entity {
	
	
	public BulletEntity(String id, Image image, float scale) throws SlickException {
		
		super(id);
		setScale(scale);
		AddComponent(new ImageRenderComponent("BulletRenderComponent", image));
		AddComponent(new BulletMovement("BulletMovementComponent"));
		AddComponent(new Collidable("BulletCollidable", this, image));
		
	}
	
}