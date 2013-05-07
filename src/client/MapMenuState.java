package client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MapMenuState extends BasicGameState  {

	int stateID = -1;

	Image background = null;
	Image map1option = null;
	Image map2option = null;
	Image map3option = null;
	Image map4option = null;

	private static int menuX = 200;
	private static int menuY = 150;

	float map1Scale = 1;
	float map2Scale = 1;
	float map3Scale = 1;
	float map4Scale = 1;
	float scaleStep = 0.0001f;

	public MapMenuState( int stateID ){
		this.stateID = stateID;
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image("data/tankwars-map-bg.jpg");

		// Load the menu images
		Image menuOptions = new Image("data/mapoption.png");
		map1option = menuOptions.getSubImage(0, 0, 156, 118);
		map2option = menuOptions.getSubImage(158, 0, 156, 118);
		map3option = menuOptions.getSubImage(318, 0, 156, 118);
		map4option = menuOptions.getSubImage(476, 0, 156, 118);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// render the background
		background.draw(0, 0);

		// Draw menu
		map1option.draw(menuX, menuY, map1Scale);
		map2option.draw(menuX+250, menuY, map2Scale);
		map3option.draw(menuX, menuY+180, map3Scale);
		map4option.draw(menuX+250, menuY+180, map4Scale);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();

		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		boolean insideMap1 = false;
		boolean insideMap2 = false;
		boolean insideMap3 = false;
		boolean insideMap4 = false;
		
		map1option.draw(menuX, menuY, map1Scale);
		map2option.draw(menuX+250, menuY, map2Scale);
		map3option.draw(menuX, menuY+180, map3Scale);
		map4option.draw(menuX+250, menuY+180, map4Scale);
		
		if ( ( mouseX >= menuX && mouseX <= menuX + map1option.getWidth()) && ( mouseY >= menuY && mouseY <= menuY + map1option.getHeight()) ) {
			insideMap1 = true;
		} else if ( ( mouseX >= menuX + 250 && mouseX <= menuX + 250 + map2option.getWidth()) && ( mouseY >= menuY && mouseY <= menuY + map2option.getHeight()) ) {
			insideMap2 = true;
		} else if ( ( mouseX >= menuX && mouseX <= menuX+ map3option.getWidth()) && ( mouseY >= menuY+ 180 && mouseY <= menuY+ 180 + map3option.getHeight()) ) {
			insideMap3 = true;
		} else if ( ( mouseX >= menuX + 250 && mouseX <= menuX + 250 + map4option.getWidth()) && ( mouseY >= menuY + 180 && mouseY <= menuY + 180 + map4option.getHeight()) ) {
			insideMap4 = true;
		}

		if(insideMap1){
			if(map1Scale < 1.05f) {
				map1Scale += scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
			}
		} else {
			if(map1Scale > 1.0f) {
				map1Scale -= scaleStep * delta;
			}
		}
		
		if(insideMap2){
			if(map2Scale < 1.05f) {
				map2Scale += scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
			}
		} else {
			if(map2Scale > 1.0f) {
				map2Scale -= scaleStep * delta;
			}
		}
		
		if(insideMap3){
			if(map3Scale < 1.05f) {
				map3Scale += scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
			}
		} else {
			if(map3Scale > 1.0f) {
				map3Scale -= scaleStep * delta;
			}
		}
		
		if(insideMap4){
			if(map4Scale < 1.05f) {
				map4Scale += scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
			}
		} else {
			if(map4Scale > 1.0f) {
				map4Scale -= scaleStep * delta;
			}
		}
		
	}

}
