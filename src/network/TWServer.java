package network;
import game.TWWorld;
import java.io.IOException;

import network.TWNetwork.PlayerMovement;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;

public class TWServer {
	Server server;
	Kryo kryo;
	TWWorld world;

	public TWServer(){
		start();
		
		//Update the world
		//Push the world to clients

	}
	
	private void start(){
		/**
		 * Game
		 */
		world = new TWWorld();
		
		/**
		 * Network
		 */
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
		});
	}
	
	protected void handleMessage(Object object) {
		
		if (object instanceof String) {
			processMessage( (String) object );
		}
		
		else if ( object instanceof PlayerMovement ) {
			world.setPlayerMovement( (PlayerMovement) object );
		}
		
		else if ( object instanceof PlayerShoots ) {
			world.setPlayerShoots( (PlayerShoots) object );
		}
		
		else if ( object instanceof TWWorld ) {
			world.setPlayerShoots( (TWWorld) object );
		}
		
	}
	
	private final void processMessage( String message ){
		System.out.println("Server got: "+message);
	}

}
