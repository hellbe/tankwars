package gameplay;

import java.io.IOException;
import java.util.ArrayList;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;

public class TWServer {
	Server server;
	ArrayList<Integer> clients = new ArrayList<Integer>();
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
		kryo.register( World.class );
		server.start();
		
		try {
			server.bind(55555, 55556);
		} catch (IOException e) {
			e.printStackTrace();
		}

		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof World) {
					processWorld( (World) object );
				}
			}

			public void connected (Connection connection) {
				int id = connection.getID();
				clients.add(id);
			}
		});
	}

	private final void processWorld( World world ){

	}

}
