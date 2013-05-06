package entity;

import org.newdawn.slick.geom.Vector2f;

import component.Collidable;

/**
 * an invisible entity that blocks a specific region on the map using the component Collidable.
 * @author Ludde
 *
 */
public class BlockEntity extends GameEntity {

	public BlockEntity(String id,Vector2f pos, Vector2f size) {
		super(id);
		this.setPosition(pos);
		this.AddComponent(new Collidable("collision", this, size));
	}
	
}