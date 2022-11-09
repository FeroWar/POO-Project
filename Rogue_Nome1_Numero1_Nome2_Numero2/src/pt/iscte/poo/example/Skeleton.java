package pt.iscte.poo.example;

import java.util.List;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
public class Skeleton extends ObjectHealth implements ImageTile,Movable {

	public Skeleton(Point2D position,int l,int objHID, int hp,int dmg) {
		super(position,l,objHID,hp,dmg);
	}

	@Override
	public String getName() {
		return "Skeleton";
	}
	
	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	public void move(Point2D endPosition,List<GameElement> array){
		Vector2D Vector = Vector2D.movementVector(getPosition(),endPosition);
		if(isWithinBounds(getPosition().plus(Vector)) && colission(array,getPosition().plus(Vector))) {
			changePosition(getPosition().plus(Vector));}
	}
	
	static public boolean isWithinBounds(Point2D position) {
		return position.getX() >=0 && position.getY() >= 0 &&  position.getX() < 10 &&  position.getY() < 10; 
	}
	public boolean colission(List<GameElement> array, Point2D position) {
		for(int i=0;i!=array.size();i++){
			if(position.equals(array.get(i).getGamePosition())) {
				return false;
			}
		}
		return true;	
	}
}
