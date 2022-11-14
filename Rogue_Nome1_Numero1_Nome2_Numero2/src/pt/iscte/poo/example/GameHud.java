package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class GameHud  extends GameElement implements ImageTile{
	private String name;
	
	public GameHud(Point2D position,String name) {
		super(position);
		this.name=name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
}
