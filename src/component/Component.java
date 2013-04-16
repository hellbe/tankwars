package component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import entity.GameEntity;

public abstract class Component {
 
    protected String id;
    protected GameEntity owner;
 
    public String getId() {
        return id;
    }
 
    public void setOwnerEntity(GameEntity owner) {
    	this.owner = owner;
    }
 
    public abstract void update(GameContainer gc, StateBasedGame sb, int delta) throws SlickException;
}