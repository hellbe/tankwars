package network;

public class TWServerUpdater implements Runnable {
	
	float oldTime = System.nanoTime();
	float newTime;
	float delta;
	boolean isRunning = true;
	TWGameServer server;
	
	public TWServerUpdater( TWGameServer server ){
		this.server = server;
	}
	
	public void run() {
		while( isRunning ){
			newTime = System.nanoTime();
			delta = newTime - oldTime;
			oldTime = newTime;
			server.update( delta );
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			server.render();
		}
	}

}
