package pt.iscte.poo.example;

public interface Attackable {
	void getHit(int damage);
	GameElement attack(GameElement enemy);
	int getHealth();
	int getDamage();
	void changeHealth(int hp);
	void changeDamage(int dmg);
}
