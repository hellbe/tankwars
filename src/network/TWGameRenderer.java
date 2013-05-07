package network;

import java.util.ArrayList;
import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWMap;
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

/**
 * Client painter, draws everything on the client window
 * @author Ludwig, Peter, Simon
 *
 */
public class TWGameRenderer {
	/**
	 * game client
	 */
	TWGameClient gameClient;
	/**
	 * bullet image
	 */
	Image bulletImage;
	/**
	 * Tank image
	 */
	Image tankImage;
	/**
	 * player offset
	 */
	Vector2f offset = new Vector2f();
	/**
	 * the active map
	 */
	TiledMap map;
	/**
	 * chat field
	 */
	TextField messageField;
	/**
	 * background color (0.3 opacity)
	 */
	Color bgColor = new Color( 0f, 0f, 0f, 0.3f );
	/**
	 * transparent color
	 */
	Color transparent = new Color( 0f, 0f, 0f, 0f );
	/**
	 * gamewindow height
	 */
	int windowHeight;
	/**
	 * gamewindow width
	 */
	int windowWidth;
	/**
	 * map height in pixels, null if not set
	 */
	int mapHeight;
	/**
	 * map width in pixels, null if not set
	 */
	int mapWidth;
	/**
	 * map margin (in pixels)
	 */
	int margin = 5;
	/**
	 * text height (in pixels)
	 */
	int textHeight = 20;

	/**
	 * Game renderer constructor
	 * @param gameClient local gameClient
	 * @param gc gameContainer
	 */
	public TWGameRenderer( final TWGameClient gameClient, GameContainer gc ){
		this.gameClient = gameClient;
		this.windowHeight = gc.getHeight();
		this.windowWidth = gc.getWidth();
		try {
			tankImage = new Image("data/fastTank.png");
			bulletImage = new Image("data/bullet2.png");
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
		messageField = new TextField(gc, gc.getDefaultFont(), 2 * margin, windowHeight - textHeight - 2 * margin, 350, 20);
		messageField.setBorderColor( transparent ); 
		messageField.setBackgroundColor( transparent );
		messageField.addListener( new ComponentListener(){
			public void componentActivated( AbstractComponent source ){
				gameClient.sendMessage( messageField.getText() );
				messageField.setText("");
				messageField.setFocus(false);
			}
		});
	}

	/**
	 * updates the offset so that that the view follows the tank
	 */
	public void updateOffset(){
		Vector2f position = gameClient.entities.getPlayer( gameClient.getPlayerId() ).position;

		// Set x-axis offset
		if( position.x > windowWidth / 2 ){
			offset.x = position.x - windowWidth / 2;
			if ( position.x > mapWidth - windowWidth / 2 ){
				offset.x = mapWidth - windowWidth;
			}
		} 
		else {
			offset.x = 0;
		}

		// Set y-axis offset
		if( position.y > windowHeight / 2  ){
			offset.y = position.y - windowHeight / 2;
			if ( position.y > mapHeight - windowHeight / 2 ){
				offset.y = mapHeight - windowHeight;
			}
		}
		else {
			offset.y = 0;
		}
	}

	/**
	 * paint/render every entity on the map
	 * @param entities the entity container
	 * @throws SlickException
	 */
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

	/**
	 * load the map properties
	 * @param mapInfo
	 * @throws SlickException
	 */
	public void loadMap(TWMap mapInfo) throws SlickException {
		map = new TiledMap( mapInfo.path, mapInfo.folder );
		mapWidth = map.getWidth() * map.getTileWidth();
		mapHeight = map.getHeight() * map.getTileHeight();
	}
	
	/**
	 * render the map based on the current offset 
	 * @throws SlickException
	 */
	public void renderMap() throws SlickException {
		map.render( - (int) offset.x, - (int) offset.y );
	}
/**
 * draw the scoreboard
 * @param g
 */
	public void renderScore(Graphics g){
		ArrayList<TWPlayer> players = gameClient.entities.getPlayers();
		g.setColor( bgColor );
		g.fillRect( margin, margin, 150, 60 + textHeight * players.size() );
		g.setColor( Color.white );
		g.drawString("Score", 2 * margin, 30);
		g.drawLine( 2 * margin, 51, 150 - margin, 51);
		int y = 55;
		for ( TWPlayer player : players ){
			String string = "Player " + player.id + ": " + player.score;
			if ( player.id == gameClient.networkClient.id ){
				g.setColor( Color.red );
				g.drawString( string , 2 * margin, y );
				g.setColor( Color.white );
			} 
			else {
				g.drawString( string, 2 * margin, y );
			}
			y = y + textHeight;
		}
	}
/**
 * render the tank healthbars
 * @param players
 * @param g
 */
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
/**
 * render the chat
 * @param g
 * @param messages the messagecontainer of all the chatmessages
 */
	public void renderMessages( Graphics g, TWMessageContainer messages ) {
		int boxHeight = 2 * margin + textHeight + messages.size() * 20;
		g.setColor( bgColor );
		g.fillRect( margin, windowHeight - boxHeight - margin, 400, boxHeight );
		g.setColor( Color.white );
		int y = windowHeight - 2 * margin - textHeight * ( 1 + messages.size() ) ;
		for ( String message : messages ){
			g.drawString( message, 2 * margin, y );
			y = y + textHeight;
		}
	}
/**
 * render the messagefield
 * @param gc the gamecontainer
 * @param g the graphics
 * @param string 
 */
	public void renderMessageField( GameContainer gc, Graphics g, String string ) {
		messageField.render(gc,g);
		messageField.setFocus(true);
	}

}
