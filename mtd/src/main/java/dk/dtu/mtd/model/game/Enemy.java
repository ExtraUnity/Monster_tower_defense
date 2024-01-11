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
    public void takeDamage(int amount, int playerId) {
        health -= amount;
        if (isDead()) {
            die(playerId);
        }
    }

    public boolean isDead() {
        return health <= 0;
    }


    protected void die(int playerId) {
        transferRewardToPlayer(playerId);
        performDeathAnimation();
    }

    // Is this enemy at the finish line
    public boolean reachedFinish() {
        return this.y > 1080 && this.y < 2000 && !isDead();
    }

    // Method to define the movement of the enemy
    //THIS IS CURSED DON'T HATE ME
    public void move() {
        int distToCenterX = Math.abs(this.x - 960);
        if(this.y < 590 && distToCenterX < 310) { //First stretch moving downwards
            this.y += this.speed;

        } else if(distToCenterX < 520) { //Second stretch, moving to the side
            this.x += this.x > 960 ? this.speed : -this.speed;
            
        } else if(distToCenterX < 560 && this.y > 390) {
            this.y -= this.speed;

        } else if(distToCenterX < 740) {
            this.x += this.x > 960 ? this.speed : -this.speed;
            
        } else {
            this.y += this.speed;
        }


    }

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
    protected void transferDamageToPlayer(int playerId) {
        if(playerId == Game.player1.id) {
            Game.player1.takeDamage(this.damage);
        } else {
            Game.player2.takeDamage(this.damage);
        }
    }

    // Method to transfer reward to the player
    protected void transferRewardToPlayer(int playerId) {
        if(playerId == Game.player1.id) {
            Game.player1.addReward(reward);
            System.out.println("player 1 has " + Game.player1.getRewards() + " reward");
        } else {
            Game.player2.addReward(reward);
            System.out.println("player 2 has " + Game.player2.getRewards() + " reward");
        }
    }

    // Abstract method for death animation
    protected abstract void performDeathAnimation();

}
