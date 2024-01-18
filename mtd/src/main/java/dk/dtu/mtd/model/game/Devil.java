package dk.dtu.mtd.model.game;

public class Devil extends Enemy{

    public Devil() {
        super(7, 10, 1, 3);
        cost = 250;
        incomeIncrement = 5;
    }

    @Override
    public void attack() {
    }

    @Override
    protected void performDeathAnimation() {
    }
    
}
