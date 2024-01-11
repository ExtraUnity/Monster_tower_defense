package dk.dtu.mtd.model.game;

public class Skeleton extends Enemy{

    public Skeleton(Game game) {
        super(5, 2, 1, 10, game);
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
