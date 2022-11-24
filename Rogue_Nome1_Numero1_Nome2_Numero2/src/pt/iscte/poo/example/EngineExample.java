package pt.iscte.poo.example;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Point2D;
import java.awt.event.KeyEvent;
import pt.iscte.poo.utils.Vector2D;

public class EngineExample implements Observer {

	public static final int GRID_HEIGHT = 11;
	public static final int GRID_WIDTH = 10;

	private static EngineExample INSTANCE = null;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

	private Hero hero;
	private GameHud hud;
	private GameElement item;
	private List<List<GameElement>> rooms;
	private List<GameElement> entities;
	private int turns;
	private int numbersOfRooms = 4;
	private int currentRoom;

	public static EngineExample getInstance() {
		if (INSTANCE == null)
			INSTANCE = new EngineExample();
		return INSTANCE;
	}

	private EngineExample() {
		gui.registerObserver(this);
		gui.setSize(GRID_WIDTH, GRID_HEIGHT);
		gui.go();
	}

	public void start() {
		entities = new ArrayList<>();
		rooms = new ArrayList<>();
		for (int i = 0; i != numbersOfRooms; i++) {
			rooms.add(i, new Room("room" + i).getList());
		}
		addFloor();
		this.entities = rooms.get(0);
		currentRoom = 0;
		for (int i = 0; i != entities.size(); i++) {
			gui.addImage(entities.get(i));
		}
		startHud();
		addHero(new Point2D(4, 4));
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns);
		gui.update();
	}

	private void addFloor() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int x = 0; x != GRID_WIDTH; x++)
			for (int y = 0; y != GRID_HEIGHT; y++)
				tileList.add(new Floor(new Point2D(x, y)));
		gui.addImages(tileList);
	}

	private void addHero(Point2D position) {
		hero = new Hero(position);
		entities.add(entities.size(), hero);
		gui.addImage(hero);
	}

	private void addGreen(Point2D position) {
		hud = new GameHud(position, "Green");
		entities.add(entities.size(), hud);
		gui.addImage(hud);
	}

	private void addBlack(Point2D position) {
		hud = new GameHud(position, "Black");
		entities.add(entities.size(), hud);
		gui.addImage(hud);
	}

	private void charactersUpdate() {
		for (int i = 0; i != entities.size(); i++) {
			if (entities.get(i) instanceof Enemy && !(entities.get(i) instanceof Hero)) {
				Enemy enemy = (Enemy) entities.get(i);
				Point2D pos = enemy.move(hero.getPosition());
				int index = enemyCollision(pos);
				if (index == -1) {
					enemy.changePosition(pos);
					entities.remove(i);
					entities.add(i, enemy);
				} else {
					GameElement hero = enemy.attack(entities.get(index));
					entities.remove(index);
					entities.add(index, hero);
				}
			}
		}
		healthUpdate();
		if (hero.getHealth() <= 0) {
			// gui.setMessage("You're dead");
//			gui.removeImage((Hero)entities.get(0));
//			entities.remove(0);
		}
	}

	public int getTurn() {
		return this.turns;
	}

	@Override
	public void update(Observed source) {
		for (int h = 0; h != entities.size(); h++) {
			if (entities.get(h) instanceof Hero) {
				this.hero = (Hero) entities.get(h);
			}
		}

		int key = ((ImageMatrixGUI) source).keyPressed();

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT | key == KeyEvent.VK_RIGHT) {
			heroAction(key);
		}
		if (key == KeyEvent.VK_Q) {
			if (hero.getInventory().size() >= 1) {
				heroDrop(0);
			}
		}
		if (key == KeyEvent.VK_W) {
			if (hero.getInventory().size() >= 2) {
				heroDrop(1);
			}
		}
		if (key == KeyEvent.VK_E) {
			if (hero.getInventory().size() >= 3) {
				heroDrop(2);
			}
		}
		healthUpdate();
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns);
		gui.update();
	}

	public void heroAction(int key) {
		System.out.println(entities);
		Point2D pos = hero.keyCode(key);
		int index = collision(pos);
		if (index == -1) {
			hero.changePosition(pos);
		}else if(index ==  -2) {
			return;
		}else if (entities.get(index) instanceof Enemy) {
			GameElement entity = hero.attack(entities.get(index));
			if (entity instanceof Enemy) {
				Enemy enemy1 = (Enemy) entity;
				if (enemy1.getHealth() <= 0) {
					gui.removeImage(entities.get(index));
					entities.remove(index);
				} else {
					entities.remove(index);
					entities.add(index, entity);
				}
			}
		} else if (entities.get(index) instanceof Pickable) {
			hero.changePosition(pos);
			if (hero.pickUp(entities.get(index))) {
				hudUpdate();
				entities.remove(entities.get(index));
				System.out.println(entities);
//				gui.removeImage(entities.get(index));
			}
		} else if (entities.get(index) instanceof Door) {
			Door door = (Door) entities.get(index);
			if (door.getId() == null) {
				roomUpdate(door, index);
			} else {
				for (int i = 0; i != hero.getInventory().size(); i++) {
					if (hero.getInventory().get(i) instanceof Key) {
						Key Dkey = (Key) hero.getInventory().get(i);
						if (door.getId().equals(Dkey.getId())) {
							hero.drop(i);
							gui.removeImage(Dkey);
							hudUpdate();
							roomUpdate(door, index);
							break;
						}
					}
				}
			}
		}
		charactersUpdate();
		turns++;

	}

	public void roomUpdate(Door door, int index) {
		gui.removeImage(entities.get(index));
		entities.remove(index);
		entities.add(new Door(door.getPosition(), door.getRoom(), door.getSpawnPosition()));
		gui.addImage(entities.get(index));
		rooms.remove(currentRoom);
		rooms.add(currentRoom, this.entities);
		gui.clearImages();
		String[] id = door.getRoom().split("m");
		this.entities = rooms.get(Integer.parseInt(id[1]));
		currentRoom = Integer.parseInt(id[1]);
		addFloor();
		for (int w = 0; w != entities.size(); w++) {
			gui.addImage(entities.get(w));
		}
		hero.changePosition(door.getSpawnPosition());
		entities.add(hero);
		gui.addImage(hero);
		startHud();
		hudUpdate();
		healthUpdate();
		gui.update();
	}

	public void heroDrop(int i) {
		GameElement item = (GameElement) hero.getInventory().get(i);
		Point2D pos = new Point2D(0, 0);
		if (itemCollision(hero.getGamePosition()) == -1) {
			pos = (hero.getGamePosition());
		} else if (itemCollision(hero.getGamePosition().plus(new Vector2D(1, 0))) == -1) {
			pos = (hero.getGamePosition().plus(new Vector2D(1, 0)));
		} else if (itemCollision(hero.getGamePosition().plus(new Vector2D(0, -1))) == -1) {
			pos = (hero.getGamePosition().plus(new Vector2D(0, -1)));
		} else if (itemCollision(hero.getGamePosition().plus(new Vector2D(-1, 0))) == -1) {
			pos = (hero.getGamePosition().plus(new Vector2D(-1, 0)));
		}
		if (!(item instanceof HealthPotion)) {
			item.changePosition(pos);
			entities.add(item);
			hero.drop(i);
		} else {
			item.changePosition(new Point2D(4, 4));
			entities.remove(item);
			gui.removeImage(item);
			hero.drop(i);
		}
		hudUpdate();
	}

	public int collision(Point2D position) {
		if(position.getX()>10 ||position.getX()<0 || position.getY()>10 || position.getY()<0) {
			return -2;
		}
		for (int i = 0; i != entities.size(); i++) {
			if (position.equals(entities.get(i).getGamePosition())) {
				return i;
			}
		}
		return -1;
	}

	public int enemyCollision(Point2D position) {
		for (int i = 0; i != entities.size(); i++) {
			if (position.equals(entities.get(i).getGamePosition()) && !(entities.get(i) instanceof Pickable)) {
				return i;
			}
		}
		return -1;
	}

	public int itemCollision(Point2D position) {
		for (int i = 0; i != entities.size(); i++) {
			if (!(entities.get(i) instanceof Hero)) {
				if (position.equals(entities.get(i).getGamePosition())) {
					return i;
				}
			}
		}
		return -1;
	}

	public void healthUpdate() {
		Hero hero = new Hero(new Point2D(-1, -1));
		for (int h = 0; h != entities.size(); h++) {
			if (entities.get(h) instanceof Hero) {
				hero = (Hero) entities.get(h);
			}
		}
		for (int i = 0; i != entities.size(); i++) {
			;
			if (entities.get(i) instanceof GameHud) {
				Point2D pos = entities.get(i).getGamePosition();
				int health = hero.getHealth();
				for (int j = 5; j >= (health / 2); j--) {
					if (j >= 0) {
						if (pos.equals(new Point2D(4 - j, 10))) {
							healthSuport(i, pos, "Red");
						}
					}
				}
				for (int j = 0; j < (health / 2); j++) {
					if (pos.equals(new Point2D(4 - j, 10))) {
						healthSuport(i, pos, "Green");
					}
				}
				if ((health % 2) == 1) {
					if (pos.equals(new Point2D(4 - health / 2, 10))) {
						healthSuport(i, pos, "RedGreen");
					}
				}
			}
		}
	}

	public void healthSuport(int i, Point2D pos, String colour) {
		gui.removeImage(entities.get(i));
		entities.remove(i);
		GameHud Color = new GameHud(pos, colour);
		entities.add(i, Color);
		gui.addImage(Color);
	}
	
	public void hudClear() {
		for(int i=0;i!=3;i++) {
		for (int j = 0; j != entities.size(); j++) {
			if (entities.get(j).getGamePosition().equals(new Point2D(i + 7, 10))) {
				if (!(entities.get(j) instanceof GameHud)) {
					gui.removeImage(entities.get(j));
					entities.remove(j);
					break;
					}
				}
			}
		}
		}
	
	public void hudUpdate() {
		hudClear();
		for (int i = 0; i != hero.getInventory().size(); i++) {
			hudSupport(i);
		}
	}

	public void hudSupport(int i) {
		GameElement item = (GameElement) hero.getInventory().get(i);
		item.changePosition(new Point2D(i + 7, 10));
		gui.addImage(item);
	}

	public void startHud() {
		addGreen(new Point2D(0, 10));
		addGreen(new Point2D(1, 10));
		addGreen(new Point2D(2, 10));
		addGreen(new Point2D(3, 10));
		addGreen(new Point2D(4, 10));
		addBlack(new Point2D(5, 10));
		addBlack(new Point2D(6, 10));
		addBlack(new Point2D(7, 10));
		addBlack(new Point2D(8, 10));
		addBlack(new Point2D(9, 10));
	}
}