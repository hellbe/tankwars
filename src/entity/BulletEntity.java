package entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import component.BulletMovement;
import component.Collidable;
import component.ImageRenderComponent;

public class BulletEntity extends GameEntity {
	
	float bulletDamage = 20f;
	
	public BulletEntity(String id, Image image, float scale) throws SlickException {
		super(id);
		setScale(scale);
		AddComponent(new ImageRenderComponent("BulletRenderComponent", image));
		AddComponent(new BulletMovement("BulletMovementComponent", bulletDamage));
		AddComponent(new Collidable("BulletCollidable", this, image));
	}
	
}