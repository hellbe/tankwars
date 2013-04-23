package network;

import java.util.ArrayList;


import network.TWNetwork.PlayerStatus;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import entity.GameEntity;

public class TWClientWorld {

	public TWEntities entities;
	public TiledMap map;
	public TWClient client;
	public PlayerStatus playerStatus;

	public TWClientWorld( TWClient client ) throws SlickException{
		this.client = client;
		this.playerStatus = new PlayerStatus( client.id );
		this.map = client.getMap();
	}

	public void changePlayerStatus(int key, boolean pressed ) {
		switch ( key ){
		case Input.KEY_LEFT:

			break;
		case Input.KEY_RIGHT:

			break;
		case Input.KEY_UP:

			break;
		case Input.KEY_DOWN:

			break;
		case Input.KEY_SPACE:

			break;
		}

	}

	public void sendPlayerStatus() {
		// TODO Auto-generated method stub
	}

	public void render() {
		// TODO Auto-generated method stub

	}

	public void update() {
		entities = client.getEntities();
	}


}

