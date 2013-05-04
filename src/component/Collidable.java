package component;
 
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import entity.GameEntity;

public class Collidable extends Component {
 
	public class Hitbox {
		public GameEntity owner;
		public Rectangle box;
		
		public Hitbox(GameEntity owner, Rectangle hitbox) {
			this.owner=owner;
			this.box=hitbox;	
		}
		
	}
	
	public static ArrayList<Hitbox> Collidables = null;

	Hitbox hitbox;
	
	public Collidable( String id ,GameEntity e, Vector2f size) {
		this.id = id;
		
		if (Collidables==null) {
			Collidables=new ArrayList<Hitbox>();
			Collidables.add(hitbox=new Hitbox(e,new Rectangle(e.getPosition().x, e.getPosition().y, size.x,size.y)));
		} else {
			Collidables.add(hitbox=new Hitbox(e,new Rectangle(e.getPosition().x, e.getPosition().y, size.x,size.y)));
		}

		if (e.getSize()==null) {
			e.setSize(size);
		}
		
		System.out.println(e.toString() +" now is collidable with size " + e.getSize().toString());
		e.setCollidable(true);
	}
	
	public Collidable( String id ,GameEntity e, Image img) {
		Vector2f size=new Vector2f(img.getWidth(),img.getHeight());
		this.id = id;
		
		if (Collidables==null) {
			Collidables=new ArrayList<Hitbox>();
			Collidables.add(hitbox=new Hitbox(e,new Rectangle(e.getPosition().x, e.getPosition().y, size.x,size.y)));
		} else {
			Collidables.add(hitbox=new Hitbox(e,new Rectangle(e.getPosition().x, e.getPosition().y, size.x,size.y)));
		}
		
		if (e.getSize()==null) {
			e.setSize(size);
		}
		
		System.out.println(e.toString() +" now is collidable with size " + e.getSize().toString());
		e.setCollidable(true);
	}


	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		
		hitbox.box.setBounds(owner.getPosition().x, owner.getPosition().y, owner.getSize().x, owner.getSize().y);
		
	}
	
}