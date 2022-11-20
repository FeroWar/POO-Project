package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Skeleton extends Enemy implements ImageTile, Movable, Attackable{

	public Skeleton(Point2D position) {
		super(position, 5, 1);
	}

	@Override
	public String getName() {
		return "Skeleton";
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
		int t = EngineExample.getInstance().getTurn();
		if (t % 2 == 0) {
			Vector2D Vector = Vector2D.movementVector(getPosition(), endPosition);
			return getPosition().plus(Vector);
		}
		return getPosition();
	}

	// static public boolean isWithinBounds(Point2D position) {
	// return position.getX() >=0 && position.getY() >= 0 && position.getX() < 10 &&
	// position.getY() < 10;
	// }

	@Override
	public void getHit(int damage) {
		changeHealth(getHealth() - damage);
	}

	@Override
	public GameElement attack(GameElement enemy) {
		int t = EngineExample.getInstance().getTurn();
		if (t % 2 == 0 && enemy instanceof Hero) { // t%2 porque o esqueleto só ataca uma vez por cada 
			//duas jogadas. Para solucionar este problema, definimos que apenas ataca nos turnos pares.
			Hero hero = (Hero) enemy;
			hero.getHit(getDamage());
			GameElement exit = hero;
			return exit;
		}
		return enemy;
	}
}
