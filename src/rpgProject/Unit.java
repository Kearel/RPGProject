package rpgProject;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import rpgProject.actions.Movement;
import rpgProject.rooms.BattleRoom;

public class Unit implements BattleUnit {
	private RegularSprite sprite;
	public Vector2f position;
	private Vector2i gridPosition;
	private String name;
	private int hpStat, strengthStat, dexterityStat, intelligenceStat, wisdomStat;
	private int hp, speed;
	public ArrayList<Action> actions;
	private int depth;
	private int movement;
	public boolean acting;
	public Action action;
	private Action attack;
	public Movement move;
	private Team team;

	public Unit(String n, Texture t, int ox, int oy, int width, int height,
			int x, int y, Team te, int[] stats, Action at,
			ArrayList<Action> a) {
		name = n;
		sprite = new RegularSprite(t, ox, oy, width, height);
		gridPosition = new Vector2i(x, y);
		position = new Vector2f(x * 75 + 65, y * 50 + 325);
		team = te;
		team.add(this);
		hpStat = stats[0];
		strengthStat = stats[1];
		dexterityStat = stats[2];
		intelligenceStat = stats[3];
		wisdomStat = stats[4];
		hp = stats[0] * 12;
		speed = stats[2] * 2;
		attack = at;
		actions = a;
		action = null;
		movement = 2;
		move = new Movement();
		move.setMovement(movement);
		depth = 0;
		acting = false;
	}

	public void draw() {
		if (!acting) {
			position = new Vector2f(gridPosition.x * 75 + 65,
					gridPosition.y * 50 + 325);
		}
		depth = (int) position.y;
		sprite.setFlipped(gridPosition.x > 2);
		sprite.setPosition(position);
		sprite.draw(windowMain.window, RenderStates.DEFAULT);
	}

	public void update() {
		if (acting) {
			action.perform(this);
		}
		if (hp < 1)
			delete();
	}

	public Team getTeam() {
		return team;
	}

	public String getName() {
		return name;
	}

	public RegularSprite getSprite() {
		return sprite;
	}

	public void delete() {
		BattleRoom.removeList.add(this);
	}

	public int getDepth() {
		return depth;
	}
	
	public int getHP() {
		return hp;
	}
	
	public int getHPMax() {
		return hpStat * 12;
	}
	
	public int getHPStat() {
		return hpStat;
	}
	
	public int getStrengthStat() {
		return strengthStat;
	}
	
	public int getDexterityStat() {
		return dexterityStat;
	}
	
	public int getIntelligenceStat() {
		return intelligenceStat;
	}
	
	public int getWisdomStat() {
		return wisdomStat;
	}

	public int getSpeed() {
		return speed;
	}

	@Override
	public Vector2i getGrid() {
		return gridPosition;
	}

	public void setGrid(Vector2i g) {
		gridPosition = g;
	}

	public Action attack() {
		return attack;
	}

	public int damage(int d) {
		hp -= d;
		BattleRoom.textCreate.add(new DamageText("-1", Color.BLACK, 10,
				new Vector2f(position.x - 25, position.y - 50)));
		return d;
	}
}
