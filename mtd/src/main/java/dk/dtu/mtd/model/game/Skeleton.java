package dk.dtu.mtd.model.game;

public class Skeleton extends Enemy{

    public Skeleton() {
        super(7, 4, 1, 10);
        cost = 50;
        incomeIncrement = 5;
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
