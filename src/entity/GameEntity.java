package entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Component;
import component.RenderComponent;

/**
 * fully functioning generic game entity class, you can add components to this such as movement or an image
 * contains the information that every entity in the world needs.
 * @author Ludde
 *
 */
public class GameEntity {

	//entity id
	String id;
	//position of the entity (x,y)
	Vector2f position;
	//size of the entity in (x,y) 
	private Vector2f size = null;
	//entity size scale (often used when rendering images)
	protected float scale;
	//entity rotation
	float rotation;
	//is the object currently colliding?
	boolean colliding = false;
	//set to true by the Collidable component if the object is collidable
	private boolean isCollidable = false;
	
	//respective component lists, these lists are iterated through in order to update entity values,
	//since no entity does it's own calculations.
	protected ArrayList<RenderComponent> renderComponent = null;
	protected ArrayList<Component> components = null;

	/**
	 * Game entity constructor
	 * @param id entity id
	 */
	public GameEntity(String id) {
		this.id = id;
		renderComponent= new ArrayList<RenderComponent>();
		components = new ArrayList<Component>();
		position = new Vector2f(0,0);
		scale = 1;
		rotation = 0;
	}

	/**
	 * add a component to this identity, in other word give the entity some property such as movement
	 * @param component
	 */
	public void AddComponent(Component component) {
		if( RenderComponent.class.isInstance(component) ) {
			renderComponent.add( (RenderComponent)component);
		}
		component.setOwnerEntity(this);
		components.add(component);
	}

	/**
	 * Get a component (property) from the entity. Often requires casts and should be avoided when possible.
	 * @param id the id of the component to get (in ignorecase)
	 * @return
	 */
	public Component getComponent(String id) {
		for(Component comp : components) {
			if ( comp.getId().equalsIgnoreCase(id) ) {
				return comp;
			}
		}
		return null;
	}

	/**
	 * get position of the entity
	 * @return
	 */
	public Vector2f getPosition() {
		return position;
	}
	/**
	 * get entity scale
	 * @return
	 */
	public float getScale() {
		return scale;
	}
	/**
	 * get entity rotation
	 * @return
	 */
	public float getRotation() {
		return rotation;
	}
	/**
	 * get entity id
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set the entity position (be careful not to make the entity follow another, though, since the method calls for
	 * a reference)
	 * @param position the new position for the entity
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	/**
	 * set rotation for the entity
	 * @param rotation in degrees (0-360)
	 */
	public void setRotation(float rotate) {
		rotation = rotate;
	}
	/**
	 * set scale of the entity
	 * @param desired scale (1f is default)
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * for every component that this entity has => update
	 * @param gc
	 * @param sb
	 * @param delta
	 * @throws SlickException
	 */
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		for(Component component : components) {
			component.update(gc, sb, delta);
		}
	}

	/**
	 * updates every rendercomponent that this entity owns.
	 * @param gc
	 * @param sb
	 * @param gr
	 */
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