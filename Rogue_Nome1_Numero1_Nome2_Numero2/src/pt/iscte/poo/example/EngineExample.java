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
	private Sword sword;
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
		Room m=new Room(0);
		this.entities=m.getList();
		startHud();
		addHero(new Point2D(4, 4));
		addSword(new Point2D(3,4));
		addSword(new Point2D(6,4));
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
	private void addSword(Point2D position) {
		sword = new Sword(position);
		entities.add(sword);
		gui.addImage(sword);
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
	private void addBlack(Point2D position) {
		hud = new GameHud(position,"Black");
		entities.add(entities.size(), hud);
		gui.addImage(hud);
	}
	private void charactersUpdate() {
		for (int i = 0; i != entities.size(); i++) { // tivemos que impedir o heroi de ser percorrido pela funcao para
		// ele não dar dano a si mesmo de cada vez que andava.
			if (entities.get(i) instanceof Enemy && !(entities.get(i) instanceof Hero)) {
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
		hudUpdate();
		healthUpdate();
		if(hero.getHealth()<=0) {
			//gui.setMessage("You're dead");
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
			if(entities.get(h) instanceof Hero) {
				this.hero = (Hero) entities.get(h);
			}
	}

		int key = ((ImageMatrixGUI) source).keyPressed();

		if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT | key == KeyEvent.VK_RIGHT) {
			heroAction(key);
		}
		if(key == KeyEvent.VK_Q) {
			if(hero.getInventory().size()>=1) {
			GameElement item=(GameElement)hero.getInventory().get(0);
			heroDrop(item,0);
		}
		}
		if(key == KeyEvent.VK_W) {
			hero.drop(1);
		}
		if(key == KeyEvent.VK_E) {
			hero.drop(2);
		}
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns);
		gui.update();
	}
	
	public void heroAction(int key) {
		Point2D pos = hero.keyCode(key);
		int index = collision(pos);
		if (index == -1 || entities.get(index) instanceof Pickable) {
			hero.changePosition(pos);
			if(index!=-1) {
				if(hero.pickUp(entities.get(index))) {
					hudUpdate();
					gui.removeImage(entities.get(index));
					entities.remove(index);
			}
			}
		} else {
			GameElement entity =hero.attack(entities.get(index));
			if(entity instanceof Enemy) {
			Enemy enemy1=(Enemy)entity;
			if(enemy1.getHealth()<=0) {
			gui.removeImage(entities.get(index));
			entities.remove(index);
			}else {
				entities.remove(index);
				entities.add(index, entity);
			}
			}
		}
		charactersUpdate();
		turns++;
	}
	public void heroDrop(GameElement item,int i) {
		if(itemCollision(hero.getGamePosition())==-1) {
			System.out.println(itemCollision(hero.getGamePosition())+"1");
			item.changePosition(hero.getGamePosition());
			entities.add(item);
			gui.addImage(item);
			hero.drop(i);
		}else if(itemCollision(hero.getGamePosition().plus(new Vector2D(-1,0)))==-1){
			System.out.println(itemCollision(hero.getGamePosition().plus(new Vector2D(-1,0)))+"2");
			item.changePosition(hero.getGamePosition().plus(new Vector2D(1,0)));
			entities.add(item);
			gui.addImage(item);
			hero.drop(i);
		}else if(itemCollision(hero.getGamePosition().plus(new Vector2D(0,-1)))==-1){
			System.out.println(itemCollision(hero.getGamePosition().plus(new Vector2D(0,-1)))+"3");
			item.changePosition(hero.getGamePosition().plus(new Vector2D(0,-1)));
			entities.add(item);
			gui.addImage(item);
			hero.drop(i);
		}else if(itemCollision(hero.getGamePosition().plus(new Vector2D(1,0)))==-1){
			System.out.println(itemCollision(hero.getGamePosition().plus(new Vector2D(1,0)))+"4");
			item.changePosition(hero.getGamePosition().plus(new Vector2D(-1,0)));
			entities.add(item);
			gui.addImage(item);
			hero.drop(i);
		}
	}
	public int collision(Point2D position) {
		for (int i = 0; i != entities.size(); i++) {
			if (position.equals(entities.get(i).getGamePosition())) {
				return i;
			}
		}
		return -1;
	}
	public int itemCollision(Point2D position) {
		for (int i = 0; i != entities.size(); i++) {
			if (!(entities.get(i) instanceof Hero) && position.equals(entities.get(i).getGamePosition())) {
				return i;
			}
		}
		return -1;
	}
	
	public void healthUpdate () {
		Hero hero=new Hero(new Point2D(4,4));
		for (int h = 0; h != entities.size(); h++) {
			if(entities.get(h) instanceof Hero) {
				hero = (Hero) entities.get(h);
			}
	}
		for (int i = 0; i != entities.size(); i++) {;
			if(entities.get(i) instanceof GameHud) {
				Point2D pos=entities.get(i).getGamePosition();
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
		gui.removeImage(entities.get(i));
		entities.remove(i);
		GameHud Color=new GameHud(pos,colour);
		entities.add(i,Color);
		gui.addImage(Color);
	}
	public void hudUpdate () {
		for(int i=0;i!=3;i++) {
			hudSupport(i);
		}
	}
	public void hudSupport(int i) {
		List<Pickable> inventory = hero.getInventory();
		if (inventory.size() > i) {
			GameElement item = (GameElement) inventory.get(i);
			item.changePosition(new Point2D(i+7, 10));
			entities.add(item);
			gui.addImage(item);
		}
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
