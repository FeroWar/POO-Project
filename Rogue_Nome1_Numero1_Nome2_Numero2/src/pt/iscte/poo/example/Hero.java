package pt.iscte.poo.example;

import java.util.ArrayList;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Hero extends Enemy implements ImageTile,Movable,Attackable,Effects,Cloneable {
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

	public int getLayer() {
		return 2;
	}
	
	public void action(int key) {
		Point2D pos = keyCode(key);
		int index = collision(pos);
		if (index == -1) {
			changePosition(pos);
		} else if (index == -3) {
			engine.getGui().setStatusMessage("ROGUE Starter Package - Turns:" + engine.getTurn() + " - " + engine.getPlayerName() + ":" + engine.getScore());
			Scoreboard.scoreboard();
			engine.addScore(5000);
			if (engine.getGui().askUser("You Won with " + engine.getScore() + " points, Want to restart?").equals("y")) {
				engine.getGui().clearImages();
				EngineExample.getInstance().start();
			} else {
				engine.getGui().dispose();
				System.exit(0);
			}
		} else if (index == -2) {
			return;
		} else if (engine.getCurrentRoom().get(index) instanceof Enemy) {
			heroEnemy((Enemy) engine.getCurrentRoom().get(index), index);
		} else if (engine.getCurrentRoom().get(index) instanceof Pickable) {
			changePosition(pos);
			heroPickable(engine.getCurrentRoom().get(index));
		} else if (engine.getCurrentRoom().get(index) instanceof Door) {
			heroDoor((Door) engine.getCurrentRoom().get(index), index);
		}

	}
	
	public void heroDoor(Door door,int index) {
		if (door.getId() == null) {
			new Room().roomUpdate(door, index);
		} else {
			for (int i = 0; i != inventory.size(); i++) {
				if (inventory.get(i) instanceof Key) {
					Key Dkey = (Key) inventory.get(i);
					if (door.getId().equals(Dkey.getId())) {
						engine.addScore(1000);
						engine.getGui().removeImage((GameElement)inventory.get(i));
						inventory.remove(i);
						engine.getHud().hudUpdate();
						new Room().roomUpdate(door, index);
						break;
					}
				}
			}
		}
		engine.getSave().setSave();
	}
	public void heroPickable(GameElement a) {
		if (pickUp(a)) {
			engine.getGui().removeImage(a);
			engine.getHud().hudUpdate();
			engine.getCurrentRoom().remove(a);
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
				engine.getGui().addImage(item);
				engine.getCurrentRoom().add(item);
			}
			engine.getGui().removeImage(engine.getCurrentRoom().get(index));
			engine.getCurrentRoom().remove(entity);
		} else {
			engine.getCurrentRoom().remove(entity);
			engine.getCurrentRoom().add(index, entity);
		}

	}
	public int collision(Point2D position) {
		if (position.getX() > 10 || position.getX() < 0 || position.getY() > 10 || position.getY() < 0) {
			return -2;
		}
		for (int i = 0; i != engine.getCurrentRoom().size(); i++) {
			if (position.equals(engine.getCurrentRoom().get(i).getGamePosition())) {
				if (engine.getCurrentRoom().get(i) instanceof Treasure) {
					return -3;
				}
				return i;
			}
		}
		return -1;
	}
	public int itemCollision(Point2D position) {
		for (int i = 0; i != engine.getCurrentRoom().size(); i++) {
			if (!(engine.getCurrentRoom().get(i) instanceof Hero)) {
				if (position.equals(engine.getCurrentRoom().get(i).getGamePosition())) {
					return i;
				}
			}
		}
		return -1;
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
		engine.getHud().healthUpdate();
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
			}
			inventory.remove(i);
			
			}
	}

	public void drop(int i) {
		if (inventory.size() > i) {
		GameElement item = (GameElement) getInventory().get(i);
		Point2D pos = new Point2D(0, 0);
		if (itemCollision(getGamePosition()) == -1) {
			pos = (getGamePosition());
		} else if (itemCollision(getGamePosition().plus(new Vector2D(1, 0))) == -1) {
			pos = (getGamePosition().plus(new Vector2D(1, 0)));
		} else if (itemCollision(getGamePosition().plus(new Vector2D(0, -1))) == -1) {
			pos = (getGamePosition().plus(new Vector2D(0, -1)));
		} else if (itemCollision(getGamePosition().plus(new Vector2D(-1, 0))) == -1) {
			pos = (getGamePosition().plus(new Vector2D(-1, 0)));
		}
			item.changePosition(pos);
			engine.getCurrentRoom().add(item);
			dropSupport(i);
			engine.getHud().hudUpdate();
		}
	}
	public void use(int i) {
		if (inventory.size() > i) {
			if(inventory.get(i) instanceof HealthPotion) {
			GameElement item = (GameElement) getInventory().get(i);
			engine.getGui().removeImage(item);
			dropSupport(i);
			engine.getHud().hudUpdate();
			if(this.getHealth()<=5) {
				this.changeHealth(this.getHealth()+5);
				engine.getHud().healthUpdate();
			}else {
				this.changeHealth(10);
				engine.getHud().healthUpdate();
			}
			}
			}
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
	public void setInventory( ArrayList<Pickable> a) {
		inventory=a;
		
	}
	public void zeroHp() {
		String response1=engine.getGui().askUser("You Died, Want to load last save?");
		while(!(response1.equals("yes") || response1.equals("no"))) {
			response1=engine.getGui().askUser("You Died, Want to load last save?");
		}
		if (response1.equals("yes")) {
			if (engine.getSave().getSavedTurns() != 0) {
				new Room().loadSave();
			} else {
				String response2=engine.getGui().askUser("There is no saved game, would you like to restart?");
				while(!(response2.equals("yes") || response2.equals("no"))) {
					response2=engine.getGui().askUser("There is no saved game, would you like to restart?");
				}
				if (response2.equals("yes")) {
					engine.getGui().clearImages();
					engine.start();
				} else {
					Scoreboard.scoreboard();
					engine.getGui().dispose();
					System.exit(0);
				}
			}
		} else {
			String response2=engine.getGui().askUser("Would you like to restart?");
			while(!(response2.equals("yes") || response2.equals("no"))) {
				response2=engine.getGui().askUser("Would you like to restart?");
			}
			if (response2.equals("yes")) {
			engine.getGui().clearImages();
			engine.start();
			} else {
			Scoreboard.scoreboard();
			engine.getGui().dispose();
			System.exit(0);
		}
	}
	}
}
