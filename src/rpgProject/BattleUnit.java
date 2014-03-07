package rpgProject;

import org.jsfml.system.Vector2i;

public interface BattleUnit {
	public void update();
	
	public RegularSprite getSprite();
	
	public int getDepth();
	
	public Vector2i getGrid();
	
	public void draw();
}
