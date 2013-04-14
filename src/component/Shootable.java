package component;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import entity.BulletEntity;

public class Shootable extends RenderComponent {

	ArrayList<BulletEntity> bulletList = null;
	ArrayList<BulletEntity> removeList = null;
	
	private long lastFire;
	private long fireDelay = 400;
	private float bulletScale=1.0f;
	private int maximum_projectiles = 20;
	private Image bulletImage = null;
	
	public Shootable(String id, Vector2f entitySize, Image image) {
		super(id);
		this.bulletImage=image;
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		
		
		//h�ller man knappe intryckt s� roteras projektilen. Fixa!
		if (gc.getInput().isKeyDown(Input.KEY_1) && gc.getTime()-lastFire >= fireDelay) {
			
			lastFire=gc.getTime();
			//h�r m�ste det pillas lite s� att de slutar krocka!	
			
			
			float xspawn = owner.getPosition().x + (float) ((float) (owner.getSize().x*owner.getScale()+bulletImage.getWidth()*bulletScale)*java.lang.Math.sin(java.lang.Math.toRadians(owner.getRotation())));
			float yspawn = owner.getPosition().y - (float) ((float) (owner.getSize().y*owner.getScale()+bulletImage.getHeight()*bulletScale)*java.lang.Math.cos(java.lang.Math.toRadians(owner.getRotation())));
			
			Vector2f bulletspawn = new Vector2f(xspawn,yspawn);
			//Vector2f bulletspawn = new Vector2f((owner.getPosition().x+(owner.getSize().x-bulletImage.getWidth())/2.0f), (owner.getPosition().y-bulletImage.getHeight()*bulletScale-1.0f));
			
			
			
			BulletEntity tmp=null;
			
			if (bulletList==null){
				bulletList=new ArrayList<BulletEntity>();
				removeList=new ArrayList<BulletEntity>();
				bulletList.add(tmp=new BulletEntity("bullet", bulletImage,bulletScale));
				tmp.setPosition(bulletspawn);
				tmp.setRotation(owner.getRotation());
				tmp = null;
			}
			else if (bulletList.size()<=maximum_projectiles){
				bulletList.add(tmp=new BulletEntity("bullet", bulletImage,bulletScale));
				tmp.setPosition(bulletspawn);
				tmp.setRotation(owner.getRotation());
				tmp = null;
			}
		}
		
		if (bulletList!=null) {
			for (BulletEntity bullet : bulletList) {

				if ( bullet.getPosition().x > gc.getWidth() || bullet.getPosition().x<0) {
					removeList.add(bullet);	
				}
				if ( bullet.getPosition().y > gc.getHeight()+40 || bullet.getPosition().y<-40) {
					removeList.add(bullet);	
				}

			}

			if (removeList!=null) {	
				for (BulletEntity remobj : removeList) {
					bulletList.remove(remobj);
				}
				removeList.clear();

			}
		}
		if (bulletList!= null) {	
			for (BulletEntity bullet : bulletList) {
				bullet.update(gc, sb, delta);
				bullet.render(gc, sb, gc.getGraphics());
			}
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		if (bulletList!= null) {	
			for (BulletEntity bullet : bulletList) {
				bullet.render(gc, sb, gr);
			}
		}
	}
	
}