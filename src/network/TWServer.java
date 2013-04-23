package network;
import java.io.IOException;
import network.TWNetwork.PlayerStatus;
import org.newdawn.slick.SlickException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;

public class TWServer {
	Server server;
	Kryo kryo;
	TWServerWorld world;
	TWServerUpdater updater;
	Thread thread;

	public TWServer() throws SlickException{
		world = new TWServerWorld();
		updater = new TWServerUpdater( world );
		thread = new Thread( updater );
		thread.run();
		start();
	}
	
	private void start() throws SlickException{
		world = new TWServerWorld();
		server = new Server();
		TWNetwork.register( server );
		server.start();
		
		try {
			server.bind(55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				handleMessage(object);
			}
			
			public void connected(Connection connection){
				System.out.println( connection.getID() );
			}
			
		});
	}
	
	protected void handleMessage(Object object) {
		
		if (object instanceof String) {
			message( (String) object );
		}
		
		else if ( object instanceof PlayerStatus ) {
			world.updatePlayerStatus( (PlayerStatus) object );
		}
		
	}
	
	private final void message( String message ){
		System.out.println("Server got: "+message);
	}
	
	
	
}
