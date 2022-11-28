package pt.iscte.poo.example;

import java.util.ArrayList;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Thief extends Enemy implements ImageTile, Movable, Attackable{
	
	private ArrayList<Pickable> inventory;

	public Thief(Point2D position) {
		super(position, 5, 0);
		inventory=new ArrayList<Pickable>();
	}

	@Override
	public String getName() {
		return "Thief";
	}

	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public Point2D move(Point2D endPosition) {
			Vector2D Vector = Vector2D.movementVector(getPosition(), endPosition);
			if(inventory.size()!=0) {
				Vector=new Vector2D(-Vector.getX(),-Vector.getY());
				return getPosition().plus(Vector);
			}else {
				return getPosition().plus(Vector);
			}
	}

	@Override
	public void getHit(int damage) {
		changeHealth(getHealth() - damage);
	}

	@Override
	public GameElement attack(GameElement enemy) {
			Hero hero = (Hero) enemy;
			int i=(int)Math.random()*3;
			if(hero.getInventory().size()!=0){
			inventory.add(hero.getInventory().get(i));
			EngineExample.getInstance().guiRemove((GameElement)hero.getInventory().get(i));
			hero.drop(i);
			}
			GameElement exit = hero;
			return exit;
	}
	public ArrayList<Pickable> getInventory(){
		return inventory;
	}
}