package network;

import org.newdawn.slick.geom.Vector2f;

public class TWBullet extends TWGameEntity {

	int playerId;
	
	public TWBullet(){ }
	
	public TWBullet(int playerId, Vector2f position, Vector2f direction) {
		this.speed = 40;
		this.size = new Vector2f(25,25);
		this.playerId = playerId;
		this.position = position;
		this.direction = direction;
	}
	
}
