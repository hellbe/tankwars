package client;
 
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import component.Collidable;
import component.ImageRenderComponent;
import entity.BulletEntity;
import entity.GameEntity;
import entity.TankEntity;
import gameplay.World;


public class PlayState extends BasicGameState {
 
    int stateID;
    World world;
    Client client;
    Kryo kryo;
 
    TankEntity player1 = null;
    GameEntity land = null;
    GameEntity collisionObject = null;
    BulletEntity testBullet = null;
    
    public PlayState( int stateID ) {
       this.stateID = stateID;
    }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	//Connect to the server
    	connect();
    	
    	//init background
    	land = new GameEntity("land");
        land.AddComponent( new ImageRenderComponent("LandRender", new Image("/data/land.jpg")) );
        
        //init player
        player1 = new TankEntity("player1");
        player1.setPosition(new Vector2f(100, 100));
        
        //init collidable object
        collisionObject = new GameEntity("object");
        collisionObject.AddComponent( new ImageRenderComponent("objectrender", new Image("/data/battletank.png")));
        collisionObject.AddComponent(new Collidable("collidable", collisionObject, new Vector2f(128,128)));
        collisionObject.setPosition(new Vector2f(500,300));

        //testbullet
        testBullet = new BulletEntity("testbullet", new Image("data/bullet.png"), 0.4f);
        testBullet.setPosition(new Vector2f(400,300));
        
    }
 
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    	land.render(gc, null, g);
    	player1.render(gc, null, g);
    	collisionObject.render(gc, null, g);
    	testBullet.render(gc, null, g);
    	
    	//Render everything in the world
    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException     {
    	land.update(gc, null, delta);
    	player1.update(gc, null, delta);
    	collisionObject.update(gc, null, delta);
    	testBullet.update(gc, null, delta);
    	
    	//Update the world from the server
    	
    	//Send input to the server
    	
    }

	@Override
	public int getID() {
		return stateID;
	}
	
	private void connect(){
		client = new Client();
		kryo = client.getKryo();
		kryo.register( World.class );
		client.start();
		try {
			client.connect(5000, "127.0.0.1", 55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		addKeyListener(new KeyAdapter() {
//			public void keyPressed (KeyEvent e) {
//				sendKey(e.getKeyCode(), true);
//			}
//
//			public void keyReleased (KeyEvent e) {
//				sendKey(e.getKeyCode(), false);
//			}
//
//			private void sendKey (int keyCode, boolean pressed) {
//				switch (keyCode) {
//				case KeyEvent.VK_LEFT:
//					break;
//				case KeyEvent.VK_RIGHT:
//					break;
//				}
//				client.sendTCP(message);
//			}
//		});
	}
 
}