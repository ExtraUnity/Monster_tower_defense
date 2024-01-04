package dk.dtu.mtd.model.game;

import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Game implements Runnable{
    public int id;
    public Space space;

    public Game(int id){
        this.id = id;
        space = new SequentialSpace();
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
