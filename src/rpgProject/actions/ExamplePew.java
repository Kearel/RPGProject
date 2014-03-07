package rpgProject.actions;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import rpgProject.Action;
import rpgProject.BattleUnit;
import rpgProject.Range;
import rpgProject.Unit;
import rpgProject.example.PewProjectile;
import rpgProject.rooms.BattleRoom;

public class ExamplePew extends Action {

	public ExamplePew() {
		super(30, "Pew Pew Pew", "Pew the pew pew: become a legend.");
		Vector2i[] shape = new Vector2i[6];
		shape[0] = new Vector2i(1,0);
		shape[1] = new Vector2i(2,0);
		shape[2] = new Vector2i(3,0);
		shape[3] = new Vector2i(4,0);
		shape[4] = new Vector2i(5,0);
		shape[5] = new Vector2i(6,0);
		Vector2i origin = new Vector2i(0,0);
		Range r = new Range(origin,shape,null,false);
		super.setRange(r);
	}

	public String[] load() {
		String[] s = new String[1];
		s[0] = "pewpew.png";
		return s;
	}

	public void perform(Unit user) {
		if (!super.isDone()) {
			super.perform(user);
			if (super.getFrame() % 2 == 0) {
				user.getSprite().setFrame(1);
				BattleRoom.creationList.add(new PewProjectile(user.getTeam(), !user.getSprite().getFlipped(),
						new Vector2f(user.position.x, user.position.y)));
			} else {
				user.getSprite().setFrame(0);
			}
			if (super.getFrame() == 30)
				user.getSprite().setFrame(0);
		}
	}

	public boolean isDone() {
		for (BattleUnit o : BattleRoom.battleList) // basically making sure the
													// action doesn't end
		// if there are still projectiles in the room
		{
			if (o.getClass() == PewProjectile.class)
				return false;
		}
		return super.isDone();
	}

}
