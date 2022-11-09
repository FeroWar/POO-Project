package pt.iscte.poo.example;

public interface Attackable {
	void getHit(int damage);
	GameElement attack(GameElement enemy);
	int getHealth();
	int getDamage();
	

}
