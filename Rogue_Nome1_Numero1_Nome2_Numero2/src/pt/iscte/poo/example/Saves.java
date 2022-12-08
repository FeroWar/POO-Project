package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Saves{
	EngineExample engine=EngineExample.getInstance();
	private List<GameElement> room;
	private Hero heroSaved;
	private List<Pickable> heroInventorySaved;
	private int scoreSaved;
	private int turnsSaved;
	
	public Saves() {
		room=new ArrayList<GameElement>();
		heroSaved=new Hero(new Point2D(4,4));
		heroInventorySaved= new ArrayList<Pickable>();
		scoreSaved=0;
		turnsSaved=0;
	}
	public void setSave() {
		room=engine.getCurrentRoom();
		heroSaved=engine.getHero();
		heroInventorySaved=engine.getHero().getInventory();
		scoreSaved=engine.getScore();
		turnsSaved=engine.getTurn();
	}
	public List<GameElement> getSavedRoom() {
		return room;
	}
	public Hero getSavedHero() {
		return heroSaved;
	}
	public List<Pickable> getSavedInventory() {
		return heroInventorySaved;
	}
	public int getSavedScore() {
		return scoreSaved;
	}
	public int getSavedTurns() {
		return turnsSaved;
	}


}
