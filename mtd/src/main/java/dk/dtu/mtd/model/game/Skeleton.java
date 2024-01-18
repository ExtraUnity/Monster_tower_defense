package dk.dtu.mtd.model.game;

public class Skeleton extends Enemy{

    public Skeleton() {
        super(7, 4, 2, 10);
        cost = 100;
        incomeIncrement = 2;
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
        //throw new UnsupportedOperationException("Unimplemented method 'performDeathAnimation'");
    }
    
}
