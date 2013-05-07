package client;
 
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class TWClient extends StateBasedGame {
	
    public static final int MAINMENUSTATE = 0;
    public static final int HOSTGAMEPLAYSTATE = 1;
    public static final int JOINGAMEPLAYSTATE = 2;
    public static final int ENDGAMESTATE = 3;
//    public static final int MAPMENUSTATE = 4;
    
    public TWClient() {
        super("TankWars");
    }
 
    public static void main(String[] args) throws SlickException {
         AppGameContainer app = new AppGameContainer(new TWClient());
         app.setDisplayMode(800, 600, false);
         app.start();
    }
 
    public void initStatesList(GameContainer gameContainer) throws SlickException {
    	this.addState(new MapMenuState(1));
//    	this.addState(new MenuState(MAINMENUSTATE));
//    	this.addState(new PlayState(HOSTGAMEPLAYSTATE));
//    	this.addState(new PlayState(JOINGAMEPLAYSTATE));
//    	this.addState(new EndState(ENDGAMESTATE));
    }
}