package pt.iscte.poo.example;

import java.util.ArrayList;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Hero extends Enemy implements ImageTile,Movable,Attackable,Effects {
	private int armor;
	private int poison;
	private ArrayList<Pickable> inventory;
	EngineExample engine=EngineExample.getInstance();

	public Hero(Point2D position) {
		super(position,10,1); //10  vida,  1 damage. Como  são os dados default, e sempre que for criado um  herói tem que ser mediante estes mesmos  dados, consideramos que não é necessário dá-los como argumentos.
		this.armor=0;
		this.inventory=new ArrayList<Pickable>();
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
	public void heroDoor(Door door,int index) {
		if (door.getId() == null) {
			engine.roomUpdate(door, index);
		} else {
			for (int i = 0; i != inventory.size(); i++) {
				if (inventory.get(i) instanceof Key) {
					Key Dkey = (Key) inventory.get(i);
					if (door.getId().equals(Dkey.getId())) {
						engine.addScore(1000);
						drop(i);
						engine.hudUpdate();
						engine.roomUpdate(door, index);
						break;
					}
				}
			}
		}
	}
	public void heroPickable(GameElement a) {
		if (pickUp(a)) {
			engine.guiRemove(a);
			engine.hudUpdate();
			engine.removeFromRoom(a);
		}
	}
	public void heroEnemy(Enemy entity,int index) {
		attack(entity);
		if (entity.getHealth() <= 0) {	
			engine.addScore(500);
			if(entity instanceof Thief) {
				Thief thief=(Thief) entity;
				GameElement item = (GameElement)thief.getInventory().get(0);
				item.changePosition(thief.getGamePosition());
				engine.guiAdd(item);
				engine.addToRoom(item);
			}
			engine.guiRemove(engine.roomIndex(index));
			engine.removeFromRoom(entity);
		} else {
			engine.removeFromRoom(entity);
			engine.addToRoomIndex(index, entity);
		}

	}
	
	public Point2D keyCode(int keycode) {
		Direction Direction1 = Direction.directionFor(keycode);
		Vector2D Vector = Direction1.asVector();
		return getPosition().plus(Vector); //em vez de o calcular aqui também o poderiamos fazer na função move, porém não o consideramos necessário já que significaria adicionar uma linha extra (pelo  que desnecessária).
	}
	public Point2D move(Point2D endPosition){
			return endPosition;
	}
	@Override
	public void getHit(int damage) {
		if(armor!=0) {
			if(Math.random()<=(0.5/armor)){ //assumimos que podemos ter mais do que uma armadura, dando stack aoi seu efeito (1 armadura-50% chance de dano; 2  armaduras-25% chance de dano; etc.)
				changeHealth(getHealth()-damage);
			}
		}
		else {
			changeHealth(getHealth()-damage);
		}
	}
	@Override
	public GameElement attack(GameElement enemy) {
		if(enemy instanceof Enemy) {
			Enemy enemy1=(Enemy)enemy;
			enemy1.getHit(getDamage());//não podemos só pôr damage=1 porque é possível o herói ter uma espada (para além de ser preferível não ter coisas hardcoded)
			GameElement enemy2=enemy1;
			return enemy2;
		}
		return enemy;
	}
	
	public boolean pickUp(GameElement item){
		if(inventory.size()<3) {
			inventory.add((Pickable)item);
			if(item instanceof Sword) {
				this.changeDamage(getDamage()*2);
			}else if(item instanceof Armor) {
				this.armor++;
			}
				
			return true;
		}
		return false;
		}
	
	public void dropSupport(int i){
		if(inventory.size()>=i) {
			GameElement item=(GameElement)inventory.get(i);
			if(item instanceof Sword) {
				this.changeDamage(getDamage()/2);
			}else if(item instanceof Armor) {
				this.armor--;
			}else if(item instanceof HealthPotion) {
				this.poison=0;
				if(this.getHealth()<=5) {
					this.changeHealth(this.getHealth()+5);
				}else {
					this.changeHealth(10);
				}
			}
			inventory.remove(i);
			
			}
	}
	public void drop(int i) {
		GameElement item = (GameElement) getInventory().get(i);
		Point2D pos = new Point2D(0, 0);
		if (engine.itemCollision(getGamePosition()) == -1) {
			pos = (getGamePosition());
		} else if (engine.itemCollision(getGamePosition().plus(new Vector2D(1, 0))) == -1) {
			pos = (getGamePosition().plus(new Vector2D(1, 0)));
		} else if (engine.itemCollision(getGamePosition().plus(new Vector2D(0, -1))) == -1) {
			pos = (getGamePosition().plus(new Vector2D(0, -1)));
		} else if (engine.itemCollision(getGamePosition().plus(new Vector2D(-1, 0))) == -1) {
			pos = (getGamePosition().plus(new Vector2D(-1, 0)));
		}
		if (!(item instanceof HealthPotion)) {
			item.changePosition(pos);
			engine.addToRoom(item);
			dropSupport(i);
		} else {
			engine.guiRemove(item);
			dropSupport(i);
		}
		engine.hudUpdate();
	}

	public ArrayList<Pickable> getInventory(){
		return inventory;
	}
	@Override
	public void setPoison(int a){
		this.poison=a;
	}
	@Override
	public int getPoison(){
		return poison;
	}
}
