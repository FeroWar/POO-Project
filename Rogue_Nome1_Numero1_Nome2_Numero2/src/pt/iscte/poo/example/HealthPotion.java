package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class HealthPotion extends GameElement implements ImageTile,Pickable {

	public HealthPotion(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return "HealingPotion";
	}

	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 1;
	}
}