package pt.iscte.poo.example;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Hero extends GameElement implements ImageTile,Movable,Attackable {

	private int health;
	private int damage;
	private int armor;

	public Hero(Point2D position) {
		super(position);
		this.health=10;
		this.damage=1;
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
		this.health-=damage;
	}
	@Override
	public GameElement attack(GameElement enemy) {
		if(enemy instanceof Skeleton) {
			Skeleton Skel=(Skeleton)enemy;
			Skel.getHit(this.damage);
			GameElement exit=Skel;
			return exit;
		}
		if(enemy instanceof Bat) {
			Bat bat=(Bat)enemy;
			bat.getHit(this.damage);
			GameElement exit=bat;
			return exit;
		}
		if(enemy instanceof Thug) {
			Thug thug=(Thug)enemy;
			thug.getHit(this.damage);
			GameElement exit=thug;
			return exit;
		}
		return enemy;
	}
	@Override
	public int getHealth() {
		return this.health;
	}
	@Override
	public int getDamage() {
		return this.damage;
	}
}
