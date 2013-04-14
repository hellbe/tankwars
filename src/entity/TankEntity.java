package entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Collidable;
import component.Component;
import component.ImageRenderComponent;
import component.RenderComponent;
import component.Shootable;
import component.TopDownMovement;

/**
 * Player-/input controlled Tank Entity. Steer using the wasd keys and shoot with '1'.
 * @author Ludde
 *
 */
public class TankEntity extends Entity {

	/**
	 * can be changed if needed or wanted.
	 */
	Image tankImage=new Image("/data/fasttank.png");
	Image bulletImage= new Image("/data/bullet.png");
	/**
	 * Tank Entity's constructor
	 * @param id the idstring of the entity
	 * @throws SlickException
	 */
	public TankEntity(String id) throws SlickException {
		super(id);
		this.setScale(0.7f);
		this.setSize(new Vector2f(tankImage.getWidth()*scale,tankImage.getHeight()*scale));
		this.AddComponent(new ImageRenderComponent("TankImageRender", tankImage));
		this.AddComponent( new TopDownMovement("TankMovement"));
		this.AddComponent(new Collidable("collidable", this, getSize()));
		this.AddComponent(new Shootable("shootableTank", getSize(),bulletImage));
	}
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException
	{
		for(Component component : components)
		{
			component.update(gc, sb, delta);
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr)
	{
		gr.drawString(position.x+ "  " + colliding, 0, 100);
		for (RenderComponent component : renderComponent) {
			component.render(gc, sb, gr);
		}
		
	}
	
	
}