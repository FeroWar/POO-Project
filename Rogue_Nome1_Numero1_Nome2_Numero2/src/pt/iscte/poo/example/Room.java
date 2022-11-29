package pt.iscte.poo.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.utils.Point2D;

public class Room {
	private List<GameElement> roomElement;
	private ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
	private Hero hero;
	private Door door;
	private Wall wall;
	private GameHud hud;
	private GameElement enemy;
	private GameElement item;
	private GameElement treasure;

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
        if(chars[0].equals("Bat")) {
            addBat(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Skeleton")) {
            addSkeleton(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Thug")) {
            addThug(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Thief")) {
            addThief(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Scorpion")) {
            addScorpion(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Sword")) {
            addSword(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Armor")) {
            addArmor(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("HealingPotion")) {
            addHealthPotion(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Key")) {
            addKey(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])),chars[3]);
		} else if (chars[0].equals("Door")) {
			if (chars.length >= 7) {
				addDoor(new Point2D(Integer.parseInt(chars[1]), Integer.parseInt(chars[2])), chars[3],
						new Point2D(Integer.parseInt(chars[4]), Integer.parseInt(chars[5])), chars[6]);
			} else {
				addDoorOpen(new Point2D(Integer.parseInt(chars[1]), Integer.parseInt(chars[2])), chars[3],
						new Point2D(Integer.parseInt(chars[4]), Integer.parseInt(chars[5])));
			}
    }else if(chars[0].equals("Treasure")) {
    	addTreasure(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
    }
    }

	public List<GameElement> getList() {
		return this.roomElement;
	}

	public void changeList(List<GameElement> a) {
		this.roomElement = a;
	}

	private void addGreen(Point2D position) {
		hud = new GameHud(position, "Green");
		roomElement.add(hud);
	}

	private void addRed(Point2D position) {
		hud = new GameHud(position, "Red");
		roomElement.add(hud);
	}

	private void addRedGreen(Point2D position) {
		hud = new GameHud(position, "RedGreen");
		roomElement.add(hud);
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
}