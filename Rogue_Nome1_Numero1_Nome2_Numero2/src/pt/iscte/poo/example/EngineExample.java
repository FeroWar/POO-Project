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
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private static EngineExample INSTANCE = null;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
	
	private Hero hero;
	private Skeleton skeleton;
	private Bat bat;
	private Thug thug;
	private Wall wall;
	private List<GameElement> entities;
	private List<ObjectHealth> enemies;
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
		entities=new ArrayList<>();
		addFloor();
		addHero(new Point2D(4,4));
		addSkeleton(new Point2D(1,1));
		addBat(new Point2D(4,8));
		addThug(new Point2D(8,3));
		addWall(new Point2D(0,0));
		addWall(new Point2D(0,1));
		addWall(new Point2D(0,2));
		addWall(new Point2D(0,3));
		addWall(new Point2D(0,4));
		addWall(new Point2D(0,5));
		addWall(new Point2D(0,6));
		addWall(new Point2D(0,7));
		addWall(new Point2D(0,8));
		addWall(new Point2D(0,9));
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns);
		gui.update();
	}
	
	private void addFloor() {
		List<ImageTile> tileList = new ArrayList<>();
		for (int x=0; x!=GRID_WIDTH; x++)
			for (int y=0; y!=GRID_HEIGHT; y++)
				tileList.add(new Floor(new Point2D(x,y)));
		gui.addImages(tileList);
	}
	
	private void addHero(Point2D position) {
		hero = new Hero(position,0,enemies.size(),10,1);
		enemies.add(enemies.size(),hero);
		entities.add(entities.size(),hero);
		gui.addImage(hero);
	}
	private void addWall(Point2D position) {
		wall=new Wall(position,0);
		entities.add(entities.size(),wall);
		gui.addImage(wall);
	}
	private void addSkeleton(Point2D position){
		skeleton = new Skeleton(position,0,enemies.size(),10,1);
		enemies.add(enemies.size(),skeleton);
		entities.add(entities.size(),skeleton);
		gui.addImage(skeleton);
	}
	private void addBat(Point2D position) {
		bat = new Bat(position,0,enemies.size(),10,1);
		enemies.add(enemies.size(),bat);
		entities.add(entities.size(),bat);
		gui.addImage(bat);
	}
	private void addThug(Point2D position) {
		thug = new Thug(position,0,enemies.size(),10,1);
		enemies.add(enemies.size(),thug);
		entities.add(entities.size(),thug);
		gui.addImage(thug);
	}
	
	private void charactersUpdate (int turns) {
		skeleton.move(hero.getPosition(),entities);
		bat.move(hero.getPosition(),entities);
		thug.move(hero.getPosition(),entities);
	}
	
	@Override
	public void update(Observed source) {
		
		int key = ((ImageMatrixGUI) source).keyPressed();
		
		if (key == KeyEvent.VK_DOWN) {		
			hero.keyCode(key,entities,enemies);
			charactersUpdate(turns);
			turns++;
		}
		if (key == KeyEvent.VK_UP) {		
			hero.keyCode(key,entities,enemies);
			charactersUpdate(turns);
			turns++;
		}
		if (key == KeyEvent.VK_LEFT) {		
			hero.keyCode(key,entities,enemies);
			charactersUpdate(turns);
			turns++;
		}
		if (key == KeyEvent.VK_RIGHT) {		
			hero.keyCode(key,entities,enemies);
			charactersUpdate(turns);
			turns++;
		}
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns);
		gui.update();
	}
}
