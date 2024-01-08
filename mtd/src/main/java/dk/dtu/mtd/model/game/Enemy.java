package dk.dtu.mtd.model.game;

public abstract class Enemy {
    protected int health;
    protected int speed;
    protected int damage; // Damage the enemy does on the opposing player
    protected int reward; // Amount of money gained from killing enemy 

    public Enemy(int health, int speed, int damage, int reward) {
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.reward = reward;
    }

    // Method to take damage, reducing health
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            die();
        }
    }

    // Method to handle the enemy's death
    protected void die() {
        // Logic for handling what happens when an enemy dies (e.g., giving rewards)
    }

    // Abstract method to define the movement of the enemy
    public abstract void move();

    // Abstract method to define the attack behavior of the enemy
    public abstract void attack();

    // Getters and setters for the attributes
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
