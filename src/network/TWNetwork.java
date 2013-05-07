package network;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class TWNetwork {

	static public void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(String.class);
		kryo.register(TWPlayerStatus.class);
		kryo.register(TWGameEntity.class);
		kryo.register(Vector2f.class);
		kryo.register(TWPlayer.class);
		kryo.register(TWEntityContainer.class);
		kryo.register(CopyOnWriteArrayList.class);
		kryo.register(TWMap.class);
		kryo.register(TWBullet.class);
	}
	/**
	 * All network messages
	 */
	static public class TWPlayerStatus {
		boolean left = false;
		boolean right = false;
		boolean up = false;
		boolean down = false;
		
		int move = 0; 			// 1 means forward, -1 backwards and 0 still
		int turn = 0; 			// 1 means turn right, -1 left and 0 go straight
		boolean shoot = false; 	//if the player wants to shoot
		boolean change = false;
		
		public TWPlayerStatus(){ }
		
	}
	
	@SuppressWarnings("serial")
	static public class TWEntityContainer extends CopyOnWriteArrayList<TWGameEntity>{
		public TWEntityContainer(){
			super();
		}
		
		public TWPlayer getPlayer ( Integer id ){
			for ( TWPlayer player : this.getPlayers() ){
				if ( player.id == id ){
					return player;
				}
			}
			return null;
		}
		
		public void removePlayer ( Integer id ){
			int i = -1;
			for ( TWPlayer player : this.getPlayers() ){
				if ( player.id == id ){
					this.remove(player);
				}
			}
			
		}
		
		public ArrayList<TWPlayer> getPlayers() {
			ArrayList<TWPlayer> toReturn = new ArrayList<TWPlayer>();
			for ( TWGameEntity entity : this ){
				if ( entity instanceof TWPlayer ){
					toReturn.add ( (TWPlayer) entity );
				}
			}
			return toReturn;
		}
		
		public ArrayList<TWBullet> getBullets() {
			ArrayList<TWBullet> toReturn = new ArrayList<TWBullet>();
			for ( TWGameEntity entity : this ){
				if ( entity instanceof TWBullet ){
					toReturn.add ( (TWBullet) entity );
				}
			}
			return toReturn;
		}
	}
	
	static public class TWMap{
		String path;
		String folder;
		
		public TWMap( String path, String folder ){
			this.path = path;
			this.folder = folder;
		}
		
		public TWMap(){}
	}

}
