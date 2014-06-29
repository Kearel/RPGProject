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

import rpgProject.battle.BattleUnit;
import rpgProject.graphics.ColoredText;
import rpgProject.graphics.DamageText;
import rpgProject.battle.DepthComparator;
import rpgProject.battle.action.Range;
import rpgProject.battle.Team;
import rpgProject.battle.Unit;
import rpgProject.WindowMain;
import rpgProject.battle.action.Action;

import java.util.Collections;
import java.util.ArrayList;

public class BattleRoom implements Room {
	public static ArrayList<BattleUnit> battleList = new ArrayList<BattleUnit>();
	public static ArrayList<BattleUnit> creationList = new ArrayList<BattleUnit>();
	public static ArrayList<BattleUnit> removeList = new ArrayList<BattleUnit>();
	public static ArrayList<DamageText> textCreate = new ArrayList<DamageText>();
	private static ArrayList<DamageText> textList = new ArrayList<DamageText>();
	public static ArrayList<DamageText> textRemove = new ArrayList<DamageText>();
	private boolean turnStart = false;
	private ArrayList<Unit> turnOrder = new ArrayList<Unit>();
	private Sprite battlefield, selectSprite;
	private ColoredText hudText;
	private int turn;
	private RectangleShape hudBack;
	private RectangleShape redSelect;
	private Unit selectedUnit;
	private Action selectedAction;
	private int menuMode = 0; // 0 = base, 2 + n = attack page #n
	private int mouseMode = 0; // 1 selecting unit 2 moving 3 targeting

	public BattleRoom() {
		WindowMain.window.setTitle("Battle Room");
		hudBack = new RectangleShape(new Vector2f(500, 100));
		hudBack.setFillColor(Color.WHITE);
		hudBack.setOutlineColor(Color.BLACK);
		hudBack.setOutlineThickness(1);
		hudBack.setPosition(0, 0);
		redSelect = new RectangleShape();
		redSelect.setFillColor(Color.TRANSPARENT);
		redSelect.setOutlineColor(Color.RED);
		redSelect.setOutlineThickness(1);
		battlefield = new Sprite(
				WindowMain.textureManager.loadTexture("battlefield.png"));
		selectSprite = new Sprite(
				WindowMain.textureManager.loadTexture("select.png"));
		turn = -1;
		selectedUnit = null;
		selectedAction = null;
		hudText = new ColoredText(WindowMain.defont, "DEFAULT");
	}

	public void loadUnit(String name, String path, int x, int y, Team t,
			int[] stats, Action attack, ArrayList<Action> actions) {
		Texture te = WindowMain.textureManager.loadTexture(path);
		battleList.add(new Unit(name, WindowMain.textureManager
				.createSprite(te), x, y, t, stats, attack, actions));

	}

	public void organizeUnits() {
		turnOrder.clear();
		Unit lowest = null;
		for (BattleUnit b : battleList) {
			if (b.getClass() == Unit.class) {
				Unit u = (Unit) b;
				if (turnOrder.size() == 0) {
					turnOrder.add(u);
					lowest = u;
				} else {
					for (int i = 0; i < turnOrder.size(); i++) {
						if (turnOrder.get(i).getSpeed() <= u.getSpeed()) {
							turnOrder.add(i, u);
							if (turnOrder.get(i).getSpeed() < lowest.getSpeed()) {
								lowest = turnOrder.get(i);
							}
							break;
						}
					}
				}
			}
		}
		@SuppressWarnings("unchecked")
		ArrayList<Unit> extraTurns = (ArrayList<Unit>) turnOrder.clone();
		int iteration = 1;
		while (extraTurns.size() > 0) {
			ArrayList<Unit> tempTurns = new ArrayList<Unit>();
			for (Unit u : extraTurns) {
				if (u.getSpeed() / (iteration * 3) > lowest.getSpeed()) {
					tempTurns.add(u);
					for (int i = 0; i < turnOrder.size(); i++) {
						if (turnOrder.get(i).getSpeed() < u.getSpeed()) {
							turnOrder.add(i, u);
							break;
						}
					}
				}
			}

			extraTurns = tempTurns;
		}
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
		if (turn < turnOrder.size() && turnStart) // we act if we still have
													// actions to take!
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
					turnStart = false;
				}
			} else {
				turn++;
				turnStart = false;
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

		int mX = Mouse.getPosition(WindowMain.window).x;
		int mY = Mouse.getPosition(WindowMain.window).y;
		// @DRAW
		WindowMain.window.draw(battlefield);

		if (selectedUnit != null) {
			WindowMain.window.draw(hudBack);

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
						WindowMain.window.draw(redSelect);
					}
					if (mX > 230 && mY > 70 && mX < 265 && mY < 85) {
						redSelect.setPosition(230, 70);
						redSelect.setSize(new Vector2f(35, 15));
						WindowMain.window.draw(redSelect);
						// skill
					}
					if (mX > 450 && mY > 70 && mX < 495 && mY < 85) {
						redSelect.setPosition(450, 70);
						redSelect.setSize(new Vector2f(45, 15));
						WindowMain.window.draw(redSelect);
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
							WindowMain.window.draw(redSelect);
						}
					}

					if (menuMode * 3 >= selectedUnit.actions.size()) {
						drawText("#G>", 485, 70);
					} else {
						drawText(">", 485, 70);
						if (mX > 485 && mY > 70 && mX < 495 && mY < 85) {
							redSelect.setPosition(485, 70);
							redSelect.setSize(new Vector2f(10, 15));
							WindowMain.window.draw(redSelect);
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
			WindowMain.window.draw(t);
		}
	}

	@Override
	public void input(Event e) {
		if (e.asMouseEvent() != null) {
			int mX = Mouse.getPosition(WindowMain.window).x;
			int mY = Mouse.getPosition(WindowMain.window).y;
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
					&& e.asKeyEvent().key == Keyboard.Key.SPACE && !turnStart) {
				turnStart = true;
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
		WindowMain.window.draw(hudText);
	}

	public void drawRange(Action a, boolean mouse) {
		Range r = a.getRange();
		Vector2i origin;
		Vector2i g = selectedUnit.getGrid();
		if (mouse) {
			Vector2i m = Mouse.getPosition(WindowMain.window);
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
					WindowMain.window.draw(selectSprite);
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
				WindowMain.window.draw(selectSprite);
			}
		}
	}
}
