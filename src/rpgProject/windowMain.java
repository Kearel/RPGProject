package rpgProject;

/*
 * health - controls how much health you have
 * speed - how soon your turn is
 * attack - damage stat
 * defense - defense stat
 * 
 */
import java.io.IOException;
import java.io.InputStream;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.graphics.Color;

import rpgProject.example.TestRoom;
import rpgProject.rooms.BattleRoom;
import rpgProject.rooms.Room;

public class windowMain {
	public static RenderWindow window = new RenderWindow(
			new VideoMode(500, 500), "DEFAULT");
	public static Room room;
	public static Font defont;

	public static void main(String[] args) {
		load();
		room = new BattleRoom(
				"Example 1,exampleGuy.png,50,87,100,100,2,2,0,10,5,ExamplePew,ExamplePew,ExamplePew\nExample 2,exampleGuy.png,50,85,100,100,3,2,1,3,5,ExamplePew");
		// room = new TestRoom();
		window.setFramerateLimit(30);

		while (window.isOpen()) {
			window.clear(Color.WHITE);
			room.run();
			window.display();
			for (Event event : window.pollEvents()) {
				if (event.type == Event.Type.CLOSED) {
					window.close();
				} else {
					room.input(event);
				}
			}
		}
	}

	private static void load() {
		defont = new Font();
		InputStream is = windowMain.class.getClassLoader().getResourceAsStream(
				"rpgProject/resources/FreeMonoBold.ttf");
		try {
			defont.loadFromStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
