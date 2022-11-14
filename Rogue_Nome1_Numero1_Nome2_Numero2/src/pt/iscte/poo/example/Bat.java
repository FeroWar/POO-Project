package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
public class Bat extends Enemy implements ImageTile,Movable,Attackable {

	public Bat(Point2D position) {
		super(position,3,1);
	}

	@Override
	public String getName() {
		return "Bat";
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
		Direction Direction1 = Direction.random();
		Vector2D Vector = Direction1.asVector();
		if(Math.random()>=0.5) {
			Vector = Vector2D.movementVector(getPosition(),endPosition);
		}
		return getPosition().plus(Vector);
		}
	@Override
	public void getHit(int damage) {
		changeHealth(getHealth()-damage);
	}
	@Override
	public GameElement attack(GameElement enemy) {
		if(enemy instanceof Hero) {
			if(Math.random()>=0.5) {
			Hero hero=(Hero)enemy;
			hero.getHit(getDamage());
			if(getHealth()!=3) {
				changeHealth(getHealth()+1);
			}
			GameElement exit = hero;
			return exit;
			}
		}
		return enemy;
	}
}
