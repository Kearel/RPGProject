package rpgProject.world;

import org.jsfml.graphics.RenderStates;
import org.jsfml.system.Vector2f;

import rpgProject.graphics.RegularSprite;
import rpgProject.WindowMain;
import rpgProject.rooms.WorldRoom;

public class WorldActor {
	public int x, y;
	public int worldX, worldY;
	public WorldRoom room;
	public RegularSprite sprite;
	public int direction, moving;
	public String name;
	public WorldActor(WorldRoom room, String name,  int x, int y, RegularSprite sprite)
	{
		this.room = room;
		this.name = name;
		this.worldX = x;
		this.worldY = y;
		this.x = x*32;
		this.y = y*32;
		this.sprite = sprite;
		direction = 0;
		moving = 0;
	}
	
	public void run()
	{
		
	}
	
	public void draw()
	{
		sprite.setPosition(new Vector2f(x,y));
		sprite.draw(WindowMain.window, RenderStates.DEFAULT);
	}
	
	public Class getType()
	{
		return WorldActor.class;
	}
}
