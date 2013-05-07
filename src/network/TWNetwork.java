package network;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.geom.Vector2f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 * network class, registers and contains the classes we want to send over the network
 * @author Ludde
 *
 */
public class TWNetwork {

	/**
	 * register the classes we want to send over the network
	 * @param endPoint server or client (handled by cryonet)
	 */
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
		kryo.register(TWMessageContainer.class);
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
		boolean shoot = false; 	//true if the player wants to shoot
		boolean change = false;
		
		public TWPlayerStatus(){ }
		
	}
	
	@SuppressWarnings("serial")
	/**
	 * synchronized entitylist containing the entities and methods for returning different items in the list
	 * @author Ludde
	 *
	 */
	static public class TWEntityContainer extends CopyOnWriteArrayList<TWGameEntity>{
	
		/**
		 * container constructor
		 */
		public TWEntityContainer(){
			super();
		}
		
		/**
		 * method for returning a player from the list
		 * @param id the id of the desired player
		 * @return returns the desired player, null if player does not exist/method failed
		 */
		public TWPlayer getPlayer ( Integer id ){
			for ( TWPlayer player : this.getPlayers() ){
				if ( player.id == id ){
					return player;
				}
			}
			return null;
		}
		/**
		 * remove a player from the list (and consequently the game), does nothing if id is invalid
		 * @param id the player to remove
		 */
		public void removePlayer ( Integer id ){
			for ( TWPlayer player : this.getPlayers() ){
				if ( player.id == id ){
					this.remove(player);
				}
			}
			
		}
		/**
		 * get all the game players in an ArrayList, returns empty set if no players are in the game.
		 * @return all players
		 */
		public ArrayList<TWPlayer> getPlayers() {
			ArrayList<TWPlayer> toReturn = new ArrayList<TWPlayer>();
			for ( TWGameEntity entity : this ){
				if ( entity instanceof TWPlayer ){
					toReturn.add ( (TWPlayer) entity );
				}
			}
			return toReturn;
		}
		
		/**
		 * get all the bullets in the game, returns an empty set if no bullets are present
		 * @return arraylist of every bullet
		 */
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
	/**
	 * the game map to be played
	 * @author Ludde
	 *
	 */
	static public class TWMap{
		String path;
		String folder;
		
		/**
		 * map constructor, creates an empty map
		 */
		public TWMap(){}
		/**
		 * map constructor, defines the path and folder to the map
		 * @param path path to map
		 * @param folder map folder
		 */
		public TWMap( String path, String folder ){
			this.path = path;
			this.folder = folder;
		}
		
	}
	
	/**
	 * message container, used by the chat. Is a synhronized list of strings
	 * @author Ludde
	 *
	 */
	static public class TWMessageContainer extends CopyOnWriteArrayList<String>{
		
		/**
		 * message container constructor, creates an empty list of messages
		 */
		TWMessageContainer(){
			super();
		}
		
	}

}
