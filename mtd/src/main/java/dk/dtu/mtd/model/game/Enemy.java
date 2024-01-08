package dk.dtu.mtd.model.game;

import dk.dtu.mtd.controller.Controller;

public abstract class Enemy {
    protected int health;
    protected int speed;
    protected int damage; // Damage the enemy does on the opposing player
    protected int reward; // Amount of money gained from killing enemy 

    protected int x, y;

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
        transferRewardToPlayer();
        performDeathAnimation();
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

    /// Coordinate handling ///
    // Method to update the enemy's position
    public void updatePosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    // Getters and setters for the coordinates
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Method to transfer damage to the tower
    protected void transferDamageToPlayer() {
        Controller.damageEnemyToPlayer(this.damage);
    }

    // Method to transfer reward to the player
    protected void transferRewardToPlayer() {
        Controller.rewardEnemyToPlayer(this.reward);
    }

    // Abstract method for death animation
    protected abstract void performDeathAnimation();

}
