package network;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;

public class TWServer {
	Server server;
	Kryo kryo;
	
	public static void main(String[] args) {
		new TWServer();
	}

	public TWServer(){
		start();
		
		while(true){
			//Update the world
			//Push the world to clients
		}

	}
	
	private void start(){
		server = new Server();
		Kryo kryo = server.getKryo();
		kryo.register( TWMessage.class );
		
		server.start();
		try {
			server.bind(55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof TWMessage) {
					processMessage( (TWMessage) object );
				}
			}
		});
	}
	
	private final void processMessage( TWMessage message ){
		System.out.println(message.text);
	}

}
