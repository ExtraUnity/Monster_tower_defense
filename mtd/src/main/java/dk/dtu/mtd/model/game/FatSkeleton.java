package dk.dtu.mtd.model.game;

public class FatSkeleton extends Enemy{

    public FatSkeleton() {
        super(40, 2, 10, 20);
        cost = 100;
        incomeIncrement = 10;
    }

     @Override
    public void attack() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'attack'");
    }

    @Override
    protected void performDeathAnimation() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'performDeathAnimation'");
    }
    
}