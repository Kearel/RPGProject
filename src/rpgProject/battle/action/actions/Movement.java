package rpgProject.battle.action.actions;

import java.util.ArrayList;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import rpgProject.battle.action.Action;
import rpgProject.battle.action.Range;
import rpgProject.battle.Unit;

public class Movement extends Action {
	public Movement() {
		super(20, "Move", "Move around the map.");
		setMovement(2);
	}

	@Override
	public void perform(Unit u) {
		super.perform(u);
		if (super.getFrame() == 1) {
			Vector2i movement = super.getRange().getOrigin();
			if (u.getGrid().x < movement.x) {
				u.setGrid(new Vector2i(u.getGrid().x + 1, u.getGrid().y));
			}
			if (u.getGrid().x > movement.x) {
				u.setGrid(new Vector2i(u.getGrid().x - 1, u.getGrid().y));
			}

			if (u.getGrid().y < movement.y) {
				u.setGrid(new Vector2i(u.getGrid().x, u.getGrid().y + 1));
			}
			if (u.getGrid().y > movement.y) {
				u.setGrid(new Vector2i(u.getGrid().x, u.getGrid().y - 1));
			}
			if (u.getGrid().x != movement.x || u.getGrid().y != movement.y)
				super.reset();

			u.position = new Vector2f(u.getGrid().x * 75 + 65,
					u.getGrid().y * 50 + 325);
		}

	}

	public void setMovement(int move) {
		ArrayList<Vector2i> shape = new ArrayList<Vector2i>();

		int count = move * 2 + 1;
		for (int i = 0; i < count; i++) {
			shape.add(new Vector2i((int) -count / 2 + i, 0));
		}

		count -= 2;
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < count - 2 * i; j++) {
				shape.add(new Vector2i((int) -(count - 2 * i) / 2 + j, i + 1));
				shape.add(new Vector2i((int) -(count - 2 * i) / 2 + j, -i - 1));
			}
		}
		Vector2i[] ar = shape.toArray(new Vector2i[0]);
		Vector2i[] ar2 = new Vector2i[1];
		ar2[0] = new Vector2i(0, 0);
		super.setRange(new Range(new Vector2i(0, 0), ar2, ar, true));
	}
}
