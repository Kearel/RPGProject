package rpgProject;

import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import rpgProject.rooms.BattleRoom;

public abstract class Projectile implements BattleUnit {
	public Vector2f position;
	public RegularSprite sprite;
	public int depth;
	public Team team;

	public Projectile(Vector2f pos, Texture t, int ox, int oy, int width,
			int height) {
		position = pos;
		sprite = new RegularSprite(t, ox, oy, width, height);
		sprite.setPosition(pos);
		depth = (int) pos.y;
	}

	public RegularSprite getSprite() {
		return sprite;
	}
	
	public void draw()
	{
		sprite.setPosition(position);
		sprite.draw(windowMain.window, RenderStates.DEFAULT);
	}

	public void update() {
	}

	public void delete() {
		onDeath();
		BattleRoom.removeList.add(this);
	}

	public int getDepth() {
		return depth;
	}

	public Vector2i getGrid() {
		int x = (int) (position.x - 25) / 75;
		int y = (int) (position.y - 300) / 50;
		return new Vector2i(x, y);
	}

	public void onDeath() {
		// a placeholder method for when a projectile gets removed/deleted
	}
}
