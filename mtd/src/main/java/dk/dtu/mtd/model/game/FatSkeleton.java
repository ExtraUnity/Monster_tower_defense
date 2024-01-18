package dk.dtu.mtd.model.game;

public class FatSkeleton extends Enemy{

    public FatSkeleton() {
        super(50, 2, 10, 20);
        cost = 350;
        incomeIncrement = 5;
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