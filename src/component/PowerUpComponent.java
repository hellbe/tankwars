package component;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Collidable.Hitbox;
import entity.GameEntity;

public class PowerUpComponent extends Component {

	Rectangle hitbox;
	GameEntity collidingWith;
	String type;
	
	public PowerUpComponent(String id, String type) {
		this.id=id;
		this.type=type;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		
		
		Vector2f position = owner.getPosition();
		Vector2f size = owner.getSize();

		if (position != null && size != null) {
	
			//initiate hitbox if unset
			if (hitbox==null) {
				hitbox = new Rectangle(position.x, position.y, size.x, size.y);
			}
			//update hitbox values
			hitbox.setBounds(position.x, position.y, size.x, size.y);

			
			//check collisions to other objects..
			ArrayList<Hitbox> Collidables = Collidable.Collidables;
			if (Collidables != null) {
				boolean foundCollision=false;
				for (Hitbox o : Collidables) {
					if (this.hitbox.intersects(o.box)) {
						foundCollision=true;
						collidingWith=o.owner;
						break;
					}
				}    		
				if (foundCollision) {
					
					if (this.type.equalsIgnoreCase("doubledamage") && collidingWith.getComponent("shootable")!=null){
					((Shootable)collidingWith.getComponent("shootable")).setDoubleDamage();
					}

					//lägg till mer powerups här om ni vill! fortsätt som ovan.
					
					owner.setPosition(new Vector2f(gc.getWidth()+200, 0));
					
				}
				collidingWith=null;
			}
		}
		
	}
	
}