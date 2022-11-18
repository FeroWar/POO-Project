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

	private static EngineExample INSTANCE = null;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();

	private Hero hero;
	private Skeleton skeleton;
	private Bat bat;
	private Thug thug;
	private Wall wall;
	private GameHud hud;
	private List<GameElement> entities;
	private int turns;

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
		addFloor();
		addHero(new Point2D(4, 4));
		addSkeleton(new Point2D(1, 1));
		addBat(new Point2D(4, 8));
		addThug(new Point2D(8, 3));
		addWall(new Point2D(0, 0));
		addWall(new Point2D(0, 1));
		addWall(new Point2D(0, 2));
		addWall(new Point2D(0, 3));
		addWall(new Point2D(0, 4));
		addWall(new Point2D(0, 5));
		addWall(new Point2D(0, 6));
		addWall(new Point2D(0, 7));
		addWall(new Point2D(0, 8));
		addWall(new Point2D(0, 9));
		addGreen(new Point2D(0, 10));
		addGreen(new Point2D(1, 10));
		addGreen(new Point2D(2, 10));
		addGreen(new Point2D(3, 10));
		addGreen(new Point2D(4, 10));
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
		hud = new GameHud(position,"Green");
		entities.add(entities.size(), hud);
		gui.addImage(hud);
	}
	private void addRed(Point2D position) {
		hud = new GameHud(position,"Red");
		entities.add(entities.size(), hud);
		gui.addImage(hud);
	}
	private void addRedGreen(Point2D position) {
		hud = new GameHud(position,"RedGreen");
		entities.add(entities.size(), hud);
		gui.addImage(hud);
	}
	private void addWall(Point2D position) {
		wall = new Wall(position);
		entities.add(entities.size(), wall);
		gui.addImage(wall);
	}

	private void addSkeleton(Point2D position) {
		skeleton = new Skeleton(position);
		entities.add(entities.size(), skeleton);
		gui.addImage(skeleton);
	}

	private void addBat(Point2D position) {
		bat = new Bat(position);
		entities.add(entities.size(), bat);
		gui.addImage(bat);
	}

	private void addThug(Point2D position) {
		thug = new Thug(position);
		entities.add(entities.size(), thug);
		gui.addImage(thug);
	}

//	Public Static AbstractObject Create(String type, Point2D p) {
//	    if(type.equals("Bat")) {
//	        return new Bat(p);
//	    }else if(type.equals("Skeleton") {
//			return new Skeleton(p);
//	    }
//	}
	private void charactersUpdate() {
		for (int i = 1; i != entities.size(); i++) { // tivemos que impedir o heroi de ser percorrido pela funcao para
		// ele não dar dano a si mesmo de cada vez que andava.
			if (entities.get(i) instanceof Enemy) {
				Enemy enemy = (Enemy) entities.get(i);
				Point2D pos = enemy.move(hero.getPosition());
				int index = collision(pos);
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
		Hero hero = (Hero) entities.get(0);
		if(hero.getHealth()<=0) {
			gui.setMessage("You're dead");
//			gui.removeImage((Hero)entities.get(0));
//			entities.remove(0);
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
		}
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns);
		gui.update();
	}
	
	public void heroAction(int key) {
		Point2D pos = hero.keyCode(key);
		int index = collision(pos);
		if (index == -1) {
			hero.changePosition(pos);
		} else {
			GameElement enemy = hero.attack(entities.get(index));
			entities.remove(index);
			entities.add(index, enemy);
		}
		charactersUpdate();
		turns++;
	}
	public void healthUpdate () {
		for (int i = 1; i != entities.size(); i++) {;
			if(entities.get(i) instanceof GameHud) {
				Point2D pos=entities.get(i).getGamePosition();
				
				Hero hero = (Hero) entities.get(0);
				int health=hero.getHealth();
				for(int j=5;j>=(health/2);j--) {
					if(pos.equals(new Point2D(4-j,10))) {
						healthSuport(i,pos,"Red");
					}
				}
				for(int j=0;j<(health/2);j++) {
					if(pos.equals(new Point2D(4-j,10))) {
						healthSuport(i,pos,"Green");
					}
				}
				if((health%2)==1) {
					if(pos.equals(new Point2D(4-health/2,10))) {
						healthSuport(i,pos,"RedGreen");
					}
				}
				}
			}
	}
	public void healthSuport(int i,Point2D pos,String colour) {
		gui.removeImage((GameHud)entities.get(i));
		entities.remove(i);
		GameHud Color=new GameHud(pos,colour);
		entities.add(i,Color);
		gui.addImage(Color);
	}

	public int collision(Point2D position) {
		for (int i = 0; i != entities.size(); i++) {
			if (position.equals(entities.get(i).getGamePosition())) {
				return i;
			}
		}
		return -1;
	}
}
