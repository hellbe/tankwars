package network;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;

public class TWClient extends JFrame {

	public static void main(String[] args) {
		new TWClient();
	}

	public TWClient(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(200, 200));
        pack();
        setVisible(true);
        
		final Client client = new Client();
		Kryo kryo = client.getKryo();
		kryo.register( TWMessage.class );

		client.start();
		try {
			client.connect(5000, "127.0.0.1", 55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}

		addKeyListener(new KeyAdapter() {
			public void keyPressed (KeyEvent e) {
				sendKey(e.getKeyCode(), true);
			}

			public void keyReleased (KeyEvent e) {
				sendKey(e.getKeyCode(), false);
			}

			private void sendKey (int keyCode, boolean pressed) {
				TWMessage message = new TWMessage();
				if(pressed){
					message.text = "Klienten tryckte in ";
				} else {
					message.text = "Klienten släppte ";
				}
				switch (keyCode) {
				case KeyEvent.VK_LEFT:
					message.text += "vänsterpil!";
					break;
				case KeyEvent.VK_RIGHT:
					message.text += "högerpil!";
					break;
				}
				client.sendTCP(message);
				
			}
		});

	}

}
