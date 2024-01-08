package dk.dtu.mtd.model.game;

import java.util.LinkedList;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Game implements Runnable {
    public int id;
    public Player player1;
    public Player player2;
    public Space space;

    public Game(int id, int playerID1, int playerID2) {
        this.id = id;
        this.player1 = new Player(playerID1, 150);
        this.player2 = new Player(playerID2, 150);
        space = new SequentialSpace();
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Tuple contens: ("request" , 'type of request' , 'player ID')
                handleGameRequest(space.get(new ActualField("request"),
                        new FormalField(String.class), new FormalField(Integer.class)));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressWarnings("unchecked")
    void handleGameRequest(Object[] request) throws InterruptedException {
        if (request[1].toString().equals("exit")) {
            if ((int) request[2] == player1.id) {
                space.put("exit", player1.id);
                space.put("gameClosed", player2.id);
            } else {
                space.put("exit", player2.id);
                space.put("gameClosed", player1.id);
            }
        } else if (request[1].toString().equals("damage")) { // TODO: discuss naming conventions in the group
            if ((int) request[2] == player1.id) {
                player2.setHealth(player2.getHealth() - 10);
                // ("damadge", newHealth, playerID)
                space.put("gui","damage", player1.getHealth(), player1.id);
                space.put("gui","damage", player2.getHealth(), player2.id);

                System.out.println("player2 recived damage");
            } else {
                player1.setHealth(player1.getHealth() - 10);
                space.put("gui","damage", player1.getHealth(), player1.id);
                space.put("gui","damage", player2.getHealth(), player2.id);

                System.out.println("player1 recived damage");
            }
        } else if (request[1].toString().equals("chat")) {
            //Retrieve chatlist and update to include message
            Object[] res = space.get(new ActualField("chatList"), new FormalField(LinkedList.class));
            LinkedList<String> chat = (LinkedList<String>) res[2];
            chat.add(request[2].toString());
            space.put("chatList", chat);
            //One for each player
            space.put("gui","chat", chat, player1.id);
            space.put("gui","chat", chat, player2.id);
        }
    }

}
