package dk.dtu.mtd.model.game;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Game implements Runnable{
    public int id;
    public int player1;
    public int player2;
    public Space space;

    public Game(int id, int playerID1, int playerID2){
        this.id = id;
        this.player1 = playerID1;
        this.player2 = playerID2;
        space = new SequentialSpace();
    }
    
    @Override
    public void run() {
        RequestHandeler gameRequestHandeler = new RequestHandeler(space);
        while(true){
            try {
                //Tuple contens: ("request" , 'type of request' , 'player ID')
                gameRequestHandeler.handleGameRequest(space.get(new ActualField("request"), new FormalField(String.class), new FormalField(Integer.class)));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class RequestHandeler{
    Space space;
    public RequestHandeler(Space space){
        this.space = space;
    }

    void handleGameRequest(Object[] request) throws InterruptedException{
        if(request[1].toString().equals("exit")){
            space.put("exit", (Integer) request[2]);
        }
    }
}