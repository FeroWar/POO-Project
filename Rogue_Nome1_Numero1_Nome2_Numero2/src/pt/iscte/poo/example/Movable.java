package pt.iscte.poo.example;

import java.util.List;

import pt.iscte.poo.utils.Point2D;

public interface Movable {
		 void move(Point2D heroPosition,List<GameElement> array);
		 boolean colission(List<GameElement> array,Point2D position);	
	

}
