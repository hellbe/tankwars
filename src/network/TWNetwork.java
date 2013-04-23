package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class TWNetwork {

	static public void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(String.class);
		kryo.register(PlayerStatus.class);
	}
	/**
	 * All network messages
	 */
	static public class PlayerStatus {
		int id;			//The player's client connection id
		int move = 0; 			// 1 means forward, -1 backwards and 0 still
		int turn = 0; 			// 1 means turn right, -1 left and 0 go straight
		boolean shoot = false; 	//if the player wants to shoot
		
		public PlayerStatus( int id ){
			this.id = id;
		}
		
	}
	
	static public class WorldData {
		
	}
	
	static public class WorldGameData {
		
	}

}
