package pt.iscte.poo.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import pt.iscte.poo.utils.Point2D;

public class Room {
	private List<GameElement> roomElement;
	private Door door;
	private Wall wall;
	private GameElement enemy;
	private GameElement item;
	private GameElement treasure;
	EngineExample engine=EngineExample.getInstance();

	public Room() {
		roomElement = new ArrayList<GameElement>();
	}

	public Room(String room) {
		roomElement = new ArrayList<GameElement>();
		try {
			Scanner sc = new Scanner(new File("rooms/" + room + ".txt"));
			for (int y = 0; sc.hasNextLine(); y++) {
				String line = sc.nextLine();
				String[] chars=line.split(",");
				Create(chars);
				for (int x = 0; x != line.length(); x++) {
					if (line.charAt(x) == '#') {
						addWall(new Point2D(x, y));
					}
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("Ficheiro " + room + ".txt" + " nao encontrado");
		}
	}


    public  void Create(String[] chars) { // switch to cases
    	 String type=chars[0];
    	switch (type) {
    	  case "Bat":
            addBat(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
            break;
    	  case "Skeleton":
    		  addSkeleton(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
              break;
    	  case "Thug":
    		  addThug(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
              break;
    	  case "Thief":
    		  addThief(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
              break;
    	  case "Scorpion":
    	  		addScorpion(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
              break;
    	  case "Sword":
    		  addSword(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
            break;
    	  case "Armor":
    		  addArmor(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
            break;
    	  case "HealingPotion":
    		  addHealthPotion(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
            break;
    	  case "Key":
    		  addKey(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])),chars[3]);
            break;
    	  case "Door":
  			if (chars.length >= 7) {
				addDoor(new Point2D(Integer.parseInt(chars[1]), Integer.parseInt(chars[2])), chars[3],
						new Point2D(Integer.parseInt(chars[4]), Integer.parseInt(chars[5])), chars[6]);
			} else {
				addDoorOpen(new Point2D(Integer.parseInt(chars[1]), Integer.parseInt(chars[2])), chars[3],
						new Point2D(Integer.parseInt(chars[4]), Integer.parseInt(chars[5])));
			}
            break;
    	  case "Treasure":
    		  addTreasure(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
            break;
    	}
    }

	public List<GameElement> getList() {
		return this.roomElement;
	}

	public void changeList(List<GameElement> a) {
		this.roomElement = a;
	}
	private void addWall(Point2D position) {
		wall = new Wall(position);
		roomElement.add(wall);
	}

	private void addSkeleton(Point2D position) {
		enemy = new Skeleton(position);
		roomElement.add(enemy);
	}

	private void addBat(Point2D position) {
		enemy = new Bat(position);
		roomElement.add(enemy);
	}

	private void addThug(Point2D position) {
		enemy = new Thug(position);
		roomElement.add(enemy);
	}
	private void addThief(Point2D position) {
		enemy = new Thief(position);
		roomElement.add(enemy);
	}
	private void addScorpion(Point2D position) {
		enemy = new Scorpion(position);
		roomElement.add(enemy);
	}
	private void addSword(Point2D position) {
		item = new Sword(position);
		roomElement.add(item);
	}
	private void addArmor(Point2D position) {
		item = new Armor(position);
		roomElement.add(item);
	}
	private void addKey(Point2D position,String id) {
		item = new Key(position,id);
		roomElement.add(item);
	}
	private void addHealthPotion(Point2D position) {
		item = new HealthPotion(position);
		roomElement.add(item);
	}
	private void addDoor(Point2D position,String room,Point2D newPos,String id) {
		door = new Door(position,room,newPos,id);
		roomElement.add(door);
	}
	private void addDoorOpen(Point2D position,String room,Point2D newPos) {
		door = new Door(position,room,newPos);
		roomElement.add(door);
	}
	private void addTreasure(Point2D position) {
		treasure = new Treasure(position);
		roomElement.add(treasure);
	}
	
	public void roomUpdate(Door door, int index) {
		engine.getGui().removeImage(engine.getCurrentRoom().get(index));
		engine.getCurrentRoom().remove(index);
		engine.getCurrentRoom().add(new Door(door.getPosition(), door.getRoom(), door.getSpawnPosition()));
		engine.getGui().addImage(engine.getCurrentRoom().get(index));
		engine.getGui().clearImages();
		String[] id = door.getRoom().split("m");
		engine.setCurrentRoom(Integer.parseInt(id[1]));
		engine.addFloor();
		for (int w = 0; w != engine.getCurrentRoom().size(); w++) {
			engine.getGui().addImage(engine.getCurrentRoom().get(w));
		}
		engine.getGui().addImage(engine.getHero());
		engine.startHud();
		engine.getHud().hudUpdate();
		engine.getHud().healthUpdate();
		engine.getGui().update();
		engine.getHero().changePosition(door.getSpawnPosition());
		List<GameElement> list=new ArrayList<GameElement>();
		for(int i=0;i != engine.getCurrentRoom().size();i++) {
			list.add(i,(GameElement)engine.getCurrentRoom().get(i).clone());
		}
		engine.getSave().setSavedRoom(list);
		engine.getSave().setSavedHero((Hero) engine.getHero().clone());
		engine.getSave().setSavedInventory((ArrayList<Pickable>) engine.getHero().getInventory().clone());
		engine.getSave().setSavedScore(engine.getScore());
		engine.getSave().setSavedTurns(engine.getTurn());
	}
	public void loadSave() {
		engine.removeRoom(engine.getRoomNumber());
		engine.addRoom(engine.getRoomNumber(),engine.getSave().getSavedRoom());
		engine.setHero((Hero) engine.getSave().getSavedHero().clone());
		engine.getHero().setInventory((ArrayList<Pickable>) engine.getSave().getSavedInventory());
		engine.setScore(engine.getSave().getSavedScore());
		engine.setTurns(engine.getSave().getSavedTurns());
		engine.getGui().clearImages();
		engine.addFloor();
		for (int w = 0; w != engine.getCurrentRoom().size(); w++) {
			engine.getGui().addImage(engine.getCurrentRoom().get(w));
		}
		engine.getGui().addImage(engine.getHero());
		engine.startHud();
		engine.getHud().hudUpdate();
		engine.getHud().healthUpdate();
		engine.getGui().update();
	}
}