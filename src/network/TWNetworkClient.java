package network;
import java.io.IOException;
import java.net.InetAddress;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import com.esotericsoftware.kryo.Kryo;
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

			public void disconnected(Connection connection) { }

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
				throw new RuntimeException("Could not discover any network server");
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
