package pt.iscte.poo.example;

import pt.iscte.poo.utils.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Saves{
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
	public void setSavedRoom(List<GameElement> list) {
		room=list;
	}
	public List<GameElement> getSavedRoom() {
		return room;
	}
	public void setSavedHero(Hero a) {
		heroSaved=a;
	}
	public Hero getSavedHero() {
		return heroSaved;
	}
	public void setSavedInventory(List<Pickable> list) {
		heroInventorySaved=list;
	}
	public List<Pickable> getSavedInventory() {
		return heroInventorySaved;
	}
	public void setSavedScore(int a) {
		scoreSaved=a;
	}
	public int getSavedScore() {
		return scoreSaved;
	}
	public void setSavedTurns(int a) {
		turnsSaved=a;
	}
	public int getSavedTurns() {
		return turnsSaved;
	}


}
