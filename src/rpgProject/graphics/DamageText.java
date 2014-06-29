package rpgProject.graphics;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import rpgProject.WindowMain;
import rpgProject.rooms.BattleRoom;

public class DamageText implements Drawable {
	private Color color;
	private int decay;
	private Text text;
	public DamageText(String t, Color c, int d, Vector2f p){
		color = c;
		decay = 255/d;
		text = new Text(t,WindowMain.defont);
		text.setColor(c);
		text.setPosition(p);
	}
	public void update(){
		if(color.a < 0)
			BattleRoom.textRemove.add(this);
		color = new Color(color.r,color.g,color.b,color.a - decay);
		text.setColor(color);
		text.setPosition(new Vector2f(text.getPosition().x,text.getPosition().y-3));
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		text.draw(target, states);
	}
}
