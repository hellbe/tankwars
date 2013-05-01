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

	public TWNetworkClient( TWGameClient gameClient ) throws SlickException {
		this.gameClient = gameClient;
		client = new Client();
		TWNetwork.register( client );

		client.addListener( new Listener() {

			public void connected(Connection connection) {
				handleConnect(connection);
			}

			public void received(Connection connection, Object object) {
				handleMessage(connection.getID(), object);
			}

			public void disconnected(Connection connection) {
				handleDisonnect(connection);
			}

		});

		client.start();
		try {
			client.connect(5000, "127.0.0.1", 55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void handleMessage(int id2, Object object) {
		if ( object instanceof TWEntityContainer ){
			gameClient.entities = (TWEntityContainer) object;
		} 
		else if ( object instanceof TWMap ){
			TWMap mapInfo = (TWMap) object;
			try {
				gameClient.map = new TiledMap( mapInfo.path, mapInfo.folder );
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}

	}

	private void handleConnect(Connection connection) {
		id = connection.getID();
		System.out.println("Client: connected to server with connection id: "+id);
	}

	protected void handleDisonnect(Connection connection) {
		System.out.println("Client "+id+" disconnected");
	}

	public void shutdown() {
		client.stop();
		client.close();
	}

	public void send( Object data ){
		client.sendTCP( data );
	}

}
