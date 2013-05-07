package network;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;

public class TWGameRenderer {
	TWGameClient gameClient;
	Image bulletImage;
	Image tankImage;
	Vector2f offset;
	TiledMap map;

	public TWGameRenderer( TWGameClient gameClient ){
		this.gameClient = gameClient;
		offset = new Vector2f();
		try {
			tankImage = new Image("data/tank.png");
			bulletImage = new Image("data/bullet2.png");
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void updateOffset(){
		Vector2f position = gameClient.getPlayerEntityPosition();
		
		// Set x-axis offset
		if( position.x > 400 ){
			offset.x = position.x - 400f;
			if ( position.x > map.getWidth() * map.getTileWidth() - 400 ){
				offset.x = map.getWidth() * map.getTileWidth() - 800;
			}
		} 
		else {
			offset.x = 0;
		}
		
		// Set y-axis offset
		if( position.y > 300  ){
			offset.y = position.y - 300f;
			if ( position.y > map.getHeight() * map.getTileHeight() - 300 ){
				offset.y = map.getHeight() * map.getTileHeight() - 600;
			}
		}
		else {
			offset.y = 0;
		}
	}

	public void renderEntity(TWGameEntity entity ) throws SlickException {
		Image image;
		if ( entity instanceof TWPlayer ){
			image = tankImage;
		} 
		else if ( entity instanceof TWBullet ){
			image = bulletImage;
		}
		else {	// Just for testing
			image = bulletImage;
		}
		image.setRotation( (float) entity.direction.getTheta() );
		image.drawCentered( entity.position.x - (int) offset.x , entity.position.y - (int) offset.y );
	}

	public void renderMap() throws SlickException {
		if( map == null ){
			if ( gameClient.mapInfo != null ){
				map = new TiledMap( gameClient.mapInfo.path, gameClient.mapInfo.folder );
			}
		} 
		else {
			map.render( - (int) offset.x, - (int) offset.y);
		}
	}
	
	public void renderScore(Graphics g){
		ArrayList<TWPlayer> players = gameClient.entities.getPlayers();
		int y = 30;
		g.setColor( new Color( 0, 0, 0, 0.3f) );
		g.fillRect(5, 5, 200, 30 + 20 * players.size() );
		g.setColor( Color.white );
		for ( TWPlayer player : players ){
			String string = "Player "+player.id+"'s score: "+player.score;
			if ( player.id == gameClient.networkClient.id ){
				g.setColor( Color.red );
				g.drawString( string , 10, y );
				g.setColor( Color.white );
			} 
			else {
				g.drawString( string , 10, y );
			}
			y = y + 20;
		}
	}
	
	public void renderHealthBar( TWPlayer player, Graphics g ){
		if ( player.hp > 50 ){
			g.setColor( Color.green );
		}
		else if ( player.hp > 20 ){
			g.setColor( Color.yellow );
		}
		else if ( player.hp > 0 ){
			g.setColor( Color.red );
		}
		g.fillRect( player.position.x - offset.x - 25f , player.position.y - 45f - offset.y, player.hp / 2, 10 );
		g.setColor( Color.black );
		g.drawRect( player.position.x - offset.x - 25f , player.position.y - 45f - offset.y, 50, 10 );
	}
}
