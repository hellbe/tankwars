package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class TWNetwork {

	static public void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(String.class);
		kryo.register(PlayerMovement.class);
	}
	/**
	 * All network messages
	 */
	static public class PlayerMovement {
		int playerId;
		float direction;
	}
	
	static public class PlayerShoots {
		int playerId;
		float direction;
	}
	
	static public class WorldData {
		
	}
	
	static public class WorldGameData {
		
	}

}
