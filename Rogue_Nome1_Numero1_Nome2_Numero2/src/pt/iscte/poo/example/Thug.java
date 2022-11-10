package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
public class Thug extends Enemy implements ImageTile,Movable,Attackable {

	public Thug(Point2D position) {
		super(position,10,3);
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
	@Override
	public void getHit(int damage) {
		changeHealth(getHealth()-damage);
	}
	@Override
	public GameElement attack(GameElement enemy) {
		if(enemy instanceof Hero) {
			if(Math.random()>=0.7) {
			Hero hero=(Hero)enemy;
			hero.getHit(getDamage());
			GameElement exit = hero;
			return exit;
			}
		}
		return enemy;
	}
}
