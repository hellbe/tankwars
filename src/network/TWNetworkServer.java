package network;

import java.io.IOException;
import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWPlayerStatus;
import org.newdawn.slick.SlickException;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class TWNetworkServer {
	private Server server;
	private TWGameServer gameServer;

	public TWNetworkServer( final TWGameServer gameServer ) throws SlickException {
		this.gameServer = gameServer;
		server = new Server();
		TWNetwork.register( server );
		server.start();

		try {
			server.bind(55555,55556);
		} catch (IOException e) {
			e.printStackTrace();
		}

		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				handleReceived( connection, object );
			}

			public void connected(Connection connection){
				gameServer.entities.add( new TWPlayer(connection.getID()) );
				server.sendToTCP( connection.getID(),  gameServer.mapInfo );
			}
			
			public void disconnected(Connection connection){
				gameServer.entities.removePlayer( connection.getID() );
			}

		});

	}

	private void handleReceived(Connection connection, Object object) {

		if (object instanceof String) {
			message( (String) object );
		}

		else if ( object instanceof TWPlayerStatus ) {
			gameServer.updatePlayerStatus( connection.getID(), (TWPlayerStatus) object );
		}

	}

	private final void message( String message ){
		System.out.println("Server got: "+message);
	}

	public void updateClients( TWEntityContainer entities) {
		server.sendToAllTCP( entities );
	}
	
	public void stop(){
		server.stop();
	}
}
