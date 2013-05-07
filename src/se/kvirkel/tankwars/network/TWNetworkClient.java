package se.kvirkel.tankwars.network;
import java.io.IOException;
import java.net.InetAddress;


import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import se.kvirkel.tankwars.game.TWGame;
import se.kvirkel.tankwars.game.TWGameClient;
import se.kvirkel.tankwars.game.TWPlayer;
import se.kvirkel.tankwars.network.TWNetwork.TWEntityContainer;
import se.kvirkel.tankwars.network.TWNetwork.TWMap;
import se.kvirkel.tankwars.network.TWNetwork.TWMessageContainer;
import se.kvirkel.tankwars.network.TWNetwork.TWPlayerStatus;

import com.esotericsoftware.kryonet.*;

/**
 * The clientside network handler; sends messages to the networkserver and the gameclient. Also recieves updates.
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
			 * method invoked when disconnected from the server, checks if someone has won the game and
			 * adds it to the GameLog if so. Redirects the Game back to the mainmenu
			 */
			public void disconnected(Connection connection) { 
				TWGame.gameLog.add("The host has disconnected.");
				for (TWPlayer player : gameClient.entities.getPlayers()) {
					if (player.score == 10) {
						TWGame.gameLog.add("Player " +player.id+ " has won the game!");
					}
				}
				gameClient.game.enterState(TWGame.MAINMENUSTATE, new FadeOutTransition(Color.black, 3000), new FadeInTransition(Color.white, 2000));
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
