package rpgProject.rooms;

import org.jsfml.graphics.RenderStates;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import rpgProject.WindowMain;
import rpgProject.graphics.RegularSprite;

public class TestRoom implements Room{
	RegularSprite sprite;
	public TestRoom()
	{
		sprite = WindowMain.textureManager.createSprite(WindowMain.textureManager.loadTexture("exampleGuy.png"));
		sprite.setPosition(new Vector2f(300,300));
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		sprite.draw(WindowMain.window,RenderStates.DEFAULT);
	}

	@Override
	public void input(Event e) {
		// TODO Auto-generated method stub
		if(e.asKeyEvent() != null)
		{
			if(e.asKeyEvent().key == Keyboard.Key.SPACE && e.asKeyEvent().type == Event.Type.KEY_PRESSED)
				sprite.nextFrame();
		}
	}

}
