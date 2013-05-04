package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import client.TWClient;

public class Killable extends RenderComponent {

	private float maxHp;
	private float hp;
	private boolean isAlive=true;
	/**
	 * Makes the Entity killable
	 * @param id should be "killable" in order to work with the BulletMovement and hit-detection.
	 * @param hp hitpoints to add to the owner entity
	 */
	public Killable(String id, float hp) {
		super(id);
		this.hp = hp;
		this.maxHp= hp;
	}
	
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		
		if (hp<=0f && isAlive) {
			Vector2f pos = owner.getPosition();
			//move tank out of the picture TODO: remove tank entirely from game
			System.out.println(owner.toString()  +" was killed!");
			pos.x=gc.getWidth()+owner.getSize().x;
			owner.setPosition(pos);
			isAlive=false;
			sbg.enterState(TWClient.ENDGAMESTATE, new FadeOutTransition(Color.black, 3000), new FadeInTransition(Color.white, 2000));
		}
	}
	
	/**adds hp to the component
	 * can also subtract if dHp<0 
	 * @param dHp
	 */
	public void addHp(float hp) {
		this.hp+=hp;
	}


	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {

		if (owner.getPosition()!=null && owner.getSize()!=null) {

			if (hp/maxHp >=0.5f) {
				gr.setColor(Color.green);
			}
			else if (hp/maxHp > 0.2f && hp/maxHp < 0.5f) {
				gr.setColor(Color.yellow);
			}
			else {
				gr.setColor(Color.red);
			}
			gr.fillRect(owner.getPosition().x, owner.getPosition().y-gc.getHeight()*0.02f-2f, (hp/maxHp)*owner.getSize().x, gc.getHeight()*0.015f);
			gr.setColor(Color.black);
			gr.drawRect(owner.getPosition().x, owner.getPosition().y-gc.getHeight()*0.02f-2f, owner.getSize().x, gc.getHeight()*0.015f);
		}
		
		
	}
	
}