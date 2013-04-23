package network;

public class TWServerUpdater implements Runnable {
	
	float oldTime = System.nanoTime();
	float newTime;
	float delta;
	boolean isRunning = true;
	TWServerWorld world;
	
	public TWServerUpdater( TWServerWorld world ){
		this.world = world;
	}
	
	public void run() {
		while( isRunning ){
			newTime = System.nanoTime();
			delta = newTime - oldTime;
			oldTime = newTime;
			world.update( delta );
		}
	}

}
