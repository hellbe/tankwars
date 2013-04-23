package component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
 
public class ImageRenderComponent extends RenderComponent {
 
	Image image;
    
	public ImageRenderComponent(String id, Image image)	{
		super(id);
		this.image = image;
		
	}
 
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		Vector2f pos = owner.getPosition();
		float scale = owner.getScale();
	//	debug
	//	gr.drawRect(pos.x, pos.y, image.getWidth()*scale, image.getHeight()*scale);
		image.draw(pos.x, pos.y, scale);
		
	}
 
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		image.rotate(owner.getRotation() - image.getRotation());
		
		if (owner.getSize()==null) {
			owner.setSize(new Vector2f(image.getWidth()*owner.getScale(),image.getHeight()*owner.getScale()));
		}
		
	}
 
}