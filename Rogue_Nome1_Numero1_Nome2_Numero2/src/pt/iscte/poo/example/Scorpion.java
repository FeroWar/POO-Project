package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Scorpion extends Enemy implements ImageTile, Movable, Attackable{

	public Scorpion(Point2D position) {
		super(position, 2, 0);
	}

	@Override
	public String getName() {
		return "Scorpio";
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
			return getPosition().plus(Vector);
	}

	@Override
	public void getHit(int damage) {
		changeHealth(getHealth() - damage);
	}

	@Override
	public GameElement attack(GameElement enemy) {
			Hero hero = (Hero) enemy;
			hero.getHit(getDamage());
			GameElement exit = hero;
			return exit;
	}
}
