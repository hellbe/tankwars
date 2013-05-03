package network;
import java.io.IOException;

import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;


public class TWNetworkClient {
	private Client client;
	private TWGameClient gameClient;
	private Kryo kryo;
	public int id;

	public TWNetworkClient( final TWGameClient gameClient ) throws SlickException {
		this.gameClient = gameClient;
		client = new Client();
		TWNetwork.register( client );

		client.addListener( new Listener() {

			public void connected(Connection connection) {
				id = connection.getID();
				gameClient.playerStatus.id = id;
			}

			public void received(Connection connection, Object object) {
				
				if ( object instanceof TWEntityContainer ){
					gameClient.entities = (TWEntityContainer) object;
				} 
				else if ( object instanceof TWMap ){
					TWMap mapInfo = (TWMap) object;
					gameClient.mapInfo = mapInfo;
				}
			}

			public void disconnected(Connection connection) { }

		});

		client.start();
		try {
			client.connect(5000, "127.0.0.1", 55555);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void send( Object data ){
		client.sendTCP( data );
	}

}
