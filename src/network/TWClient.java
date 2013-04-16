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

import gameplay.World;

public class TWClient extends JFrame {
	private Client client;
	private World world;
	public int id;
	
	
	public static void main(String[] args) {
		new TWClient();
	}

	public TWClient( ){
		this.world = new World();
		client = new Client();
		client.start();
		

        client.addListener(new Listener() {
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
	
	protected void handleDisonnect(Connection connection) {
        world.onDisconnect();
	}

}
