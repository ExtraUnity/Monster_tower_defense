package dk.dtu.mtd.model.game;

public class Player {
    public int id;
    private int health;
    private int rewards;

    public Player(int id, int health, int rewards){
        this.id = id;
        this.health = health;
        this.rewards = rewards;
    }

    public void setHealth(int newHealth){
        health = newHealth;
    }
    public int getHealth(){
        return health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public void setRewards(int newReward){
        rewards = newReward;
    }

    public void spendRewards(int cost) {
        rewards =- cost;
    }

    public int getRewards(){
        return rewards;
    }


}
