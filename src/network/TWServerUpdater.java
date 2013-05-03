package network;

public class TWServerUpdater implements Runnable {

// TODO: implement this: http://www.koonsolo.com/news/dewitters-gameloop/
//	long oldTime;
//	long newTime;
//	long delta;
//	int fps = 25;
//	int skipTicks = 1000 / fps;
	boolean isRunning = true;
	TWGameServer server;
	
	public TWServerUpdater( TWGameServer server ){
		this.server = server;
	}

	public void run() {
		while( isRunning ){
			server.updateEntities( 0.5f );
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
