package dk.dtu.mtd.model.game;

public class Devil extends Enemy{

    public Devil() {
        super(4, 7, 1, 3);
        cost = 250;
        incomeIncrement = 10;
    }

    @Override
    public void attack() {
    }

    @Override
    protected void performDeathAnimation() {
    }
    
}
