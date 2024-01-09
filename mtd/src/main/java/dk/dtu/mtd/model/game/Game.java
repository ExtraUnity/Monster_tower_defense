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
    public WaveManager waveManager;

    public Game(int id, int playerID1, int playerID2) {
        this.id = id;
        this.player1 = new Player(playerID1, 150, 0);
        this.player2 = new Player(playerID2, 150, 0);
        space = new SequentialSpace();
        LinkedList<String> chat = new LinkedList<String>();
        try {
            space.put("chatList", chat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create new waveManager, this can be run as a thread:
        waveManager = new WaveManager();

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
            int damage = (int) space.get(new ActualField("data"), new ActualField("damage"), new FormalField(Integer.class))[2];
            if ((int) request[2] == player1.id) {
                
                player2.setHealth(player2.getHealth() - damage);
                // ("damadge", newHealth, playerID)
                space.put("gui","damage", player1.getHealth(), player1.id);
                space.put("gui","damage", player2.getHealth(), player2.id);

                System.out.println("player2 recived" + damage + "damage");
            } else {
                player1.setHealth(player1.getHealth() - damage);
                space.put("gui","damage", player1.getHealth(), player1.id);
                space.put("gui","damage", player2.getHealth(), player2.id);

                System.out.println("player1 recived" + damage + "damage");
            }
        } else if (request[1].toString().equals("reward")) {
            int reward = (int) request [3];

            if ((int) request[2] == player1.id) {
                player2.setRewards(player2.getRewards() + reward);
                // ("reward", newRewards, playerID)
                space.put("gui","reward", player1.getRewards(), player1.id);
                space.put("gui","reward", player2.getRewards(), player2.id);

                System.out.println("player2 recived" + reward + "rewards");
            } else {
                player1.setRewards(player1.getRewards() + reward);
                space.put("gui","reward", player1.getRewards(), player1.id);
                space.put("gui","reward", player2.getRewards(), player2.id);

                System.out.println("player1 recived" + reward + "rewards!");
            }
        } else if (request[1].toString().equals("chat")) {
            System.out.println("Game recieved chat request");
            //Retrieve chatlist and update to include message
            String msg = (String) space.get(new ActualField("data"), new ActualField("chat"), new FormalField(String.class))[2];
            System.out.println("Game recieved message");
            String player = String.valueOf((int) request[2]);
            Object[] res = space.get(new ActualField("chatList"), new FormalField(LinkedList.class));
            LinkedList<String> chat = (LinkedList<String>) res[1];
            chat.add("Player " + player + ": " + msg);
            space.put("chatList", chat);
            //One for each player
            space.put("gui","chat", chat, player1.id);
            space.put("gui","chat", chat, player2.id);
            System.out.println("Game put chat updates");
        }
    }

}
