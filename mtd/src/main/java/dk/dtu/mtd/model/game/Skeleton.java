package dk.dtu.mtd.model.game;

public class Skeleton extends Enemy{

    public Skeleton(int health, int speed, int damage, int reward) {
        super(health, speed, damage, reward);
        //TODO Auto-generated constructor stub
    }

     @Override
    public void attack() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'attack'");
    }

    @Override
    protected void performDeathAnimation() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'performDeathAnimation'");
    }

    @Override
    public void move() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }
    
}
