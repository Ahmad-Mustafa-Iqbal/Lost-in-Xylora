package main;

public class Alien {
    private String name;
    private int health;
    private int attackPower;

    public Alien(String name, int health, int attackPower) {
        this.name = name;
        this.health = health;
        this.attackPower = attackPower;
    }

    public Alien(int health, int attackPower) {
        this("Alien", health, attackPower);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public int attack() {
        return attackPower;
    }
}
