package dk.dtu.mtd.model.game;

import java.util.LinkedList;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Game implements Runnable {
    public int id;
    public static GameTicker gameTicker;
    public static Player player1;
    public static Player player2;
    public Space gameSpace;
    public WaveManager waveManager;
    public TowerManager towerManager;
    private boolean playing;

    public Game(int id, int playerID1, int playerID2) {
        this.id = id;
        player1 = new Player(playerID1, 150, 0);
        player2 = new Player(playerID2, 150, 0);
        gameSpace = new SequentialSpace();
        playing = true;
        LinkedList<String> chat = new LinkedList<String>();
        try {
            gameSpace.put("chatList", chat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create new waveManager, this can be run as a thread:
        waveManager = new WaveManager(gameSpace);
        new Thread(waveManager).start();

        gameTicker = new GameTicker(this);
        new Thread(gameTicker).start();

        towerManager = new TowerManager(gameSpace, this);
        new Thread(towerManager).start();
    }

    public void closeGame() {
        gameTicker.playing = false;
        towerManager.playing = false;
        waveManager.player1Done.set(true);
        waveManager.player2Done.set(true);
        waveManager.playing = false;
        playing = false;
    }

    @Override
    public void run() {

        while (playing) {
            try {
                // Tuple contens: ("request" , 'type of request' , 'player ID')
                handleGameRequest(gameSpace.get(new ActualField("request"),
                        new FormalField(String.class), new FormalField(Integer.class)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Game " + id + " ending it's run loop");
    }

    @SuppressWarnings("unchecked")
    void handleGameRequest(Object[] request) throws InterruptedException {
        if (request[1].toString().equals("exit")) {
            // waveManager.playing = false;
            // towerManager.playing = false;

            if ((int) request[2] == player1.id) {
                gameSpace.put("exit", player1.id);
                gameSpace.put("gameClosed", player2.id);
            } else {
                gameSpace.put("exit", player2.id);
                gameSpace.put("gameClosed", player1.id);
            }
        } else if (request[1].toString().equals("damage")) {
            int damage = (int) gameSpace.get(new ActualField("data"), new ActualField("damage"),
                    new FormalField(Integer.class))[2];
            if ((int) request[2] == player1.id) {

                player2.setHealth(player2.getHealth() - damage);
                // ("damadge", newHealth, playerID)
                String newHealth = "" + player1.getHealth() + " " + player2.getHealth();
                gameSpace.put("gui", "damage", newHealth, player1.id);
                gameSpace.put("gui", "damage", newHealth, player2.id);

                System.out.println("player2 recived " + damage + " damage");
            } else {
                player1.setHealth(player1.getHealth() - damage);
                String newHealth = "" + player1.getHealth() + " " + player2.getHealth();
                gameSpace.put("gui", "damage", newHealth, player1.id);
                gameSpace.put("gui", "damage", newHealth, player2.id);

                System.out.println("player1 recived " + damage + " damage");
            }
        } else if (request[1].toString().equals("reward")) {
            int reward = (int) request[3];

            if ((int) request[2] == player1.id) {
                player2.setRewards(player2.getRewards() + reward);
                // ("reward", newRewards, playerID)
                gameSpace.put("gui", "reward", player1.getRewards(), player1.id);
                gameSpace.put("gui", "reward", player2.getRewards(), player2.id);

                System.out.println("player2 recived" + reward + "rewards");
            } else {
                player1.setRewards(player1.getRewards() + reward);
                gameSpace.put("gui", "reward", player1.getRewards(), player1.id);
                gameSpace.put("gui", "reward", player2.getRewards(), player2.id);

                System.out.println("player1 recived" + reward + "rewards!");
            }
        } else if (request[1].toString().equals("placeTower")) {
            towerManager.placeTower((int) request[2]);
        } else if (request[1].toString().equals("chat")) {
            System.out.println("Game recieved chat request");
            // Retrieve chatlist and update to include message
            String msg = (String) gameSpace.get(new ActualField("data"), new ActualField("chat"),
                    new FormalField(String.class))[2];
            System.out.println("Game recieved message");
            String player = String.valueOf((int) request[2]);
            Object[] res = gameSpace.get(new ActualField("chatList"), new FormalField(LinkedList.class));
            LinkedList<String> chat = (LinkedList<String>) res[1];
            chat.add("Player " + player + ": " + msg);
            gameSpace.put("chatList", chat);
            // One for each player
            gameSpace.put("gui", "chat", chat, player1.id);
            gameSpace.put("gui", "chat", chat, player2.id);
            System.out.println("Game put chat updates");
        }
    }

}
