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
		public int enemyCollision(Point2D position) {
			if(EngineExample.getInstance().getHero().getGamePosition().equals(position)) {
				return -2;
			}
			for (int i = 0; i != EngineExample.getInstance().getCurrentRoom().size(); i++) {
				if (position.equals(EngineExample.getInstance().getCurrentRoom().get(i).getGamePosition()) && !(EngineExample.getInstance().getCurrentRoom().get(i) instanceof Pickable)) {
					return i;
				}
			}
			return -1;
		}
		public void update(int i) {
			Enemy enemy = (Enemy) EngineExample.getInstance().getCurrentRoom().get(i);
			Point2D pos = enemy.move(EngineExample.getInstance().getHero().getPosition());
			int index = enemyCollision(pos);
			if (index == -1) {
				enemy.changePosition(pos);
				EngineExample.getInstance().getCurrentRoom().remove(i);
				EngineExample.getInstance().getCurrentRoom().add(i, enemy);
			} else if (index == -2) {
				enemy.attack(EngineExample.getInstance().getHero());
				EngineExample.getInstance().addScore(-10);
				if(EngineExample.getInstance().getCurrentRoom().get(i) instanceof Scorpion) {
					EngineExample.getInstance().getHero().setPoison(EngineExample.getInstance().getHero().getPoison()+1);
				}
			}
		}
	}
