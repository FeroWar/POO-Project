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


    public  void Create(String[] chars) {
        if(chars[0].equals("Bat")) {
            addBat(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Skeleton")) {
            addSkeleton(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Thug")) {
            addThug(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Sword")) {
            addSword(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Armor")) {
            addArmor(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("HealingPotion")) {
            addHealthPotion(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])));
        }else if(chars[0].equals("Key")) {
            addKey(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])),chars[3]);
        }else if(chars[0].equals("Door")) {
        	if(chars.length>=7) {
        	 addDoor(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])),chars[3],new Point2D(Integer.parseInt(chars[4]),Integer.parseInt(chars[5])),chars[6]);
        	}else {
            addDoorOpen(new Point2D(Integer.parseInt(chars[1]),Integer.parseInt(chars[2])),chars[3],new Point2D(Integer.parseInt(chars[4]),Integer.parseInt(chars[5])));
        }
    }
    }

	public List<GameElement> getList() {
		return this.roomElement;
	}

	public void changeList(List<GameElement> a) {
		this.roomElement = a;
	}

	private void addHero(Point2D position) {
		hero = new Hero(position);
		roomElement.add(hero);
		gui.addImage(hero);
	}

	private void addGreen(Point2D position) {
		hud = new GameHud(position, "Green");
		roomElement.add(hud);
		gui.addImage(hud);
	}

	private void addRed(Point2D position) {
		hud = new GameHud(position, "Red");
		roomElement.add(hud);
		gui.addImage(hud);
	}

	private void addRedGreen(Point2D position) {
		hud = new GameHud(position, "RedGreen");
		roomElement.add(hud);
		gui.addImage(hud);
	}

	private void addWall(Point2D position) {
		wall = new Wall(position);
		roomElement.add(wall);
		gui.addImage(wall);
	}

	private void addSkeleton(Point2D position) {
		enemy = new Skeleton(position);
		roomElement.add(enemy);
		gui.addImage(enemy);
	}

	private void addBat(Point2D position) {
		enemy = new Bat(position);
		roomElement.add(enemy);
		gui.addImage(enemy);
	}

	private void addThug(Point2D position) {
		enemy = new Thug(position);
		roomElement.add(enemy);
		gui.addImage(enemy);
	}
	private void addSword(Point2D position) {
		item = new Sword(position);
		roomElement.add(item);
		gui.addImage(item);
	}
	private void addArmor(Point2D position) {
		item = new Armor(position);
		roomElement.add(item);
		gui.addImage(item);
	}
	private void addKey(Point2D position,String id) {
		item = new Key(position,id);
		roomElement.add(item);
		gui.addImage(item);
	}
	private void addHealthPotion(Point2D position) {
		item = new HealthPotion(position);
		roomElement.add(item);
		gui.addImage(item);
	}
	private void addDoor(Point2D position,String room,Point2D newPos,String id) {
		door = new Door(position,room,newPos,id);
		roomElement.add(door);
		gui.addImage(door);
	}
	private void addDoorOpen(Point2D position,String room,Point2D newPos) {
		door = new Door(position,room,newPos);
		roomElement.add(door);
		gui.addImage(door);
	}
}