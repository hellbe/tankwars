package se.kvirkel.tankwars.network;

import java.io.IOException;
import java.net.InetAddress;
import org.newdawn.slick.SlickException;
import se.kvirkel.tankwars.game.TWGameClient;
import se.kvirkel.tankwars.network.TWNetwork.TWEntityContainer;
import se.kvirkel.tankwars.network.TWNetwork.TWMap;
import se.kvirkel.tankwars.network.TWNetwork.TWMessageContainer;
import com.esotericsoftware.kryonet.*;

/**
 * The clientside network handler: sends messages to the server and processes messages
 * when received.
 * @author Ludwig, Peter, Simon
 *
 */
public class TWNetworkClient {
	/**
	 * the client used
	 */
	private Client client;

	/**
	 * the GameClient to update
	 */
	@SuppressWarnings("unused")				//It is used but not for any function calls, see below
	private TWGameClient gameClient;

	/**
	 * TWClient id
	 */
	public Integer id;

	/**
	 * TWNetworkClient constructor
	 * @param gameClient the gameClient to handle
	 * @throws SlickException
	 */
	public TWNetworkClient( final TWGameClient gameClient ) throws SlickException {

		//initiate network
		this.gameClient = gameClient;
		client = new Client();
		TWNetwork.register( client );
		//add a network listener
		client.addListener( new Listener() {
			/**
			 * method invoked when connected to the network
			 */
			public void connected(Connection connection) {
				id = connection.getID();
			}
			/**
			 * method invoked when received an object, synchronizes information
			 */
			public void received(Connection connection, Object object) {
				if (object instanceof TWEntityContainer) {
					gameClient.entities = (TWEntityContainer) object;
				} else if (object instanceof TWMap) {
					gameClient.mapInfo = (TWMap) object;
				} else if (object instanceof TWMessageContainer) {
					gameClient.messages = (TWMessageContainer) object;
				}
			}

			/**
			 * method invoked when disconnected from the server
			 */
			public void disconnected(Connection connection) { 
				gameClient.gameEnded();
			}

		});
	}

	/**
	 * method for sending objects
	 * @param data the object to send
	 */
	public void send( Object data ){
		client.sendTCP( data );
	}

	/**
	 * connect to a server automatically on the local area network on a preset port
	 * @param localServer
	 */
	public boolean connect( boolean localServer ){
		String ip = "127.0.0.1";
		client.start();
		if ( ! localServer ){
			InetAddress address = client.discoverHost(55556,5000);
			if ( address != null ){
				ip = address.getHostName();
			} else {
				return false;
			}
		}
		try {
			client.connect(5000, ip, 55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * disconnect from the server.
	 */
	public void disconnect() {
		client.stop();
	}

}
