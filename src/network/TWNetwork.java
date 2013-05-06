package network;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.Image;
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
	
	static public class TWEntityContainer extends CopyOnWriteArrayList<TWGameEntity>{
		public TWEntityContainer(){
			super();
		}
		
		public void addPlayer( Integer id ){
			add( new TWPlayer( id ));
		}
		
		public TWPlayer getPlayer ( Integer id ){
			for ( TWGameEntity entity : this ){
				if ( entity instanceof TWPlayer && ( (TWPlayer) entity).id == id ){
					return (TWPlayer) entity;
				}
			}
			return null;
		}
		
		public void removePlayer ( Integer id ){
			int toRemove = -1;
			for ( TWGameEntity entity : this ){
				if ( entity instanceof TWPlayer && ( (TWPlayer) entity).id == id ){
					toRemove = indexOf(entity);
					break;
				}
			}
			if ( toRemove != -1 ){
				remove( toRemove );
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
