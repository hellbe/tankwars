package network;
import java.io.IOException;
import java.net.InetAddress;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.esotericsoftware.kryonet.*;


public class TWNetworkClient {
	private Client client;
	private TWGameClient gameClient;
	public int id;

	public TWNetworkClient( final TWGameClient gameClient ) throws SlickException {
		this.gameClient = gameClient;
		client = new Client();
		TWNetwork.register( client );

		client.addListener( new Listener() {

			public void connected(Connection connection) {
				id = connection.getID();
			}
			
			public void received(Connection connection, Object object) {
				
				if ( object instanceof TWEntityContainer ){
					gameClient.entities = (TWEntityContainer) object;
				} 
				else if ( object instanceof TWMap ){
					gameClient.mapInfo = (TWMap) object;
				}
			}

			public void disconnected(Connection connection) { 
				TWGame.addtoGameLog("The host has disconnected.");
				
				for (TWPlayer player : gameClient.entities.getPlayers()) {
					if (player.score == 10) {
						TWGame.addtoGameLog("Player "+player.id+ " has won the game!");
					}
				}
				gameClient.game.enterState(TWGame.MAINMENUSTATE, new FadeOutTransition(Color.black, 3000), new FadeInTransition(Color.white, 1000));
			}

		});

	}

	public void send( Object data ){
		client.sendTCP( data );
	}
	
	public void connect( boolean localServer ){
		String ip = "127.0.0.1";
		client.start();
		if ( ! localServer ){
			InetAddress address = client.discoverHost(55556,5000);
			if ( address != null ){
				ip = address.getHostName();
			} else {
				TWGame.addtoGameLog("Could not detect any active network server!");
				gameClient.game.enterState(TWGame.MAINMENUSTATE);
				return;
			}
		}
		try {
			client.connect(5000, ip, 55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		client.stop();
	}

}
