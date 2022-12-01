package pt.iscte.poo.example;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class GameHud  extends GameElement implements ImageTile{
	private String name;
	EngineExample engine=EngineExample.getInstance();
	
	public GameHud(Point2D position,String name) {
		super(position);
		this.name=name;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Point2D getPosition() {
		return getGamePosition();
	}

	@Override
	public int getLayer() {
		return 0;
	}
	public void healthUpdate() {
		for (int i = 0; i != engine.getCurrentRoom().size(); i++) {
			if (engine.getCurrentRoom().get(i) instanceof GameHud) {
				Point2D pos = engine.getCurrentRoom().get(i).getGamePosition();
				int health = engine.getHero().getHealth();
				for (int j = 5; j >= (health / 2); j--) {
					if (j >= 0) {
						if (pos.equals(new Point2D(4 - j, 10))) {
							healthSuport(i, pos, "Red");
						}
					}
				}
				for (int j = 0; j < (health / 2); j++) {
					if (pos.equals(new Point2D(4 - j, 10))) {
						healthSuport(i, pos, "Green");
					}
				}
				if ((health % 2) == 1) {
					if (pos.equals(new Point2D(4 - health / 2, 10))) {
						healthSuport(i, pos, "RedGreen");
					}
				}
			}
		}
	}

	public void healthSuport(int i, Point2D pos, String colour) {
		engine.getGui().removeImage(engine.getCurrentRoom().get(i));
		engine.getCurrentRoom().remove(i);
		GameHud Color = new GameHud(pos, colour);
		engine.getCurrentRoom().add(i, Color);
		engine.getGui().addImage(Color);
	}

	public void hudClear() {
		for (int i = 0; i != 3; i++) {
			for (int j = 0; j != engine.getCurrentRoom().size(); j++) {
				if (engine.getCurrentRoom().get(j).getGamePosition().equals(new Point2D(i + 7, 10))) {
					if (!(engine.getCurrentRoom().get(j) instanceof GameHud)) {
						engine.getGui().removeImage(engine.getCurrentRoom().get(j));
						engine.getCurrentRoom().remove(j);
						break;
					}
				}
			}
		}
	}
	public void hudUpdate() {
		hudClear();
		for (int i = 0; i != engine.getHero().getInventory().size(); i++) {
			hudSupport(i);
		}
	}
	public void hudSupport(int i) {
		GameElement item = (GameElement) engine.getHero().getInventory().get(i);
		item.changePosition(new Point2D(i + 7, 10));
		engine.getGui().addImage(item);
	}
}
