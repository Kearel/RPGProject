package rpgProject.world;

import rpgProject.graphics.RegularSprite;
import rpgProject.rooms.WorldRoom;

public class MovingActor extends WorldActor{

	public MovingActor(WorldRoom room, String name, int x, int y, RegularSprite sprite) {
		super(room, name, x, y, sprite);
	}
	
	public void run()
	{
		if(this.direction == 1)
		{
			
			x+=8;
			moving++;
			if(moving == 4)
			{
				this.direction = 0;
				moving = 0;
			}
		}
		if(this.direction == 2)
		{
			
			y+=8;
			moving++;
			if(moving == 4)
			{
				this.direction = 0;
				moving = 0;
			}
		}
		if(this.direction == 3)
		{
			
			x-=8;
			moving++;
			if(moving == 4)
			{
				this.direction = 0;
				moving = 0;
			}
		}
		if(this.direction == 4)
		{
			
			y-=8;
			moving++;
			if(moving == 4)
			{
				this.direction = 0;
				moving = 0;
			}
		}
	}
	public void goRight()
	{
		if(this.direction == 0 && !room.checkCollision(worldX+1, worldY))
		{
			worldX++;
			this.direction = 1;
		}
	}
	
	public void goDown()
	{
		if(this.direction == 0 && !room.checkCollision(worldX, worldY+1))
		{
			worldY++;
			this.direction = 2;
		}
	}
	
	public void goLeft()
	{
		if(this.direction == 0 && !room.checkCollision(worldX-1, worldY))
		{
			worldX--;
			this.direction = 3;
		}
	}
	
	public void goUp()
	{
		if(direction == 0 && !room.checkCollision(worldX, worldY-1))
		{
			worldY--;
			this.direction = 4;
		}
	}
	
	public Class getType()
	{
		return MovingActor.class;
	}
}
