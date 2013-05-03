package network;

import network.TWNetwork.TWPlayerStatus;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TWPlayer extends TWGameEntity {

	int mayShootWhen;
	TWPlayerStatus playerStatus;
	
	public TWPlayer() {
		super();
	}
	
	public TWPlayer( TWPlayerStatus playerStatus ) {
		super();
		imagePath = "data/fastTank.png";
		this.playerStatus = playerStatus;
	}
	
	@Override
	public void update (){
		
	}
	
	@Override
	public void move( float delta ){
		if ( playerStatus.move == 1 || playerStatus.move == -1 || playerStatus.move == 0){
			speed = playerStatus.move * 30 * delta;
		}
		if ( playerStatus.turn == 1 || playerStatus.turn == -1 || playerStatus.turn == 0 ){
			direction.add( playerStatus.turn * delta * 10);
		}
		position = getFutureMove(delta);
	}
	
	
}
