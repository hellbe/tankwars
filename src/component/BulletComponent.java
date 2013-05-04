package component;
 
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Collidable.Hitbox;
import entity.GameEntity;

 
public class BulletComponent extends Component {
	
	boolean active = true;
	
	float direction;
	float speed=0.8f;
	//collision handling
	Hitbox hitbox;
	float pDamage;
	
	GameEntity collidingWith = null;
	GameEntity bulletOwner;

	int hitCounter=0;
	
	public BulletComponent(String id, float pDamage, GameEntity bulletOwner) {
		this.id = id;
		this.pDamage = pDamage;
		this.bulletOwner=bulletOwner;
	}
 
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
 
		if (active) {

			float rotation = owner.getRotation();
			float scale = owner.getScale();
			Vector2f position = owner.getPosition();

			float hip = speed * delta;

			position.x += hip * java.lang.Math.sin(java.lang.Math.toRadians(rotation));
			position.y -= hip *java.lang.Math.cos(java.lang.Math.toRadians(rotation));

			if (hasCollision(position)) {

				//if is colliding at current position => subtract hp and remove bullet
				Killable enemyKillable = (Killable) collidingWith.getComponent("killable");
				if (enemyKillable != null) {
					enemyKillable.addHp(-1f*pDamage);
				}

				position.y = gc.getHeight() + owner.getSize().y;
				active = false;
				System.out.println("hit!");	
				collidingWith=null;
			}

			owner.setPosition(position);
			owner.setRotation(rotation);
			owner.setScale(scale);

		}
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
				
				//checks so that we dont collide with ourselves or any other (heritage of) BulletEntity
				if (o!=this.hitbox && !owner.getClass().isAssignableFrom( o.owner.getClass())) {
				
					if (this.hitbox.box.intersects(o.box)) {
						collidingWith=o.owner;
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
		System.out.println("UNEXPECTED ERROR IN " + this.toString() + ": Collidables is a nullpointer.");
		return false;
	}
 
}