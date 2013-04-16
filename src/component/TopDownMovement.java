package component;
 
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import component.Collidable.Hitbox;

 
public class TopDownMovement extends Component {
 
	float direction;
	float speed=0.4f;
	
	//collision support
	ArrayList<Hitbox> Collidables = null;
	Hitbox hitbox = null;
	
	
	public TopDownMovement(String id) {
		this.id = id;
	}
 
	public float getSpeed()	{
		return speed;
	}
 
	public float getDirection() {
		return direction;
	}
 
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
 
		float rotation = owner.getRotation();
		float scale = owner.getScale();
		Vector2f position = owner.getPosition();
		Input input = gc.getInput();
 
        if(input.isKeyDown(Input.KEY_A)) {
        	rotation += -0.2f * delta;
        }
 
        if(input.isKeyDown(Input.KEY_D)) {
        	rotation += 0.2f * delta;
        }
 
        if(input.isKeyDown(Input.KEY_W)) {
            float hip = 0.4f * delta;
            position.x += hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
            position.y -= hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));
            
            if (position.x<0 || position.x>gc.getWidth()-owner.getSize().x) {
               	position.x -= hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
            }
            
            if (position.y<0 || position.y>gc.getHeight()-owner.getSize().y) {
                position.y += hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));   
            }
            
            //collision handling
            if (owner.isCollidable() && hasCollision(position)) {
            	//back up!
                position.y += hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));   
                position.x -= hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
            }
        }
        
        if(input.isKeyDown(Input.KEY_S)) {
        	float hip = speed * delta;
            position.x -= hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
            position.y += hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));

            if (position.x<0 || position.x>gc.getWidth()-owner.getSize().x) {
                position.x += hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
            }
            
            if (position.y<0 || position.y>gc.getHeight()-owner.getSize().y) {
                position.y -= hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));
            }
        
            //collision handling
            if (owner.isCollidable() && hasCollision(position)) {
                position.y -= hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));   
                position.x += hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
            }
        
        }
        	
        	owner.setPosition(position);
        	owner.setRotation(rotation);
        	owner.setScale(scale);
        
	}
	
	
	
	private boolean hasCollision(Vector2f position) {
		//initiate hitbox if unset
    	Collidables = Collidable.Collidables;
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
			} else {
				owner.setColliding(false);
				return false;
			}
		}
		System.out.println("nu kom vi fel!");
		return false;
	}
	
	
 
}