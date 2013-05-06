package network;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class TWBullet extends TWGameEntity {

	int playerId;
	
	public TWBullet(){
		super();
	}
	
	public TWBullet( int playerId, Vector2f position, Vector2f direction ){
		super();
		this.speed = 40;
		this.playerId = playerId;
		this.position = position;
		this.direction = direction;
	}
	
	public void update(){
		
	}

	
}
