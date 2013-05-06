package entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import component.ImageRenderComponent;

public class PowerUpEntity extends BlockEntity {

	Vector2f size;
	String imgPath = "data/dot.png";
	
	public PowerUpEntity(String id, Vector2f pos, Vector2f size) throws SlickException {
		super(id, pos, size);
		this.size=size;
		AddComponent(new ImageRenderComponent("image", new Image(imgPath)));	
		setPosition(new Vector2f(pos));
	}
	
}