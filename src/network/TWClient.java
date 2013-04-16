package network;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;

import game.TWWorld;

public class TWClient {
	private Client client;
	private Kryo kryo;
	private TWWorld world;
	public int id;

	public TWClient( )  {
		this.world = new TWWorld();
		client = new Client();
		kryo = client.getKryo();
		
		kryo.register( String.class );
		client.start();
		try {
			client.connect(5000, "127.0.0.1", 55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		client.sendTCP( new String("Test") );


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

	}

	protected void handleMessage(int id2, Object object) {
		
	}
	
	private void handleConnect(Connection connection) {
		
	}
	
	protected void handleDisonnect(Connection connection) {
		
	}

	public void shutdown() {
		client.stop();
		client.close();
	}

}
