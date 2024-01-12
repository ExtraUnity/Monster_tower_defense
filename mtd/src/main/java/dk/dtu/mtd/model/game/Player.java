package dk.dtu.mtd.model.game;

public class Player {
    public int id;
    private int health;
    private int rewards;
    public boolean hasLost;

    public Player(int id, int health, int rewards) {
        this.id = id;
        this.health = health;
        this.rewards = rewards;
        this.hasLost = false;
    }

    public void setHealth(int newHealth) {
        health = newHealth;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage, Game game) {
        if (this.health - damage <= 0) {
            System.out.println("Player is dead");
            this.health = 0;
            lose(game);
        } else {
            this.health -= damage;
        }
        
    }

    public void lose(Game game) {
        this.hasLost = true;
        game.closeGame();
    }

    public void setRewards(int newReward) {
        rewards = newReward;
    }

    public void spendRewards(int cost) {
        rewards -= cost;
    }

    public int getRewards() {
        return rewards;
    }

    public void addReward(int income) {
        rewards += income;
    }

}
