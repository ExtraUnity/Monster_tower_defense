package dk.dtu.mtd.model.game;

public class DeerSkull extends Enemy{

    public DeerSkull() {
        super(100, 1, 40, 100);
        cost = 500;
        incomeIncrement = 10;
    }

    @Override
    public void attack() {
    }

    @Override
    protected void performDeathAnimation() {
    }
    
}
