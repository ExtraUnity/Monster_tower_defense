package dk.dtu.mtd.model.game;

public class Player {
    public int id;
    private int health;

    public Player(int id, int health){
        this.id = id;
        this.health = health;
    }

    public void setHealth(int newHealth){
        health = newHealth;
    }
    public int getHealth(){
        return health;
    }
}
