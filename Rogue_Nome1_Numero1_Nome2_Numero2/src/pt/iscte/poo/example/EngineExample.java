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
	public static final int NUMBER_OF_ROOMS = 4;

	private static EngineExample INSTANCE = null;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

	private Hero hero;
	private GameHud hud;
	private List<List<GameElement>> rooms;
	private int turns;
	private int currentRoom;
	private String playerName;
	private int score;

	private Door lastDoorSaved;
	private Hero heroSaved;
	private ArrayList<Pickable> heroInventorySaved;
	private int scoreSaved;
	private int turnsSaved;

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
		turns = 0;
		score = 0;
		playerName = gui.askUser("What is your UserName?");
		rooms = new ArrayList<>();
		for (int i = 0; i != NUMBER_OF_ROOMS; i++) {
			rooms.add(i, new Room("room" + i).getList());
		}
		addFloor();
		currentRoom = 0;
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			gui.addImage(rooms.get(currentRoom).get(i));
		}
		startHud();
		addHero(new Point2D(1, 1));
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns + " - " + playerName + ":" + score);
		gui.update();
	}

	public void addFloor() {
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

	private void charactersUpdate() {
		hero.getHit(hero.getPoison());
		if (hero.getHealth() <= 0) {
			hero.zeroHp();
		}
		for (int i = 0; i != rooms.get(currentRoom).size(); i++) {
			if (rooms.get(currentRoom).get(i) instanceof Enemy && !(rooms.get(currentRoom).get(i) instanceof Hero)) {
				Enemy enemy = (Enemy) rooms.get(currentRoom).get(i);
				enemy.update(i);
			}
		}
	}
	@Override
	public void update(Observed source) {
		int key = ((ImageMatrixGUI) source).keyPressed();
		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT | key == KeyEvent.VK_RIGHT) {
			hero.action(key);
			turns++;
			charactersUpdate();
		}
		if (key == KeyEvent.VK_1 || key == KeyEvent.VK_2 || key == KeyEvent.VK_3) {
			hero.drop(key - 49);
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
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns + " - " + playerName + ":" + score);
		gui.update();
	}
	public void startHud() {
		for (int i = 0; i != 5; i++) {
			addGreen(new Point2D(i, 10));
			addBlack(new Point2D(5 + i, 10));
		}
	}
	public ImageMatrixGUI getGui() {
		return gui;
	}
	public List<GameElement> getCurrentRoom() {
		return rooms.get(currentRoom);
	}
	public int getRoomNumber() {
		return currentRoom;
	}
	public void removeRoom(int i) {
		rooms.remove(i);
	}
	public void addRoom(int i,List<GameElement> list) {
		rooms.add(i,list);
	}
	public void addScore(int score) {
		this.score += score;
	}
	public void setSaveDoor(Door door) {
		lastDoorSaved = door;
	}
	public void setSavedHero(Hero hero) {
		heroSaved=hero;
	}
	public void setSavedInventory(ArrayList<Pickable> list) {
		heroInventorySaved=list;
	}
	public void setSavedTurns(int i) {
		turnsSaved=i;
	}
	public void setSavedScore(int i) {
		scoreSaved=i;
	}
	public Hero getSavedHero() {
		return heroSaved;
	}
	public List<Pickable> getSavedInventory() {
		return heroInventorySaved;
	}
	public Hero getHero() {
		return hero;
	}
	public void setHero(Hero hero) {
		this.hero=hero;
	}
	public int getTurn() {
		return this.turns;
	}
	public String getPlayerName() {
		return playerName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int i) {
		score=i;
	}
	public void setTurns(int i) {
		turns=i;
	}
	public int getSavedScore() {
		return scoreSaved;
	}
	public int getSavedTurns() {
		return turnsSaved;
	}
	public void setCurrentRoom(int i) {
		currentRoom=i;
	}
	public GameHud getHud() {
		return hud;
	}
	public Door getSavedDoor() {
		return lastDoorSaved;
	}
}