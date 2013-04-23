package entity;

import org.newdawn.slick.geom.Vector2f;

import component.Collidable;

public class BlockEntity extends GameEntity {

	public BlockEntity(String id,Vector2f pos, Vector2f size) {
		super(id);
		this.setPosition(pos);
		this.AddComponent(new Collidable("collision", this, size));
	}
	
}