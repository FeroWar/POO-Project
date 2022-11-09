package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement {
	private Point2D position;
	private int layer;
	private int ObjectHealthID;
	
	public GameElement(Point2D position,int l,int objHID) {
		this.position = position;
		this.layer = l;
		this.ObjectHealthID=objHID;
	}
	public int getObjectHealthID() {
		return this.ObjectHealthID;
	}
	
	public Point2D getGamePosition() {
		return position;
	}

	public int getLayer() {
		return layer;
	}
	public void changePosition(Point2D x) {
		this.position=x;
	}
}