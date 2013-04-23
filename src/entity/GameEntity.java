package entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Component;
import component.RenderComponent;


public class GameEntity {

	String id;
	Vector2f position;
	private Vector2f size = null;
	float scale;
	float rotation;
	boolean colliding = false;
	private boolean isCollidable = false;
	
	
	protected ArrayList<RenderComponent> renderComponent = null;
	protected ArrayList<Component> components = null;

	public GameEntity(String id) {
		this.id = id;
		renderComponent= new ArrayList<RenderComponent>();
		components = new ArrayList<Component>();
		position = new Vector2f(0,0);
		scale = 1;
		rotation = 0;
	}

	public void AddComponent(Component component) {
		if( RenderComponent.class.isInstance(component) ) {
			renderComponent.add( (RenderComponent)component);
		}
		component.setOwnerEntity(this);
		components.add(component);
	}

	public Component getComponent(String id) {
		for(Component comp : components) {
			if ( comp.getId().equalsIgnoreCase(id) ) {
				return comp;
			}
		}
		return null;
	}

	public Vector2f getPosition() {
		return position;
	}

	public float getScale() {
		return scale;
	}

	public float getRotation() {
		return rotation;
	}

	public String getId() {
		return id;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setRotation(float rotate) {
		rotation = rotate;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		for(Component component : components) {
			component.update(gc, sb, delta);
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		for (RenderComponent component : renderComponent) {
			component.render(gc, sb, gr);
		}
	}

	public Vector2f getSize() {
		return size;
	}

	public void setSize(Vector2f size) {
		this.size = size;
	}

	public boolean isColliding() {
		return colliding;
	}

	public void setColliding(boolean b) {
		this.colliding=b;
	}

	public boolean isCollidable() {
		return isCollidable;
	}

	public void setCollidable(boolean isCollidable) {
		this.isCollidable = isCollidable;
	}

}