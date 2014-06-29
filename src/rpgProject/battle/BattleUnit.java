package rpgProject.battle;

import org.jsfml.system.Vector2i;

import rpgProject.graphics.RegularSprite;

public interface BattleUnit {
	public void update();
	
	public RegularSprite getSprite();
	
	public int getDepth();
	
	public Vector2i getGrid();
	
	public void draw();
}
