package entity;

import org.newdawn.slick.geom.Vector2f;

import component.Killable;

public class PowerUpEntity extends BlockEntity {

	public PowerUpEntity(String id, Vector2f pos, Vector2f size) {
		super(id, pos, size);
		
		AddComponent(new Killable("killable", 1f));
	}
	
	
}