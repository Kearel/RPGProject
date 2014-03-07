package rpgProject.example;

import org.jsfml.system.Vector2f;

import rpgProject.Projectile;
import rpgProject.Team;
import rpgProject.Unit;
import rpgProject.rooms.BattleRoom;

public class PewProjectile extends Projectile {

	private boolean right;
	private Team team;

	public PewProjectile(Team t, boolean right, Vector2f pos) {
		super(pos, BattleRoom.textures.get("pewpew.png"), 7, 40, 15, 15);
		this.right = right;
		team = t;

	}

	public void update() {
		float x = 0;
		if (right) {
			x = super.position.x + 5;
		} else {
			x = super.position.x - 5;
		}
		float y = super.position.y;
		super.position = new Vector2f(x, y);
		super.update();
		if (x < 0 || x > 500)
			delete();
		
		Unit u = BattleRoom.getUnit(super.getGrid().x, super.getGrid().y);
		if(u != null && !team.onTeam(u))
		{
			u.damage(1);
			
			delete();
		}
	}

	@Override
	public int getDepth() {
		return (int) super.position.y;
	}
}
