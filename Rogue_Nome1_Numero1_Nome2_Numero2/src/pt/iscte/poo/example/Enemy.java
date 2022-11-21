package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class Enemy extends GameElement implements ImageTile,Movable,Attackable{
		private int health;
		private int damage;

		public Enemy(Point2D position,int hp,int dmg) {
			super(position);
			this.health=hp;
			this.damage=dmg;
		}
		
		@Override
		public Point2D getPosition() {
			return getGamePosition();
		}

		@Override
		public int getLayer() {
			return 2;
		}
		
		@Override
		public void getHit(int damage) {
			this.health-=damage;
		}
		@Override
		public int getHealth() {
			return this.health;
		}
		@Override
		public int getDamage() {
			return this.damage;
		}
		@Override
		public void changeHealth(int hp) {
			if(hp<0) {
				this.health=0;
			}else {
			this.health=hp;
			}
		}
		@Override
		public void changeDamage(int dmg) {
			this.damage=dmg;
		}
	}
