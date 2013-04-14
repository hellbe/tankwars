package component;
 
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import Components.Basic.Component;
import Components.Collidable.Hitbox;

 
public class BulletMovement extends Component {
 
	float direction;
	float speed=0.8f;
	//collision handling
	Hitbox hitbox;
	
	
 
	public BulletMovement(String id)
	{
		this.id = id;
	}
 
	public float getSpeed()
	{
		return speed;
	}
 
	public float getDirection()
	{
		return direction;
	}
 
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
 
		float rotation = owner.getRotation();
		float scale = owner.getScale();
		Vector2f position = owner.getPosition();

		float hip = speed * delta;
		
		position.x += hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
		position.y -= hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));

		if (hasCollision(position)) {
			position.x -= hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
			position.y += hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));
			//kolla om vi tr�ffat ett objekt som kan skadas, ta bort hp i s� fall och ta sedan bort projektilen.
		}
		
		
		owner.setPosition(position);
		owner.setRotation(rotation);
		owner.setScale(scale);
        
	}
	
	private boolean hasCollision(Vector2f position) {
		//initiate hitbox if unset
		
		ArrayList<Hitbox> Collidables = Collidable.Collidables;
    	if (this.hitbox == null && Collidables != null) {
    		for (Hitbox o : Collidables) {
				if (o.owner==owner) {
					hitbox = o;
					System.out.println(this.toString() + ": hitbox set.");
					break;
				}
			}
    	}
    	//check collisions to other objects..
    	
    	
    	
    	hitbox.box.setBounds(position.x, position.y, owner.getSize().x, owner.getSize().y);
		
    	
    	
    	if (Collidables != null) {
			boolean foundCollision=false;
			
			for (Hitbox o : Collidables) {
				if (o!=this.hitbox) {
				
					if (this.hitbox.box.intersects(o.box)) {
						foundCollision=true;
						break;
					}
				}	
			}
				
			if (foundCollision) {
				owner.setColliding(true);
				return true;
			}
			else {
				owner.setColliding(false);
				return false;
			}
		}
		System.out.println("nu kom vi fel!");
		return false;
	}
	
	
 
}