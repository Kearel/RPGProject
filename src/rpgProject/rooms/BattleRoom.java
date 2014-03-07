package rpgProject.rooms;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import rpgProject.BattleUnit;
import rpgProject.ColoredText;
import rpgProject.DamageText;
import rpgProject.DepthComparator;
import rpgProject.Range;
import rpgProject.RegularSprite;
import rpgProject.SpeedComparator;
import rpgProject.Team;
import rpgProject.Unit;
import rpgProject.windowMain;
import rpgProject.Action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class BattleRoom implements Room {
	public static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	public static ArrayList<BattleUnit> battleList = new ArrayList<BattleUnit>();
	public static ArrayList<BattleUnit> creationList = new ArrayList<BattleUnit>();
	public static ArrayList<BattleUnit> removeList = new ArrayList<BattleUnit>();
	public static ArrayList<DamageText> textCreate = new ArrayList<DamageText>();
	private static ArrayList<DamageText> textList = new ArrayList<DamageText>();
	public static ArrayList<DamageText> textRemove = new ArrayList<DamageText>();
	private ArrayList<Unit> turnOrder = new ArrayList<Unit>();
	private Sprite battlefield, selectSprite;
	private RegularSprite bar;
	private ColoredText hudText;
	private int turn;
	private ArrayList<Team> teams = new ArrayList<Team>();
	private RectangleShape hudBack;
	private RectangleShape redSelect;
	private Unit selectedUnit;
	private Action selectedAction;
	private int menuMode = 0; // 0 = base, 2 + n = attack page #n
	private int mouseMode = 0; // 1 selecting unit 2 moving 3 targeting

	public BattleRoom(String input) {
		windowMain.window.setTitle("Battle Room");
		hudBack = new RectangleShape(new Vector2f(500, 100));
		hudBack.setFillColor(Color.WHITE);
		hudBack.setOutlineColor(Color.BLACK);
		hudBack.setOutlineThickness(1);
		hudBack.setPosition(0, 0);
		redSelect = new RectangleShape();
		redSelect.setFillColor(Color.TRANSPARENT);
		redSelect.setOutlineColor(Color.RED);
		redSelect.setOutlineThickness(1);
		battlefield = new Sprite(loadTexture("battlefield.png"));
		selectSprite = new Sprite(loadTexture("select.png"));
		bar = new RegularSprite(loadTexture("bars.png"), 0, 0, 16, 16);
		loadUnits(input);
		organizeUnits();
		turn = turnOrder.size();
		selectedUnit = null;
		selectedAction = null;
		hudText = new ColoredText(windowMain.defont, "DEFAULT");
	}

	public void organizeUnits() {
		// first we re-add all units from the battleList in order to get those
		// that may have been created after.
		turnOrder.clear();
		for (BattleUnit b : battleList) {
			if (b.getClass() == Unit.class) {
				turnOrder.add((Unit) b);
			}
		}

		// then we sort them by their speed.
		Collections.sort(turnOrder, new SpeedComparator());
	}

	public void loadUnits(String units) {
		Scanner unitScan = new Scanner(units);
		while (unitScan.hasNextLine()) {
			String unitCode = unitScan.nextLine();
			Scanner makeUnit = new Scanner(unitCode);

			makeUnit.useDelimiter(",");

			String name = makeUnit.next();
			String path = makeUnit.next();
			int ox = makeUnit.nextInt();
			int oy = makeUnit.nextInt();
			int width = makeUnit.nextInt();
			int height = makeUnit.nextInt();
			int x = makeUnit.nextInt();
			int y = makeUnit.nextInt();
			int te = makeUnit.nextInt();
			int hC = makeUnit.nextInt();
			int hB = makeUnit.nextInt();
			Action at = null;
			try {
				Class<?> attackClass = Class.forName("rpgProject.actions."
						+ makeUnit.next());
				at = (Action) attackClass.newInstance();
				String[] text = at.load();
				for (String s : text)
					loadTexture(s);
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
			ArrayList<Action> actions = new ArrayList<Action>();
			while (makeUnit.hasNext()) {
				Class<?> actionClass;
				try {
					actionClass = Class.forName("rpgProject.actions."
							+ makeUnit.next());
					Action a = (Action) actionClass.newInstance();
					String[] text = a.load();
					for (String s : text) {
						loadTexture(s);
					}
					actions.add(a);
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (teams.size() <= te) {
				teams.add(new Team());
			}
			battleList.add(new Unit(name, loadTexture(path), ox, oy, width,
					height, x, y, teams.get(te), hC, hB, at, actions));
			makeUnit.close();
		}
		unitScan.close();
	}

	public Texture loadTexture(String path) {
		if (!textures.containsKey(path)) {
			Texture t = new Texture();
			try {
				InputStream is = this.getClass().getResourceAsStream(
						"/rpgProject/resources/" + path);
				t.loadFromStream(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			textures.put(path, t);

			return t;
		} else
			return textures.get(path);
	}

	public static Unit getUnit(int x, int y) {
		for (BattleUnit u : battleList) {
			if (u.getClass() == Unit.class) {
				Vector2i grid = u.getGrid();
				if (x == grid.x && y == grid.y) {
					Unit b = (Unit) u;
					return b;
				}
			}
		}

		return null;
	}

	public void run() {
		// @UPDATE
		if (turn == -1) // -1 basically means a new turn, so we first organize..
		{
			organizeUnits();
			turn++;
		}
		if (turn < turnOrder.size()) // we act if we still have actions to take!
		{
			Unit u = turnOrder.get(turn);
			if (u.action != null) {
				if (!u.action.isDone()) {
					u.acting = true;
				} else {
					turn++;
					u.action.reset();
					u.action = null;
					u.acting = false;
				}
			} else {
				turn++;
			}
		}
		// @UNIT @UPDATE
		for (BattleUnit b : battleList) // update
		{
			b.update();
		}
		for (BattleUnit b : removeList) // remove
		{
			battleList.remove(b);
			turnOrder.remove(b);
			if (selectedUnit == b)
				selectedUnit = null;
		}
		for (BattleUnit b : creationList) // create
		{
			battleList.add(b);
		}
		creationList.clear();
		removeList.clear();

		for (DamageText t : textCreate) {
			textList.add(t);
		}
		for (DamageText t : textRemove) {
			textList.remove(t);
		}
		textCreate.clear();
		textRemove.clear();

		int mX = Mouse.getPosition(windowMain.window).x;
		int mY = Mouse.getPosition(windowMain.window).y;
		// @DRAW
		windowMain.window.draw(battlefield);

		if (selectedUnit != null) {
			windowMain.window.draw(hudBack);
			// @HP
			bar.setColor(Color.RED);
			int cells = selectedUnit.hp % selectedUnit.hpCell;
			int bars = (selectedUnit.hp - cells) / selectedUnit.hpCell;
			int allowedBars = (470 - 470 % (32 + selectedUnit.hpCell * 16))
					/ (32 + selectedUnit.hpCell * 16);
			if (cells > 0)
				allowedBars--;
			int shownBars = Math.min(allowedBars, bars);
			if (cells > 0) {
				bar.setFrame(0);
				bar.setPosition(new Vector2f(30 + shownBars
						* (32 + 16 * selectedUnit.hpCell), 10));
				windowMain.window.draw(bar);

				for (int i = 0; i < selectedUnit.hpCell; i++) {
					if (i < cells)
						bar.setFrame(3);
					else
						bar.setFrame(2);
					bar.setPosition(new Vector2f(46 + shownBars
							* (32 + 16 * selectedUnit.hpCell) + 16 * i, 10));
					windowMain.window.draw(bar);
				}
				bar.setFrame(4);
				bar.setPosition(new Vector2f(46 + shownBars
						* (32 + 16 * selectedUnit.hpCell) + 16
						* selectedUnit.hpCell, 10));
				windowMain.window.draw(bar);
			}
			for (int i = 0; i < shownBars; i++) {
				bar.setFrame(0);
				bar.setPosition(new Vector2f(30 + i
						* (32 + 16 * selectedUnit.hpCell), 10));
				windowMain.window.draw(bar);
				for (int j = 0; j < selectedUnit.hpCell; j++) {
					bar.setFrame(1);
					bar.setPosition(new Vector2f(46 + i
							* (32 + 16 * selectedUnit.hpCell) + 16 * j, 10));
					windowMain.window.draw(bar);
				}
				bar.setFrame(4);
				bar.setPosition(new Vector2f(46 + i
						* (32 + 16 * selectedUnit.hpCell) + 16
						* selectedUnit.hpCell, 10));
				windowMain.window.draw(bar);
			}

			if (allowedBars < bars) {
				drawText(bars - allowedBars + " x", 10, 10);
			}

			// @RANGE
			if (mouseMode == 1) {
				drawText("#B" + selectedAction.getName() + "#b: "
						+ selectedAction.getDescription(), 10, 70);
				Range r = selectedAction.getRange();

				if (!r.mouse()) {
					r.setOrigin(selectedUnit.getGrid().x,
							selectedUnit.getGrid().y);
				}

				drawRange(selectedAction, selectedAction.getRange().mouse());
			}

			if (mouseMode == 0) {
				// @RANGE
				if (selectedUnit.action != null) {
					drawRange(selectedUnit.action, false);
				}
				if (menuMode == 0) {
					drawText("Attack", 10, 70);
					drawText("Skill", 230, 70);
					drawText("Move", 450, 70);

					if (mX > 10 && mY > 70 && mX < 65 && mY < 85) {
						redSelect.setPosition(10, 70);
						redSelect.setSize(new Vector2f(55, 15));
						windowMain.window.draw(redSelect);
					}
					if (mX > 230 && mY > 70 && mX < 265 && mY < 85) {
						redSelect.setPosition(230, 70);
						redSelect.setSize(new Vector2f(35, 15));
						windowMain.window.draw(redSelect);
						// skill
					}
					if (mX > 450 && mY > 70 && mX < 495 && mY < 85) {
						redSelect.setPosition(450, 70);
						redSelect.setSize(new Vector2f(45, 15));
						windowMain.window.draw(redSelect);
						// move
					}

				} else {
					if (menuMode == 1) {
						drawText("#G<", 10, 70);
					} else {
						drawText("<", 10, 70);
						if (mX > 10 && mY > 70 && mX < 20 && mY < 85) {
							redSelect.setPosition(10, 70);
							redSelect.setSize(new Vector2f(10, 15));
						}
					}

					int reach = Math.min(3, selectedUnit.actions.size()
							- (menuMode - 1) * 3);

					for (int i = 0; i < reach; i++) {
						drawText(
								selectedUnit.actions
										.get(i + (menuMode - 1) * 3).getName(),
								20 + i * 150, 70);

						if (mX > 20 + i * 150 && mY > 70
								&& mX < 20 + i * 150 + hudText.getTotalWidth()
								&& mY < 85) {
							redSelect.setPosition(20 + i * 150, 70);
							redSelect.setSize(new Vector2f(hudText
									.getTotalWidth(), 15));
							windowMain.window.draw(redSelect);
						}
					}

					if (menuMode * 3 >= selectedUnit.actions.size()) {
						drawText("#G>", 485, 70);
					} else {
						drawText(">", 485, 70);
						if (mX > 485 && mY > 70 && mX < 495 && mY < 85) {
							redSelect.setPosition(485, 70);
							redSelect.setSize(new Vector2f(10, 15));
							windowMain.window.draw(redSelect);
						}
					}
				}
			}

		}
		// @DRAW @UNIT
		Collections.sort(battleList, new DepthComparator());
		for (BattleUnit b : battleList) {
			b.draw();
		}

		for (DamageText t : textList) {
			t.update();
			windowMain.window.draw(t);
		}
	}

	@Override
	public void input(Event e) {
		if (e.asMouseEvent() != null) {
			int mX = Mouse.getPosition(windowMain.window).x;
			int mY = Mouse.getPosition(windowMain.window).y;
			if (e.asMouseEvent().type == Event.Type.MOUSE_BUTTON_RELEASED) {
				if (e.asMouseButtonEvent().button == Mouse.Button.LEFT) {
					if (mY > 100 && mouseMode == 0 && turn == turnOrder.size()
							&& menuMode == 0) {
						// unit checking
						/*
						 * Checks each units gridposition, if it is similar to
						 * the grid position that the mouse is in, it selects
						 * that unit. gridPosition.x * 75 + 65, gridPosition.y *
						 * 50 + 325
						 */
						selectedUnit = null;
						menuMode = 0;
						mouseMode = 0;
						int gX = (int) Math.floor((mX - 25) / 75);
						int gY = (int) Math.floor((mY - 300) / 50);
						Unit possibleUnit = null;
						// turn order at this point should hold all units
						for (Unit u : turnOrder) {
							Vector2i g = u.getGrid();
							if (g.x == gX && g.y == gY) {
								selectedUnit = u;
								break;
							}
							if (g.x == gX && g.y - 1 == gY) {
								possibleUnit = u;
							}
						}
						if (selectedUnit == null)
							selectedUnit = possibleUnit;

					}
					// @MENU @MOUSE @RANGE
					if (mouseMode == 1) {
						Range r = selectedAction.getRange();

						if (!r.mouse()) {
							r.setOrigin(selectedUnit.getGrid().x,
									selectedUnit.getGrid().y);
						}
						if (r.mouse()) {
							int x = (int) Math.floor((mX - 25) / 75)
									- selectedUnit.getGrid().x;
							int y = (int) Math.floor((mY - 300) / 50)
									- selectedUnit.getGrid().y;
							if (r.isOK(x, y)) {
								r.setOrigin((int) Math.floor((mX - 25) / 75),
										(int) Math.floor((mY - 300) / 50));
								mouseMode = 0;
								menuMode = 0;
								selectedUnit.action = selectedAction;
								selectedAction = null;
							}
						} else {
							selectedUnit.action = selectedAction;
							selectedAction = null;
							mouseMode = 0;
							menuMode = 0;
						}
					}
					if (mouseMode == 0) {
						if (menuMode == 0) {
							if (mX > 10 && mY > 70 && mX < 65 && mY < 85) {
								mouseMode = 1;
								selectedAction = selectedUnit.attack();
							}
							if (mX > 230 && mY > 70 && mX < 265 && mY < 85) {
								menuMode = 1;
							}
							if (mX > 450 && mY > 70 && mX < 495 && mY < 85) {
								mouseMode = 1;
								selectedAction = selectedUnit.move;
							}
						} else {
							if (menuMode != 1) {
								if (mX > 10 && mY > 70 && mX < 20 && mY < 85) {
									menuMode--;
								}
							}

							int reach = Math.min(3, selectedUnit.actions.size()
									- (menuMode - 1) * 3);

							for (int i = 0; i < reach; i++) {
								hudText.setString(selectedUnit.actions.get(
										i + (menuMode - 1) * 3).getName());
								if (mX > 20 + i * 150
										&& mY > 70
										&& mX < 20 + i * 150
												+ hudText.getTotalWidth()
										&& mY < 85) {
									selectedAction = selectedUnit.actions.get(i
											+ (menuMode - 1) * 3);
									mouseMode = 1;
								}
							}

							if (menuMode * 3 < selectedUnit.actions.size()) {
								menuMode++;
							}
						}
					}
				} else if (e.asMouseButtonEvent().button == Mouse.Button.RIGHT) {
					if (selectedUnit != null && mouseMode == 1) {
						selectedAction = null;
					}
					selectedUnit = null;
					mouseMode = 0;
					menuMode = 0;
				}
			}
		} else if (e.asKeyEvent() != null) {
			if (e.asKeyEvent().type == Event.Type.KEY_RELEASED
					&& e.asKeyEvent().key == Keyboard.Key.SPACE
					&& turn == turnOrder.size()) {
				turn = -1;
			}
		}
	}

	/**
	 * Draws text with the arguments given with the default render settings
	 * using an already loaded Text object
	 */
	public void drawText(String text, int x, int y) {
		hudText.setString(text);
		hudText.setPosition(x, y);
		windowMain.window.draw(hudText);
	}

	public void drawRange(Action a, boolean mouse) {
		Range r = a.getRange();
		Vector2i origin;
		Vector2i g = selectedUnit.getGrid();
		if (mouse) {
			Vector2i m = Mouse.getPosition(windowMain.window);
			origin = new Vector2i((int) Math.floor((m.x - 25) / 75),
					(int) Math.floor((m.y - 300) / 50));
			for (Vector2i i : r.getSelectionShape()) {
				int x;
				if (selectedUnit.getSprite().getFlipped())
					x = g.x - i.x;
				else
					x = g.x + i.x;
				int y = g.y + i.y;
				if (x >= 0 && x < 6 && y >= 0 && y < 3) {
					selectSprite.setPosition(x * 75 + 25, y * 50 + 300);
					selectSprite.setColor(Color.BLUE);
					windowMain.window.draw(selectSprite);
				}
			}
		} else {
			origin = r.getOrigin();
		}

		for (Vector2i i : r.getShape()) {
			int x;
			if (selectedUnit.getSprite().getFlipped())
				x = origin.x - i.x;
			else
				x = origin.x + i.x;
			int y = origin.y + i.y;
			if (x >= 0 && x < 6 && y >= 0 && y < 3) {
				selectSprite.setPosition(x * 75 + 25, y * 50 + 300);
				selectSprite.setColor(Color.RED);
				windowMain.window.draw(selectSprite);
			}
		}
	}
}
