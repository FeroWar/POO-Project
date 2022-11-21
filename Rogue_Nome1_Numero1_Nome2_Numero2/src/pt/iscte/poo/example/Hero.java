package pt.iscte.poo.example;

import java.util.ArrayList;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Hero extends Enemy implements ImageTile,Movable,Attackable {
	private int armor;
	private ArrayList<Pickable> inventory;

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
	
	public void drop(int i){
		if(inventory.size()>=i) {
			GameElement item=(GameElement)inventory.get(i);
			if(item instanceof Sword) {
				this.changeDamage(getDamage()/2);
			}else if(item instanceof Armor) {
				this.armor--;
			}else if(item instanceof HealthPotion) {
				if(this.getHealth()<=5) {
					this.changeHealth(this.getHealth()+5);
				}else {
					this.changeHealth(10);
				}
			}
			inventory.remove(i);
			
			}
	}

	public ArrayList<Pickable> getInventory(){
		return inventory;
	}
}
