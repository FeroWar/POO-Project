package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Door extends GameElement implements ImageTile {
	private String id;
	private String room;
	private Point2D spawnPosition;
	private String image;

	public Door(Point2D position,String room,Point2D newPos,String id) {
		super(position);
		this.id=id;
		this.room=room;
		this.spawnPosition=newPos;
		this.image="DoorClosed";
	}
	public Door(Point2D position,String room,Point2D newPos) {
		super(position);
		this.room=room;
		this.spawnPosition=newPos;
		this.image="DoorOpen";
	}

	@Override
	public String getName() {
		return this.image;
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
	public String getRoom() {
		return room;
	}
	public Point2D getSpawnPosition() {
		return spawnPosition;
	}
}