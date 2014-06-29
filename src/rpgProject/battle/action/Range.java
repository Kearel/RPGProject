package rpgProject.battle.action;

import org.jsfml.system.Vector2i;

public class Range {

	private Vector2i origin;
	private Vector2i[] shape;
	private Vector2i[] selectionShape;
	private boolean mouse;

	public Range(Vector2i o, Vector2i[] s, Vector2i[] sel, boolean m) {
		origin = o;
		shape = s;
		selectionShape = sel;
		mouse = m;
	}
	
	public Vector2i getOrigin()
	{
		return origin;
	}
	
	public void setOrigin(int x, int y)
	{
		origin = new Vector2i(x,y);
	}
	
	public Vector2i[] getShape()
	{
		return shape;
	}
	
	public Vector2i[] getSelectionShape()
	{
		return selectionShape;
	}
	
	public boolean isOK(int x, int y)
	{
		if(!mouse)
			return true;
		for(Vector2i i : selectionShape)
		{
			if(i.x == x && i.y == y)
				return true;
		}
		
		return false;
	}
	
	public boolean mouse()
	{
		return mouse;
	}

}
