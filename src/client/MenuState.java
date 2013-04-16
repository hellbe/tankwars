package client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
 
public class MenuState extends BasicGameState {
	  
    int stateID = -1;
  
    Image background = null;
    Image startGameOption = null;
    Image exitOption = null;
  
    private static int menuX = 200;
    private static int menuY = 300;
  
    float startGameScale = 1;
    float exitScale = 1;
  
    public MenuState( int stateID )
    {
       this.stateID = stateID;
    }
    
    @Override
    public int getID() {
        return stateID;
    }
  
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	background = new Image("data/tankwars-bg.jpg");
    	  
        // Load the menu images
        Image menuOptions = new Image("data/tankwars-menuoptions.png");
        startGameOption = menuOptions.getSubImage(0, 0, 377, 71);
        exitOption = menuOptions.getSubImage(0, 71, 377, 71);

    }
  
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    	// render the background
        background.draw(0, 0);
  
        // Draw menu
        startGameOption.draw(menuX, menuY, startGameScale);
        exitOption.draw(menuX, menuY+80, exitScale);
    }
    
    float scaleStep = 0.0001f;
  
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	Input input = gc.getInput();
    	  
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
  
        boolean insideStartGame = false;
        boolean insideExit = false;
  
        if( ( mouseX >= menuX && mouseX <= menuX + startGameOption.getWidth()) &&
            ( mouseY >= menuY && mouseY <= menuY + startGameOption.getHeight()) )
        {
            insideStartGame = true;
        }else if( ( mouseX >= menuX && mouseX <= menuX+ exitOption.getWidth()) &&
            ( mouseY >= menuY+80 && mouseY <= menuY+80 + exitOption.getHeight()) )
        {
            insideExit = true;
        }
  
        if(insideStartGame)
        {
            if(startGameScale < 1.05f)
                startGameScale += scaleStep * delta;
  
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
                sbg.enterState(client.TWClient.GAMEPLAYSTATE);
            }
        }else{
            if(startGameScale > 1.0f)
                startGameScale -= scaleStep * delta;
  
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) )
                gc.exit();
        }
  
        if(insideExit)
        {
            if(exitScale < 1.05f)
                exitScale +=  scaleStep * delta;
        }else{
            if(exitScale > 1.0f)
                exitScale -= scaleStep * delta;
        }
    }
  
}