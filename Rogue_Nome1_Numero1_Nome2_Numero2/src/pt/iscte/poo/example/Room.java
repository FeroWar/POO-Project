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
	private Skeleton skeleton;
	private Bat bat;
	private Thug thug;
	private Wall wall;
	private GameHud hud;
	private Sword sword;

	public Room() {
		roomElement = new ArrayList<GameElement>();
	}

	public Room(int i) {
		roomElement = new ArrayList<GameElement>();
		try {
			Scanner sc = new Scanner(new File("rooms/" + "room" + i + ".txt"));
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
			System.err.println("Ficheiro " + "room" + i + ".txt" + " nao encontrado");
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
		skeleton = new Skeleton(position);
		roomElement.add(skeleton);
		gui.addImage(skeleton);
	}

	private void addBat(Point2D position) {
		bat = new Bat(position);
		roomElement.add(bat);
		gui.addImage(bat);
	}

	private void addThug(Point2D position) {
		thug = new Thug(position);
		roomElement.add(thug);
		gui.addImage(thug);
	}
	private void addSword(Point2D position) {
		sword = new Sword(position);
		roomElement.add(sword);
		gui.addImage(sword);
	}
}