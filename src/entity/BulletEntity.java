package entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import component.BulletComponent;
import component.Collidable;
import component.ImageRenderComponent;

public class BulletEntity extends GameEntity {
	
	float bulletDamage = 20f;
	GameEntity bulletOwner;
	
	public BulletEntity(String id, Image image, float scale, GameEntity owner) throws SlickException {
		super(id);
		this.bulletOwner=owner;
		setScale(scale);
		AddComponent(new ImageRenderComponent("BulletRenderComponent", image));
		AddComponent(new BulletComponent("BulletMovementComponent", bulletDamage,bulletOwner));
		AddComponent(new Collidable("BulletCollidable", this, image));
	}
	
}