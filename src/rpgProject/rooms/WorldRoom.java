package rpgProject.rooms;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.window.event.Event;

public class WorldRoom implements Room {
	private int[][] world;
	private HashMap<Integer, Sprite> worldSprites = new HashMap<Integer, Sprite>();

	public WorldRoom(String path) {
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("/rpgProject/resources/" + path);
		Scanner scan = new Scanner(is);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public Sprite loadSprite(String path) {
		return new Sprite();
	};

	@Override
	public void input(Event e) {
		// TODO Auto-generated method stub

	}

}
