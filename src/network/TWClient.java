package network;
import java.io.IOException;

import network.TWNetwork.PlayerStatus;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;


public class TWClient {
	private Client client;
	private Kryo kryo;
	public int id;

	public TWClient() throws SlickException  {
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

	public TWEntities getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	public TiledMap getMap() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void send( Object data ){
		client.sendTCP( data );
	}

}
