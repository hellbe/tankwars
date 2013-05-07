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

public class TWGameRenderer {
	TWGameClient gameClient;
	Image bulletImage;
	Image tankImage;
	Vector2f offset = new Vector2f();
	TiledMap map;
	TextField messageField;
	Color bgColor = new Color( 0f, 0f, 0f, 0.3f );
	Color transparent = new Color( 0f, 0f, 0f, 0f );
	int windowHeight;
	int windowWidth;
	int mapHeight;
	int mapWidth;
	int margin = 5;
	int textHeight = 20;

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

	public void loadMap(TWMap mapInfo) throws SlickException {
		map = new TiledMap( mapInfo.path, mapInfo.folder );
		mapWidth = map.getWidth() * map.getTileWidth();
		mapHeight = map.getHeight() * map.getTileHeight();
	}
	
	public void renderMap() throws SlickException {
		map.render( - (int) offset.x, - (int) offset.y );
	}

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

	public void renderMessageField( GameContainer gc, Graphics g, String string ) {
		messageField.render(gc,g);
		messageField.setFocus(true);
	}

}
