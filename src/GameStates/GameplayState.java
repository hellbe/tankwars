package GameStates;
 
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Components.Collidable;
import Components.Basic.ImageRenderComponent;
import Entities.BulletEntity;
import Entities.Entity;
import Entities.TankEntity;
 
public class GameplayState extends BasicGameState {
 
    int stateID = -1;
    
 
    TankEntity player1 = null;
    Entity land = null;
    
    Entity collisionObject = null;
    BulletEntity testBullet = null;


    public GameplayState( int stateID ) {
       this.stateID = stateID;
    }
 
    @Override
    public int getID() {
        return stateID;
    }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	
    	//init background
    	land = new Entity("land");
        land.AddComponent( new ImageRenderComponent("LandRender", new Image("/data/land.jpg")) );
        
        //init player
        player1 = new TankEntity("player1");
        player1.setPosition(new Vector2f(100, 100));
        
        //init collidable object
        collisionObject = new Entity("object");
        collisionObject.AddComponent( new ImageRenderComponent("objectrender", new Image("/data/battletank.png")));
        collisionObject.AddComponent(new Collidable("collidable", collisionObject, new Vector2f(128,128)));
        collisionObject.setPosition(new Vector2f(500,300));

        //testbullet
        testBullet = new BulletEntity("testbullet", new Image("data/bullet.png"), 0.4f);
        testBullet.setPosition(new Vector2f(400,300));
        
    }
 
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException     {
    	land.render(gc, null, g);
    	player1.render(gc, null, g);
    	collisionObject.render(gc, null, g);
    	testBullet.render(gc, null, g);

    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException     {
    	land.update(gc, null, delta);
    	player1.update(gc, null, delta);
    	collisionObject.update(gc, null, delta);
    	testBullet.update(gc, null, delta);
    }
 
}