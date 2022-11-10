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
		hero = new Hero(position);
		entities.add(entities.size(),hero);
		gui.addImage(hero);
	}
	private void addWall(Point2D position) {
		wall=new Wall(position);
		entities.add(entities.size(),wall);
		gui.addImage(wall);
	}
	private void addSkeleton(Point2D position){
		skeleton = new Skeleton(position);
		entities.add(entities.size(),skeleton);
		gui.addImage(skeleton);
	}
	private void addBat(Point2D position) {
		bat = new Bat(position);
		entities.add(entities.size(),bat);
		gui.addImage(bat);
	}
	private void addThug(Point2D position) {
		thug = new Thug(position);
		entities.add(entities.size(),thug);
		gui.addImage(thug);
	}

	private void charactersUpdate (int turns) {
		for(int i=0;i!=entities.size();i++){
			if(turns%2==0) {
				if(entities.get(i) instanceof Skeleton) {
					Skeleton skel=(Skeleton)entities.get(i);
					Point2D pos=skel.move(hero.getPosition());
					int index=collision(pos);
					if(index==-1) {
						skel.changePosition(pos);
						entities.remove(i);
						entities.add(i,skel);
					}else {
						GameElement hero=skel.attack(entities.get(index));
//						if(hero instanceof Hero) {
//							Hero heroi=(Hero) hero;
//						if(heroi.getHealth()>0) {
						entities.remove(index);
						entities.add(index,hero);
//						}else {
//							entities.remove(index);
//						}
//						}
					}
				}
			}
			if(entities.get(i) instanceof Bat) {
				Bat bat=(Bat)entities.get(i);
				Point2D pos=bat.move(hero.getPosition());
				int index=collision(pos);
				if(index==-1) {
					bat.changePosition(pos);
					entities.remove(i);
					entities.add(i,bat);
				}else {
					GameElement hero=bat.attack(entities.get(index));
//					if(hero instanceof Hero) {
//						Hero heroi=(Hero) hero;
//					if(heroi.getHealth()>0) {
					entities.remove(index);
					entities.add(index,hero);
//					}else {
//						entities.remove(index);
//					}
//					}
				}
			}
			if(entities.get(i) instanceof Thug) {
				Thug thug=(Thug)entities.get(i);
				Point2D pos=thug.move(hero.getPosition());
				int index=collision(pos);
				if(index==-1) {
					thug.changePosition(pos);
					entities.remove(i);
					entities.add(i,thug);
				}else {
					GameElement hero=thug.attack(entities.get(index));
//					if(hero instanceof Hero) {
//						Hero heroi=(Hero) hero;
//					if(heroi.getHealth()>0) {
					entities.remove(index);
					entities.add(index,hero);
//					}else {
//						entities.remove(index);
//					}
//					}
				}
			}
			}
	}
	@Override
	public void update(Observed source) {
		
		int key = ((ImageMatrixGUI) source).keyPressed();
		
		if (key == KeyEvent.VK_DOWN) {
			Point2D pos=hero.keyCode(key);
			int index=collision(pos);
			if(index==-1) {
				hero.changePosition(pos);
			}else {
				GameElement enemy=hero.attack(entities.get(index));
//				if(enemy instanceof Enemy) {
//				Enemy ene=(Enemy) enemy;
//			if(ene.getHealth()>0) {
			entities.remove(index);
			entities.add(index,enemy);
//			}else {
//				entities.remove(index);
//			}
//			}
			}
			charactersUpdate(turns);
			turns++;
		}
		if (key == KeyEvent.VK_UP) {	
			Point2D pos=hero.keyCode(key);
			int index=collision(pos);
			if(index==-1) {
				hero.changePosition(pos);
			}else {
				GameElement enemy=hero.attack(entities.get(index));
//				if(enemy instanceof Enemy) {
//				Enemy ene=(Enemy) enemy;
//			if(ene.getHealth()>0) {
			entities.remove(index);
			entities.add(index,enemy);
//			}else {
//				entities.remove(index);
//			}
//			}
			}
			charactersUpdate(turns);
			turns++;
		}
		if (key == KeyEvent.VK_LEFT) {
			Point2D pos=hero.keyCode(key);
			int index=collision(pos);
			if(index==-1) {
				hero.changePosition(pos);
			}else {
				GameElement enemy=hero.attack(entities.get(index));
//				if(enemy instanceof Enemy) {
//				Enemy ene=(Enemy) enemy;
//			if(ene.getHealth()>0) {
			entities.remove(index);
			entities.add(index,enemy);
//			}else {
//				entities.remove(index);
//			}
//			}
			}
			charactersUpdate(turns);
			turns++;
		}
		if (key == KeyEvent.VK_RIGHT) {
			Point2D pos=hero.keyCode(key);
			int index=collision(pos);
			if(index==-1) {
				hero.changePosition(pos);
			}else {
				GameElement enemy=hero.attack(entities.get(index));
//				if(enemy instanceof Enemy) {
//				Enemy ene=(Enemy) enemy;
//			if(ene.getHealth()>0) {
			entities.remove(index);
			entities.add(index,enemy);
//			}else {
//				entities.remove(index);
//			}
//			}
			}
			charactersUpdate(turns);
			turns++;
		}
		Hero hero=(Hero)entities.get(0);
		gui.setStatusMessage("ROGUE Starter Package - Turns:" + turns + "Hero:" +  hero.getHealth());
		gui.update();
	}
	public int collision(Point2D position) {
	for(int i=0;i!=entities.size();i++){
		if(position.equals(entities.get(i).getGamePosition())) {
			return i;
		}
	}
	return -1;	
}
}
