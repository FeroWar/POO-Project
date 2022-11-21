package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Armor extends GameElement implements ImageTile,Pickable {

	public Armor(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return "Armor";
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