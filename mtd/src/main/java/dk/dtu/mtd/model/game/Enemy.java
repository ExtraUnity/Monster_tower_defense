package dk.dtu.mtd.model.game;

public abstract class Enemy {
    protected int health;
    protected int speed;
    protected int damage; // Damage the enemy does on the opposing player
    protected int reward; // Amount of money gained from killing enemy
    public int cost;
    public int incomeIncrement;
    protected int x, y;

    public Enemy(int health, int speed, int damage, int reward) {
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.reward = reward;
        this.x = -1000;
        this.y = 500;
    }

    // Method to take damage, reducing health
    public void takeDamage(int amount, int playerId, Game game) {
        health -= amount;
        if (isDead()) {
            die(playerId, game);
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    protected void die(int playerId, Game game) {
        transferRewardToPlayer(playerId, game);
    }

    // remove an enemy that is not neccesarely dead.
    public void eliminateFromRoster() {
        this.x = 3000;
        this.y = 3000;
        health = -1;
    }

    // Is this enemy at the finish line
    public boolean reachedFinish() {
        return this.y > 1030 && this.y < 3500 && !isDead();
    }

    // Method to define the movement of the enemy
    // THIS IS CURSED DON'T HATE ME
    public void move() {
        if (this.x < 0) {
            System.out.println("This enemy not spawned");
            return;
        }
        int distToCenterX = Math.abs(this.x - 960);
        if (this.y < 580 && distToCenterX < 310) { // First stretch moving downwards
            this.y += this.speed;

        } else if (distToCenterX < 520) { // Second stretch, moving to the side
            this.x += this.x > 960 ? this.speed : -this.speed;

        } else if (distToCenterX < 560 && this.y > 390) {
            this.y -= this.speed;

        } else if (distToCenterX < 740) {
            this.x += this.x > 960 ? this.speed : -this.speed;

        } else {
            this.y += this.speed;
        }

    }


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
    protected void transferDamageToPlayer(int playerId, Game game) {
        if(playerId == game.player1.id) {
            game.player1.takeDamage(this.damage, game);
        } else {
            game.player2.takeDamage(this.damage, game);
        }
    }

    // Method to transfer reward to the player
    protected void transferRewardToPlayer(int playerId, Game game) {
        //System.out.println("Transering reward " + reward + " to player with id " + playerId);
        if (playerId == game.player1.id) {
            game.player1.addReward(reward);
        } else {
            game.player2.addReward(reward);
        }
        game.updateReward(playerId);

    }

}
