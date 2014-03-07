package rpgProject.example;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import rpgProject.ColoredText;
import rpgProject.windowMain;
import rpgProject.rooms.Room;

public class TestRoom implements Room {
	private ColoredText text;
	private Text text2;

	public TestRoom() {
		text = new ColoredText(windowMain.defont, "#rAttack Skill Move");
		text.setSize(40);
		text2 = new Text("Attack Skill Move", windowMain.defont);
		text2.setPosition(0,0);
		text2.setColor(Color.BLACK);
		text2.setCharacterSize(40);
	}

	@Override
	public void run() {
		windowMain.window.draw(text2);
		windowMain.window.draw(text);
	}

	@Override
	public void input(Event e) {
	}

}
