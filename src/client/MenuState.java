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
    Image hostGameOption = null;
    Image joinGameOption = null;
    Image exitOption = null;
  
    private static int menuX = 200;
    private static int menuY = 200;
  
    float hostGameScale = 1;
    float joinGameScale = 1;
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
        hostGameOption = menuOptions.getSubImage(0, 0, 377, 71);
        joinGameOption = menuOptions.getSubImage(0, 70, 377, 60);
        exitOption = menuOptions.getSubImage(0, 130, 377, 71);

    }
  
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
    	// render the background
        background.draw(0, 0);
  
        // Draw menu
        hostGameOption.draw(menuX, menuY, hostGameScale);
        joinGameOption.draw(menuX, menuY+80, joinGameScale);
        exitOption.draw(menuX, menuY+160, exitScale);
    }
    
    float scaleStep = 0.0001f;
  
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	Input input = gc.getInput();
    	  
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
  
        boolean insideHostGame = false;
        boolean insideJoinGame = false;
        boolean insideExit = false;
  
        if( ( mouseX >= menuX && mouseX <= menuX + hostGameOption.getWidth()) &&
            ( mouseY >= menuY && mouseY <= menuY + hostGameOption.getHeight()) )
        {
            insideHostGame = true;
        }else if( ( mouseX >= menuX && mouseX <= menuX+ joinGameOption.getWidth()) &&
            ( mouseY >= menuY+80 && mouseY <= menuY+80 + joinGameOption.getHeight()) )
        {
        	insideJoinGame = true;
        }else if( ( mouseX >= menuX && mouseX <= menuX+ exitOption.getWidth()) &&
            ( mouseY >= menuY+160 && mouseY <= menuY+160 + exitOption.getHeight()) )
        {
            insideExit = true;
        }
  
        if(insideHostGame)
        {
            if(hostGameScale < 1.05f)
                hostGameScale += scaleStep * delta;
  
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
                sbg.enterState(client.TWClient.HOSTGAMEPLAYSTATE);	
            }
        } else{
            if(hostGameScale > 1.0f)
                hostGameScale -= scaleStep * delta;
  
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) )
                gc.exit();
        }
        
        if(insideJoinGame)
        {
            if(joinGameScale < 1.05f)
                joinGameScale += scaleStep * delta;
  
            if ( input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
                sbg.enterState(client.TWClient.JOINGAMEPLAYSTATE);	
            }
        } else{
            if(joinGameScale > 1.0f)
                joinGameScale -= scaleStep * delta;
  
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