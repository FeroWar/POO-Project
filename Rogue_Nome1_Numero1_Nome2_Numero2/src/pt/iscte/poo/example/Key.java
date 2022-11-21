package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Key extends GameElement implements ImageTile,Pickable {
	private String id;

	public Key(Point2D position,String id) {
		super(position);
		this.id=id;
	}

	@Override
	public String getName() {
		return "Key";
	}

	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 1;
	}
	public String getId() {
		return id;
	}
}