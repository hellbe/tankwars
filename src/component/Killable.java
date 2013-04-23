package component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Killable extends Component {

	private float hp;
	private boolean isAlive=true;
	/**
	 * Makes the Entity killable
	 * @param id should be "killable" in order to work with the BulletMovement and hit-detection.
	 * @param hp hitpoints to add to the owner entity
	 */
	public Killable(String id, float hp) {
		this.id = id;
		this.hp = hp;
	}
	
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		
		if (hp<=0f && isAlive) {
			Vector2f pos = owner.getPosition();
			//move tank out of the picture TODO: remove tank entirely from game
			System.out.println(owner.toString()  +" was killed!");
			pos.x=gc.getWidth()+owner.getSize().x;
			owner.setPosition(pos);
			isAlive=false;
		}
		
		
	}
	
	/**adds hp to the component
	 * can also subtract if dHp<0 
	 * @param dHp
	 */
	public void addHp(float hp) {
		this.hp+=hp;
	}
	
}