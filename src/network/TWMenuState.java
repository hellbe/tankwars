package network;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class TWMenuState extends BasicGameState {

	int stateID = -1;

	Image background = null;
	Image hostGameOption = null;
	Image joinGameOption = null;
	Image exitOption = null;

	private static int menuX = 250;
	private static int menuY = 350;

	float hostGameScale = 1;
	float joinGameScale = 1;
	float exitScale = 1;
	float scaleStep = 0.0001f;

	public TWMenuState( int stateID )	{
		this.stateID = stateID;
	}
	
	Music menuMusic;

	@Override
	public int getID() {
		return stateID;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		hostGameScale = 1;
		joinGameScale = 1;
		exitScale = 1;
	}
	

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image("data/tankwars-bg.jpg");
	    
		// Play music
		menuMusic = new Music("data/gamemusic.wav");
		menuMusic.loop();

		// Load the menu images
		Image menuOptions = new Image("data/tankwars-menuoptions.png");
		hostGameOption = menuOptions.getSubImage(0, 0, 377, 71);
		joinGameOption = menuOptions.getSubImage(0, 71, 377, 71);
		exitOption = menuOptions.getSubImage(0, 142, 377, 71);

	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// render the background
		background.draw(0, 0);

		// Draw menu
		hostGameOption.draw(menuX, menuY, hostGameScale);
		joinGameOption.draw(menuX, menuY+80, joinGameScale);
		exitOption.draw(menuX, menuY+160, exitScale);
		
		//Draw log if possible
		if (TWGame.getGAMELOG() != null) {
			g.setColor( new Color( 0, 0, 0, 0.3f) );
			g.fillRect(215, 160, 460, 100);
			g.setColor(Color.white);
			g.drawString(TWGame.getGAMELOG(), 218, 160);
		}
	}


	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();

		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();

		boolean insideHostGame = false;
		boolean insideJoinGame = false;
		boolean insideExit = false;

		if ( ( mouseX >= menuX && mouseX <= menuX + hostGameOption.getWidth()) && ( mouseY >= menuY && mouseY <= menuY + hostGameOption.getHeight()) ) {
			insideHostGame = true;
		} 
		else if ( ( mouseX >= menuX && mouseX <= menuX+ joinGameOption.getWidth()) && ( mouseY >= menuY+80 && mouseY <= menuY+80 + joinGameOption.getHeight()) ) {
			insideJoinGame = true;
		} 
		else if ( ( mouseX >= menuX && mouseX <= menuX+ exitOption.getWidth()) && ( mouseY >= menuY+160 && mouseY <= menuY+160 + exitOption.getHeight()) ) {
			insideExit = true;
		}


		if(insideHostGame){
			if(hostGameScale < 1.05f) {
				hostGameScale += scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
				sbg.enterState( TWGame.MAPMENUSTATE, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.white, 1000) );
			}
		} 
		else {
			if(hostGameScale > 1.0f) {
				hostGameScale -= scaleStep * delta;
			}
		}
		
		if(insideJoinGame){
			if (joinGameScale < 1.05f) {
				joinGameScale += scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
				TWGame.host = false;
				sbg.enterState(TWGame.GAMESTATE, new FadeOutTransition(Color.black, 3000), new FadeInTransition(Color.white, 2000));
			}
		} 
		else {
			if(joinGameScale > 1.0f) {
				joinGameScale -= scaleStep * delta;
			}
		}

		if(insideExit) {
			if(exitScale < 1.05f) {
				exitScale +=  scaleStep * delta;
			}
			if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ) {
				gc.exit();
			}
		} 
		else{
			if(exitScale > 1.0f) {
				exitScale -= scaleStep * delta;
			}
		}
	}
}


