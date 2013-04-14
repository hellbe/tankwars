package Components.Basic;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import Entities.Entity;
 
public abstract class Component {
 
    protected String id;
    protected Entity owner;
 
    public String getId()
    {
        return id;
    }
 
    public void setOwnerEntity(Entity owner)
    {
    	this.owner = owner;
    }
 
    public abstract void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException;
}