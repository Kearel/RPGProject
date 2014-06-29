package rpgProject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;
import org.jsfml.graphics.Color;

import rpgProject.battle.Team;
import rpgProject.battle.action.Action;
import rpgProject.battle.action.actions.ExamplePew;
import rpgProject.rooms.BattleRoom;
import rpgProject.rooms.Room;
import rpgProject.rooms.WorldRoom;

public class WindowMain {
	public static RenderWindow window = new RenderWindow(
			new VideoMode(500, 500), "DEFAULT");
	public static Room room;
	public static Font defont;
	public static TextureManager textureManager;

	public static void main(String[] args) {
		load();
		textureManager = new TextureManager();
		textureManager.createSprite(textureManager
				.loadTexture("exampleGuy.png"));

		BattleRoom b = new BattleRoom();
		int[] stats1 = { 3, 1, 1, 1, 1 };
		int[] stats2 = { 1, 1, 3, 1, 1 };
		b.loadUnit("Example 1", "exampleGuy.png", 2, 2, new Team(), stats1,
				new ExamplePew(), new ArrayList<Action>());
		b.loadUnit("Example 2", "exampleGuy.png", 1, 1, new Team(), stats2,
				new ExamplePew(), new ArrayList<Action>());
		room = b;
		
		//room = new rpgProject.rooms.TestRoom();
		//room = new WorldRoom("testRoom", 1, 1);
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
		InputStream is = WindowMain.class.getClassLoader().getResourceAsStream(
				"rpgProject/resources/FreeMonoBold.ttf");
		try {
			defont.loadFromStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
