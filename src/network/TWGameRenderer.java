package network;

import java.util.ArrayList;
import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMessageContainer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.tiled.TiledMap;

public class TWGameRenderer {
	TWGameClient gameClient;
	Image bulletImage;
	Image tankImage;
	Vector2f offset;
	TiledMap map;
	TextField messageField;

	public TWGameRenderer( final TWGameClient gameClient, GameContainer gc ){
		this.gameClient = gameClient;
		offset = new Vector2f();
		try {
			tankImage = new Image("data/fastTank.png");
			bulletImage = new Image("data/bullet2.png");
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
		messageField = new TextField(gc, gc.getDefaultFont(), 10, 570, 350, 25);
		messageField.setBorderColor( new Color( 0f, 0f, 0f, 0.0f ) ); 
		messageField.setBackgroundColor( new Color( 0f, 0f, 0f, 0.3f ));
		messageField.addListener( new ComponentListener(){
			public void componentActivated( AbstractComponent source ){
				gameClient.sendMessage( messageField.getText() );
				messageField.setText("");
				messageField.setFocus(false);
			}
		});
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

	public void renderEntities(TWEntityContainer entities ) throws SlickException {
		Image image;
		for ( TWGameEntity entity : entities ){
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

	public void renderHealthBars( ArrayList<TWPlayer> players, Graphics g ){
		for( TWPlayer player : players ){
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

	public void renderMessages( Graphics g, TWMessageContainer messages ) {
		g.setColor( new Color( 0f, 0f, 0f, 0.3f ));
		g.fillRect( 10, 400, 350, 165 );
		g.setColor( Color.white );
		int y = 405;
		for ( String message : messages ){
			g.drawString( message, 15, y );
			y = y + 20;
		}
	}

	public void renderMessageBox( GameContainer gc, Graphics g, String string ) {
		messageField.render(gc,g);
		messageField.setFocus(true);
//		g.setColor( new Color( 0f, 0f, 0f, 0.3f ));
//		g.fillRect( 10, 570, 350, 25 );
//		g.setColor( Color.white );
//		g.drawString(string, 15, 573 );
	}
}
