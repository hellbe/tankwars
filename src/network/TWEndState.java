package network;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TWEndState extends BasicGameState  {

	int stateID = -1;

	Image background = null;
	Image mainMenuOption = null;
	Image exitOption = null;

	private static int menuX = 200;
	private static int menuY = 250;

	float mainMenuScale = 1;
	float exitScale = 1;
	float scaleStep = 0.0001f;

	public TWEndState( int stateID ){
		this.stateID = stateID;
	}

	@Override
	public int getID() {
		return stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image("data/tankwars-end-bg.jpg");

		// Load the menu images
		Image menuOptions = new Image("data/tankwars-endmenuoptions.png");
		mainMenuOption = menuOptions.getSubImage(0, 0, 377, 71);
		exitOption = menuOptions.getSubImage(0, 71, 377, 71);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// render the background
		background.draw(0, 0);

		// Draw menu
		mainMenuOption.draw(menuX, menuY, mainMenuScale);
		exitOption.draw(menuX, menuY+80, exitScale);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();

		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		boolean mainMenuGame = false;
		boolean insideExit = false;

		if ( ( mouseX >= menuX && mouseX <= menuX + mainMenuOption.getWidth()) && ( mouseY >= menuY && mouseY <= menuY + mainMenuOption.getHeight()) ) {
			mainMenuGame = true;
		} else if ( ( mouseX >= menuX && mouseX <= menuX+ exitOption.getWidth()) && ( mouseY >= menuY+80 && mouseY <= menuY+80 + exitOption.getHeight()) ) {
			insideExit = true;
		}

		if(mainMenuGame){
			if(mainMenuScale < 1.05f) {
				mainMenuScale += scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
				//Tillbaka till huvudmenyn
				//sbg.enterState(TWGame.ENDGAMESTATE);
			}
		} else {
			if(mainMenuScale > 1.0f) {
				mainMenuScale -= scaleStep * delta;
			}
		}
		
		if(insideExit) {
			if(exitScale < 1.05f) {
				exitScale +=  scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
				gc.exit();
			}
		} else{
			if(exitScale > 1.0f) {
				exitScale -= scaleStep * delta;
			}
		}
		
	}

}
