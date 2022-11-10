package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Hero extends Enemy implements ImageTile,Movable,Attackable {
	private int armor;

	public Hero(Point2D position) {
		super(position,10,1);
		this.armor=0;
	}

	@Override
	public String getName() {
		return "Hero";
	}
	
	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	public Point2D keyCode(int keycode) {
		Direction Direction1 = Direction.directionFor(keycode);
		Vector2D Vector = Direction1.asVector();
		return getPosition().plus(Vector);
	}
	public Point2D move(Point2D endPosition){
			return endPosition;
	}
	@Override
	public void getHit(int damage) {
		if(armor!=0) {
		if(Math.random()>=(0.5/armor)){
		changeHealth(getHealth()-damage);
		}
		}else {
			changeHealth(getHealth()-damage);
		}
	}
	@Override
	public GameElement attack(GameElement enemy) {
		if(enemy instanceof Enemy) {
			Enemy dub=(Enemy)enemy;
			dub.getHit(getDamage());
			GameElement exit=dub;
			return exit;
		}
		return enemy;
	}
}
