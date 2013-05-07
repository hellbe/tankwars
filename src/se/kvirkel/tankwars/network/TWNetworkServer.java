package se.kvirkel.tankwars.network;

import java.io.IOException;
import org.newdawn.slick.SlickException;

import se.kvirkel.tankwars.game.TWGameServer;
import se.kvirkel.tankwars.network.TWNetwork.TWEntityContainer;
import se.kvirkel.tankwars.network.TWNetwork.TWMessageContainer;
import se.kvirkel.tankwars.network.TWNetwork.TWPlayerStatus;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * the network server handling the connection between gameserver and network client.
 * @author Ludwig, Peter, Simon
 *
 */
public class TWNetworkServer {
	
	/**
	 * this server
	 */
	private Server server;
	
	/**
	 * the gameserver
	 */
	private TWGameServer gameServer;

	/**
	 * NetworkServer constructor, registers the server to the network, binds a port to the network and adds a listener
	 * @param gameServer
	 * @throws SlickException
	 */
	public TWNetworkServer( final TWGameServer gameServer ) throws SlickException {
		
		//initiate the server
		this.gameServer = gameServer;
		server = new Server();
		TWNetwork.register( server );
		server.start();

		//bind port to server
		try {
			server.bind(55555,55556);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//add a listener to the server
		server.addListener(new Listener() {
			/**
			 * method invoked to update chatmessage and playerstatus when recieving any object of those types
			 */
			public void received (Connection connection, Object object) {

				//chat message
				if (object instanceof String) {
					gameServer.messages.add( "Player " + connection.getID() + ": " + (String) object );
					if ( gameServer.messages.size() == 8 ){
						gameServer.messages.remove(0);
					}
				}
				
				//player status
				else if ( object instanceof TWPlayerStatus ) {
					gameServer.updatePlayerStatus( connection.getID(), (TWPlayerStatus) object );
				}
			}

			/**
			 * method called upon when recieving connection. Adds player to the network.
			*/
			public void connected(Connection connection){
				
				gameServer.addPlayer( connection.getID() );
				server.sendToTCP( connection.getID(),  gameServer.mapInfo );
			}
			/**
			 * method called upon when player disconnects; removes the player from the game
			 */
			public void disconnected(Connection connection){
				gameServer.entities.removePlayer( connection.getID() );
			}

		});

	}

	/**
	 * method to update the game values on clients
	 * @param entities the entity container to sync with the clients
	 */
	public void updateClients( TWEntityContainer entities) {
		server.sendToAllTCP( entities );
	}

	/**
	 * method to update the chat values on clients
	 * @param messages list of chat messages
	 */
	public void updateClients(TWMessageContainer messages) {
		server.sendToAllTCP( messages );
	}
	
	/**
	 * stop this server from broadcasting
	 */
	public void stop(){
		server.stop();
	}

}
