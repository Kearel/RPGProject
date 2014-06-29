package rpgProject.menuSystem;

public class Button {
	private boolean onButton = false;
	private int x,y,width,height;
	
	public Button(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean onButton()
	{
		return onButton;
	}
	
	public boolean checkButton(int x, int y)
	{
		onButton = (x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height);
		return onButton;
	}
}
