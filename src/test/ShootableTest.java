package test;

import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.RenderComponent;

import entity.BulletEntity;

public class ShootableTest extends RenderComponent {

	ArrayList<BulletEntity> bulletList = null;
	ArrayList<BulletEntity> removeList = null;
	
	private long lastFire;
	private long fireDelay = 400;
	private float bulletScale=1.0f;
	private int maxProjectiles = 100;
	private Image bulletImage = null;
	
	Integer shootButton;
	
	private long testTimer;
	
	public ShootableTest(String id, Vector2f entitySize, Image image, Integer keybind) {
		super(id);
		this.bulletImage=image;
		this.shootButton=keybind;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException {
		
		//TODO: Projektilen roteras om man håller in knappen; bör fixas men är inget direkt problem.
		if (gc.getInput().isKeyDown(shootButton) && gc.getTime()-lastFire >= fireDelay) {
			
			//initiate test timer
			testTimer=gc.getTime();
			
			lastFire=gc.getTime();
			
			float xspawn = owner.getPosition().x + (float) ((float) (owner.getSize().x*owner.getScale()+bulletImage.getWidth()*bulletScale)*java.lang.Math.sin(java.lang.Math.toRadians(owner.getRotation())));
			float yspawn = owner.getPosition().y - (float) ((float) (owner.getSize().y*owner.getScale()+bulletImage.getHeight()*bulletScale)*java.lang.Math.cos(java.lang.Math.toRadians(owner.getRotation())));
			
			BulletEntity tmp = null;
			Vector2f bulletspawn = new Vector2f(xspawn,yspawn);	
			
			if (bulletList==null) {
				bulletList=new ArrayList<BulletEntity>();
				removeList=new ArrayList<BulletEntity>();
				bulletList.add(tmp=new BulletEntity("bullet", bulletImage,bulletScale));
				tmp.setPosition(bulletspawn);
				tmp.setRotation(owner.getRotation());
				tmp = null;
			} else if (bulletList.size()<=maxProjectiles){
				bulletList.add(tmp=new BulletEntity("bullet", bulletImage,bulletScale));
				tmp.setPosition(bulletspawn);
				tmp.setRotation(owner.getRotation());
				tmp = null;
			}
		//testresult
			System.out.println("Testresult bulletTimer: " + (testTimer-gc.getTime()) + "ms between buttonpress and spawn.");
		}
		
		if (bulletList!=null) {
			for (BulletEntity bullet : bulletList) {
				if ( bullet.getPosition().x > gc.getWidth() || bullet.getPosition().x<0) {
					removeList.add(bullet);	
					System.out.println("hej1");
				}
				if ( bullet.getPosition().y > gc.getHeight()+40 || bullet.getPosition().y<-40) {
					removeList.add(bullet);
					System.out.println("hej1");
				}

			}

			if (removeList!=null) {	
				for (BulletEntity remobj : removeList) {
					bulletList.remove(remobj);
					System.out.println("hej2");
				}
				removeList.clear();
			}
		}
		if (bulletList!= null) {	
			for (BulletEntity bullet : bulletList) {
				bullet.update(gc, sb, delta);
			//	bullet.render(gc, sb, gc.getGraphics());
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