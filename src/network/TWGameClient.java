package network;

import java.util.ArrayList;


import network.TWNetwork.TWEntityContainer;
import network.TWNetwork.TWPlayerStatus;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import entity.GameEntity;

public class TWGameClient {

	public TWEntityContainer entities;
	public TiledMap map;
	public TWNetworkClient client;
	public TWPlayerStatus playerStatus;

	public TWGameClient() throws SlickException{
		this.client = new TWNetworkClient( this );
		this.playerStatus = new TWPlayerStatus( client.id );
	}

	public void changePlayerStatus(int key, boolean pressed ) {
		if ( pressed ){
			switch ( key ){
			case Input.KEY_LEFT:
				playerStatus.turn = -1;
				break;
			case Input.KEY_RIGHT:
				playerStatus.turn = 1;
				break;
			case Input.KEY_UP:
				playerStatus.move = 1;
				break;
			case Input.KEY_DOWN:
				playerStatus.move = -1;
				break;
			case Input.KEY_SPACE:
				playerStatus.shoot = true;
				break;
			}
		} else {
			switch ( key ){
			case Input.KEY_LEFT:
			case Input.KEY_RIGHT:
				playerStatus.turn = 0;
				break;
			case Input.KEY_UP:
			case Input.KEY_DOWN:
				playerStatus.move = 0;
				break;
			case Input.KEY_SPACE:
				playerStatus.shoot = false;
				break;
			}
		}
		playerStatus.change = true;
	}

	public void sendPlayerStatus() {
		if( playerStatus.change ){
			client.send( playerStatus );
			playerStatus.change = false;
		}
	}

	public void render() {
		if( map != null ){
			map.render(0, 0);
		}
	}

	public void update() { }


}

