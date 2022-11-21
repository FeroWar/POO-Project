package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;

public abstract class ObjectHealth extends GameElement{
	private int health;
	private int damage;
	
	public ObjectHealth(Point2D position,int hp,int dmg) {
		super(position);
		this.health=hp;
		this.damage=dmg;
	}
	
public int getDamage() {
	return this.damage;
}
public int getHealth() {
	return this.health;
}
public int getHit(int damage) {
	int newHealth=health-damage;
	return newHealth;
	}
public int damage(ObjectHealth enemy) {
	enemy.getHit(getDamage());
	return enemy.getHealth();
}
public boolean isDead(ObjectHealth entity) {
	if(entity.getHealth()<0) {
		return true;
	}else {
	return false; 
	}
}

}
