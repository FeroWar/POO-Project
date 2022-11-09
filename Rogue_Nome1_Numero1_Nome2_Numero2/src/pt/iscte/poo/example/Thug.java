package pt.iscte.poo.example;

import java.util.List;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
public class Thug extends GameElement implements ImageTile,Movable,Attackable {

	private int health;
	private int damage;
	private int armor;

	public Thug(Point2D position) {
		super(position);
		this.health=10;
		this.damage=3;
		this.armor=0;
		}

	@Override
	public String getName() {
		return "Thug";
	}
	
	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	public Point2D move(Point2D endPosition){
		Vector2D Vector = Vector2D.movementVector(getPosition(),endPosition);
			return getPosition().plus(Vector);
	}
	
	static public boolean isWithinBounds(Point2D position) {
		return position.getX() >=0 && position.getY() >= 0 &&  position.getX() < 10 &&  position.getY() < 10; 
	}
	
	@Override
	public void getHit(int damage) {
		this.health-=damage;
	}
	@Override
	public GameElement attack(GameElement enemy) {
		if(enemy instanceof Hero) {
			if(Math.random()>=0.7) {
			Hero hero=(Hero)enemy;
			hero.getHit(this.damage);
			GameElement exit=hero;
			return exit;
			}
		}
		return enemy;
	}
	@Override
	public int getHealth() {
		return this.health;
	}
	@Override
	public int getDamage() {
		return this.damage;
	}
}
