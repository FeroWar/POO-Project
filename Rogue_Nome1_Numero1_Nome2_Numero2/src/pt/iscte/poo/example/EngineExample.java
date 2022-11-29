package pt.iscte.poo.example;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Point2D;
import java.awt.event.KeyEvent;

public class EngineExample implements Observer {

	public static final int GRID_HEIGHT = 11;
	public static final int GRID_WIDTH = 10;
	public static final int numberOfRooms = 4;

	private static EngineExample INSTANCE = null;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

	private Hero hero;
	private GameHud hud;
	private List<List<GameElement>> rooms;
	private int turns;
	private int currentRoom;
	private String PlayerName;
	private int score;
	private Door lastDoorSaved;
	private	Hero heroSaved;

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
		turns=0;
		score=0;
		PlayerName=gui.askUser("What is your UserName?");
		rooms = new ArrayList<>();
		for (int i = 0; i != numberOfRooms; i++) {
			rooms.add(i, new Room("room" + i).getList());
		}
		addFloor();
		currentRoom = 0;
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			gui.addImage(rooms.get(currentRoom).get(i));
		}
		startHud();
		addHero(new Point2D(1,1));
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns+" - "+PlayerName+":"+score);
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
		gui.addImage(hero);
	}

	private void addGreen(Point2D position) {
		hud = new GameHud(position, "Green");
		rooms.get(currentRoom).add(rooms.get(currentRoom).size(), hud);
		gui.addImage(hud);
	}

	private void addBlack(Point2D position) {
		hud = new GameHud(position, "Black");
		rooms.get(currentRoom).add(rooms.get(currentRoom).size(), hud);
		gui.addImage(hud);
	}

	private void charactersUpdate() { // put in each enemy
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			if (rooms.get(currentRoom).get(i) instanceof Enemy && !(rooms.get(currentRoom).get(i) instanceof Hero)) {
				Enemy enemy = (Enemy) rooms.get(currentRoom).get(i);
				Point2D pos = enemy.move(hero.getPosition());
				int index = enemyCollision(pos);
				if (index == -1) {
					enemy.changePosition(pos);
					rooms.get(currentRoom).remove(i);
					rooms.get(currentRoom).add(i, enemy);
				} else if (index == -2) {
					enemy.attack(hero);
					score-=10;
					if(rooms.get(currentRoom).get(i) instanceof Scorpion) {
						hero.setPoison(hero.getPoison()+1);
					}
				}
			}
		}
		hero.getHit(hero.getPoison());
		healthUpdate();
		if (hero.getHealth() <= 0) {
			Scoreboard.teste(PlayerName,score);
			if(gui.askUser("You Died, Want to lo?").equals("y")){
				rooms.remove(currentRoom);
				rooms.add(currentRoom,new Room("room" + currentRoom).getList());
				hero=new Hero(new Point2D(4,4));
				System.out.println(hero.getHealth());
				System.out.println(heroSaved.getHealth());
				hero=heroSaved;
				System.out.println(hero.getHealth());
				loadSave(lastDoorSaved);
			}	
			else {
				gui.dispose();
				System.exit(0);
				}
		}
	}

	public int getTurn() {
		return this.turns;
	}

	@Override
	public void update(Observed source) {

		int key = ((ImageMatrixGUI) source).keyPressed();

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT | key == KeyEvent.VK_RIGHT) {
			heroAction(key);
			turns++;
			charactersUpdate();
		}
		if (key == KeyEvent.VK_1 ||key == KeyEvent.VK_2 || key == KeyEvent.VK_3) {
				hero.drop(key-49);
		}
		if (key == KeyEvent.VK_Q) { 
				hero.use(0);
		}
		if (key == KeyEvent.VK_W) {
				hero.use(1);
		}
		if (key == KeyEvent.VK_E) {
				hero.use(2);
		}
		healthUpdate();
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns+" - "+PlayerName+":"+score);
		gui.update();
	}

	public void heroAction(int key) {
		Point2D pos = hero.keyCode(key);
		int index = collision(pos);
		if (index == -1) {
			hero.changePosition(pos);	
		}	
		else if(index ==  -3) {
			gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns+" - "+PlayerName+":"+score);
			Scoreboard.teste(PlayerName,score);
			if(gui.askUser("You Won with "+score+" points, Want to restart?").equals("y")){
			gui.clearImages();
			EngineExample.getInstance().start();
			}else {
				gui.dispose();
				System.exit(0);
				}
		}else if(index ==  -2) {
			return;
		}else if (rooms.get(currentRoom).get(index) instanceof Enemy) {
			hero.heroEnemy((Enemy)rooms.get(currentRoom).get(index),index);
		} else if (rooms.get(currentRoom).get(index) instanceof Pickable) {
			hero.changePosition(pos);
			hero.heroPickable(rooms.get(currentRoom).get(index));
		} else if (rooms.get(currentRoom).get(index) instanceof Door) {
			hero.heroDoor((Door)rooms.get(currentRoom).get(index), index);
		}

	}

	public void roomUpdate(Door door, int index) {
		heroSaved=(Hero)hero.clone();
		gui.removeImage(rooms.get(currentRoom).get(index));
		rooms.get(currentRoom).remove(index);
		rooms.get(currentRoom).add(new Door(door.getPosition(), door.getRoom(), door.getSpawnPosition()));
		gui.addImage(rooms.get(currentRoom).get(index));
		gui.clearImages();
		String[] id = door.getRoom().split("m");
		currentRoom = Integer.parseInt(id[1]);
		addFloor();
		for (int w = 0; w != rooms.get(currentRoom).size(); w++) {
			gui.addImage(rooms.get(currentRoom).get(w));
		}
		hero.changePosition(door.getSpawnPosition());
		rooms.get(currentRoom).add(hero);
		gui.addImage(hero);
		startHud();
		hudUpdate();
		healthUpdate();
		gui.update();
	}
	public void loadSave(Door door) {
		gui.clearImages();
		String[] id = door.getRoom().split("m");
		currentRoom = Integer.parseInt(id[1]);
		addFloor();
		for (int w = 0; w != rooms.get(currentRoom).size(); w++) {
			gui.addImage(rooms.get(currentRoom).get(w));
		}
		gui.addImage(hero);
		startHud();
		hudUpdate();
		healthUpdate();
		gui.update();
	}
	public int collision(Point2D position) {
		if(position.getX()>10 ||position.getX()<0 || position.getY()>10 || position.getY()<0) {
			return -2;
		}
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			if (position.equals(rooms.get(currentRoom).get(i).getGamePosition())) {
				if(rooms.get(currentRoom).get(i) instanceof Treasure) {
					return -3;
				}
				return i;
			}
		}
		return -1;
	}

	public int enemyCollision(Point2D position) {
		if(hero.getGamePosition().equals(position)) {
			return -2;
		}
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			if (position.equals(rooms.get(currentRoom).get(i).getGamePosition()) && !(rooms.get(currentRoom).get(i) instanceof Pickable)) {
				return i;
			}
		}
		return -1;
	}

	public int itemCollision(Point2D position) {
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			if (!(rooms.get(currentRoom).get(i) instanceof Hero)) {
				if (position.equals(rooms.get(currentRoom).get(i).getGamePosition())) {
					return i;
				}
			}
		}
		return -1;
	}

	public void healthUpdate() {
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			if (rooms.get(currentRoom).get(i) instanceof GameHud) {
				Point2D pos = rooms.get(currentRoom).get(i).getGamePosition();
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
		gui.removeImage(rooms.get(currentRoom).get(i));
		rooms.get(currentRoom).remove(i);
		GameHud Color = new GameHud(pos, colour);
		rooms.get(currentRoom).add(i, Color);
		gui.addImage(Color);
	}
	
	public void hudClear() {
		for(int i=0;i!=3;i++) {
		for (int j = 0; j != rooms.get(currentRoom).size(); j++) {
			if (rooms.get(currentRoom).get(j).getGamePosition().equals(new Point2D(i + 7, 10))) {
				if (!(rooms.get(currentRoom).get(j) instanceof GameHud)) {
					gui.removeImage(rooms.get(currentRoom).get(j));
					rooms.get(currentRoom).remove(j);
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
	public void guiAdd(GameElement image) {
		gui.addImage(image);
	}
	public void guiRemove(GameElement image) {
		gui.removeImage(image);
	}
	public void addToRoom(GameElement a) {
		rooms.get(currentRoom).add(a);
	}
	public void removeFromRoom(GameElement a) {
		rooms.get(currentRoom).remove(a);
	}
	public void addToRoomIndex(int index,GameElement a) {
		rooms.get(currentRoom).add(index,a);
	}
	public void removeFromRoomIndex(int index) {
		rooms.get(currentRoom).remove(index);
	}
	public GameElement roomIndex(int index) {
		return rooms.get(currentRoom).get(index);
	}
	public void addScore(int score) {
		this.score+=score;
	}
	public void setSaveDoor(Door door) {
		lastDoorSaved=door;
	}
}