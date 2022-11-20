package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Sword extends GameElement implements ImageTile,Pickable {

	public Sword(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return "Sword";
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
