package entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import component.BulletMovement;
import component.Collidable;
import component.ImageRenderComponent;

public class BulletEntity extends GameEntity {
	
	//hardcoded collisionsize
	Vector2f bulletCollisionSize = new Vector2f(10f,18f);
	
	public BulletEntity(String id, Image image, float scale) throws SlickException {
		super(id);
		setScale(scale);
		AddComponent(new ImageRenderComponent("BulletRenderComponent", image));
		AddComponent(new BulletMovement("BulletMovementComponent"));
		AddComponent(new Collidable("BulletCollidable", this, bulletCollisionSize));
	}
	
}