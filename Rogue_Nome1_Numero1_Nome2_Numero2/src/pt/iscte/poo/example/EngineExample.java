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
	private GameElement item;
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
		Room m=new Room("room0");
		this.entities=m.getList();
		startHud();
		addHero(new Point2D(4, 4));
		addHealthPotion(new Point2D(3, 4));
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
	private void addHealthPotion(Point2D position) {
		item = new HealthPotion(position);
		entities.add(item);
		gui.addImage(item);
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
			heroDrop(0);
		}
		}
		if(key == KeyEvent.VK_W) {
			if(hero.getInventory().size()>=2) {
			heroDrop(1);
		}
		}
		if(key == KeyEvent.VK_E) {
			if(hero.getInventory().size()>=3) {
			heroDrop(2);
		}
		}
		hudUpdate();
		healthUpdate();
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns);
		gui.update();
	}
	
	public void heroAction(int key) {
		Point2D pos = hero.keyCode(key);
		int index = collision(pos);
		if (index == -1) {
			hero.changePosition(pos);
		} else if(entities.get(index) instanceof Enemy){
			GameElement entity=hero.attack(entities.get(index));
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
		}else if(entities.get(index) instanceof Pickable) {
			hero.changePosition(pos);
			if(hero.pickUp(entities.get(index))) {
				hudUpdate();
				gui.removeImage(entities.get(index));
				entities.remove(index);
		}
		}else if(entities.get(index) instanceof Door) {
			for(int i=0;i!=hero.getInventory().size();i++) {
				if(hero.getInventory().get(i) instanceof Key) {
				Key Dkey=(Key)hero.getInventory().get(i);
				Door door=(Door)entities.get(index);
				if(door.getId()==null){
					for (int j = 0; j != entities.size(); j++) {
						gui.removeImage(entities.get(j));
						gui.update();
					}
					Room m=new Room(door.getRoom());
					this.entities=m.getList();
					hero.changePosition(door.getSpawnPosition());
					entities.add(hero);
					gui.addImage(hero);
					gui.update();
					startHud();
					hudUpdate();
					healthUpdate();
				}else if(door.getId().equals(Dkey.getId())) {
					for (int j = 0; j != entities.size(); j++) {
						gui.removeImage(entities.get(j));
						gui.update();
					}
					Room m=new Room(door.getRoom());
					this.entities=m.getList();
					hero.changePosition(door.getSpawnPosition());
					entities.add(hero);
					gui.addImage(hero);
					gui.update();
					startHud();
					hudUpdate();
					healthUpdate();
				}
			}
		}
		}
		charactersUpdate();
		turns++;
	}
	public void heroDrop(int i) {
		GameElement item=(GameElement)hero.getInventory().get(i);
		Point2D pos=new Point2D(0,0);
		if(itemCollision(hero.getGamePosition())==-1) {
			pos=(hero.getGamePosition());
		}else if(itemCollision(hero.getGamePosition().plus(new Vector2D(1,0)))==-1){
			pos=(hero.getGamePosition().plus(new Vector2D(1,0)));
		}else if(itemCollision(hero.getGamePosition().plus(new Vector2D(0,-1)))==-1){
			pos=(hero.getGamePosition().plus(new Vector2D(0,-1)));
		}else if(itemCollision(hero.getGamePosition().plus(new Vector2D(-1,0)))==-1){
			pos=(hero.getGamePosition().plus(new Vector2D(-1,0)));
		}
		if(!(item instanceof HealthPotion)) {
			item.changePosition(pos);
			entities.add(item);
			gui.addImage(item);
			hero.drop(i);
		}else {
			item.changePosition(new Point2D(-1,-1));
			entities.remove(item);
			gui.removeImage(item);
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
			if(!(entities.get(i) instanceof Hero)) {
			if (position.equals(entities.get(i).getGamePosition())) {
				return i;
			}
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
					if(j>=0) {
					if(pos.equals(new Point2D(4-j,10))) {
						healthSuport(i,pos,"Red");
					}
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
