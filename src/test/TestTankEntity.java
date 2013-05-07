package test;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import component.Collidable;
import component.Component;
import component.ImageRenderComponent;
import component.Killable;
import component.RenderComponent;
import entity.GameEntity;

public class TestTankEntity extends GameEntity {

	Image tankImage=new Image("/data/battletank.png");
	Image bulletImage= new Image("/data/bullet.png");
	float hp = 60f;
	/**
	 * Tank Entity's constructor
	 * @param id the idstring of the entity
	 * @throws SlickException
	 */
	public TestTankEntity(String id, Integer[] controlConfig) throws SlickException {
		super(id);
		this.setScale(0.7f);
		this.setSize(new Vector2f(tankImage.getWidth()*scale,tankImage.getHeight()*scale));
		this.AddComponent(new ImageRenderComponent("TankImageRender", tankImage));
		this.AddComponent( new TopDownMovementTest("TankMovement", controlConfig));
		this.AddComponent(new Collidable("collidable", this, getSize()));
		this.AddComponent(new ShootableTest("shootableTank", getSize(),bulletImage, controlConfig[4]));
		this.AddComponent(new Killable("killable", hp));
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		for(Component component : components) {
			component.update(gc, sb, delta);
		}
		//skicka information om status till server
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		for (RenderComponent component : renderComponent) {
			component.render(gc, sb, gr);
		}
	}
	
}