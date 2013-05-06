package network;

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
			bulletImage = new Image("data/bullet.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void updateOffset(){
		Vector2f position = gameClient.getPlayerEntityPosition();
		if( position.x > 400 ){
			offset.x = position.x - 400f;
			if ( position.x > map.getWidth() * map.getTileWidth() - 400 ){
				offset.x = map.getWidth() * map.getTileWidth() - 800;
			}
		}
		if( position.y > 300  ){
			offset.y = position.y - 300f;
			if ( position.y > map.getHeight() * map.getTileHeight() - 300 ){
				offset.y = map.getHeight() * map.getTileHeight() - 600;
			}
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
}
