package rpgProject.rooms;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import rpgProject.WindowMain;
import rpgProject.graphics.RegularSprite;
import rpgProject.world.MovingActor;
import rpgProject.world.WorldActor;

public class WorldRoom implements Room {
	private Sprite[][] world;
	private MovingActor player;
	private boolean[][] collision;
	private HashMap<String, Sprite> worldSprites = new HashMap<String, Sprite>();

	public WorldRoom(String path, int playerX, int playerY) {
		// lets first load the player
		Texture playerTexture = WindowMain.textureManager
				.loadTexture("/roomFiles/worldPlayer.png");
		player = new MovingActor(this, "Player", playerX, playerY,
				new RegularSprite(playerTexture));

		// next we look at the file
		InputStream is = this.getClass().getResourceAsStream(
				"/rpgProject/resources/roomFiles/" + path);
		Scanner scan = new Scanner(is);
		String current = scan.next();
		// anything before 'ROOM' is ignored, use it for comment space
		while (!current.equals("ROOM")) {
			current = scan.next();
		}
		current = scan.next();
		while (!current.equals("DIMENSIONS")) {
			worldSprites.put(current, loadSprite(scan.next()));
			if (!scan.hasNext()) {
				scan.close();
				throw new IllegalArgumentException();
			}
			current = scan.next();
		}

		// we need to get the dimensions of the room
		// so we use the next two lines to get that
		if (!scan.hasNextInt()) {
			scan.close();
			throw new IllegalArgumentException();
		}
		int width = scan.nextInt();

		if (!scan.hasNextInt()) {
			scan.close();
			throw new IllegalArgumentException();
		}
		int height = scan.nextInt();

		world = new Sprite[width][height];
		collision = new boolean[width][height];

		scan.next();

		// next we load the room

		int x = 0;
		int y = 0;
		current = scan.next();
		while (!current.equals("COLLISION")) {

			world[x][y] = worldSprites.get(current);

			x++;
			if (x == width) {
				x = 0;
				y++;
			}
			current = scan.next();
		}
		x = 0;
		y = 0;
		current = scan.next();
		while (!current.equals("ACTORS")) {
			collision[x][y] = (current.equals("1"));
			x++;
			if (x == width) {
				x = 0;
				y++;
			}
			current = scan.next();
		}

		scan.close();
	}

	public WorldActor loadActor(String information) {
		WorldActor actor = null;
		Scanner scan = new Scanner(information);

		String type = scan.next();
		if (type.equals("DA")) // Dialogue Actor
		{
			String name = scan.next();
			RegularSprite sprite = new RegularSprite(
					WindowMain.textureManager.loadTexture(scan.next()));
		}

		scan.close();
		return actor;

	}

	@Override
	public void run() {
		draw();
		player.run();
	}

	public void draw() {
		Sprite draw = null;
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				draw = world[i][j];
				draw.setPosition(i * 32, j * 32);
				WindowMain.window.draw(draw);
			}
		}
		player.draw();
	}

	public Sprite loadSprite(String path) {
		return new Sprite(WindowMain.textureManager.loadTexture(path));
	}

	public boolean checkCollision(int x, int y) {
		return collision[x][y];
	}

	@Override
	public void input(Event e) {
		if (e.asKeyEvent() != null) {
			if (e.asKeyEvent().key == Keyboard.Key.RIGHT) {
				player.goRight();
			} else if (e.asKeyEvent().key == Keyboard.Key.DOWN) {
				player.goDown();
			} else if (e.asKeyEvent().key == Keyboard.Key.LEFT) {
				player.goLeft();
			} else if (e.asKeyEvent().key == Keyboard.Key.UP) {
				player.goUp();
			}
		}

	}

}
