package se.kvirkel.tankwars.game;

/**
 * A simple runnable class updating the server
 * @author Ludwig, Peter, Simon
 *
 */
public class TWServerUpdater implements Runnable {

//  TODO: implement this: http://www.koonsolo.com/news/dewitters-gameloop/
//	long oldTime;
//	long newTime;
//	long delta;
//	int fps = 25;
//	int skipTicks = 1000 / fps;
	/**
	 * true if updater is running
	 */
	boolean isRunning = true;
	/**
	 * the server to update
	 */
	TWGameServer server;
	
	/**
	 * ServerUpdater constructor
	 * @param server the server to update
	 */
	public TWServerUpdater( TWGameServer server ){
		this.server = server;
	}

	/**
	 * runs the updater, calls the update in the server every 40ms
	 */
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
