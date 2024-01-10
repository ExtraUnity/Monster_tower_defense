package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import java.math.*;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Game implements Runnable {
    public int id;

    public static Player player1;
    public static Player player2;
    public Space gameSpace;
    private List<Tower> towerList;
    public WaveManager waveManager;

    public Game(int id, int playerID1, int playerID2) {
        this.id = id;
        player1 = new Player(playerID1, 150, 150);
        player2 = new Player(playerID2, 150, 150);
        this.towerList = new ArrayList<Tower>();
        gameSpace = new SequentialSpace();
        LinkedList<String> chat = new LinkedList<String>();
        try {
            gameSpace.put("chatList", chat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // create new waveManager, this can be run as a thread:
        waveManager = new WaveManager(gameSpace);
        new Thread(waveManager).start();

    }

    @Override
    public void run() {
        while (true) {
            try {
                // Tuple contens: ("request" , 'type of request' , 'player ID')
                handleGameRequest(gameSpace.get(new ActualField("request"),
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
                gameSpace.put("gui", "damage", player1.getHealth(), player1.id);
                gameSpace.put("gui", "damage", player2.getHealth(), player2.id);

                System.out.println("player2 recived" + damage + "damage");
            } else {
                player1.setHealth(player1.getHealth() - damage);
                gameSpace.put("gui", "damage", player1.getHealth(), player1.id);
                gameSpace.put("gui", "damage", player2.getHealth(), player2.id);

                System.out.println("player1 recived" + damage + "damage");
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
            placeTower((int) request[2]);
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

    public Boolean legalTowerPlacement(String towerType, int cost, int x, int y, int id) {
        for (Tower tower : towerList) {
            if(70 * 70 > Math.pow((tower.getX() - x), 2) + Math.pow((tower.getY() - y), 2)) {
                return false;
            }
        }
        if (player1.id == id && x > 960) {
            return false;
        } else if (player2.id == id && x < 960) {
            return false;
        }

        if (player1.id == id && player1.getRewards() <= cost) {
            return false;
        } else if (player2.id == id && player2.getRewards() <= cost) {
            return false;
        }
        return true;
    }

    public void placeTower(int playerId) {
        try {
            Object[] towerInfo = gameSpace.get(new ActualField("towerInfo"), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(Integer.class));
            if (towerInfo[1].equals("basicTower")) {
                int basicTowerCost = 50;
                if (legalTowerPlacement((String) towerInfo[1],basicTowerCost , (int) towerInfo[2], (int) towerInfo[3], playerId)) {
                    towerList.add(new BasicTower((int) towerInfo[2], (int) towerInfo[3], playerId));
                    gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), player1.id);
                    gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), player2.id);
                    if(playerId == player1.id) {
                        player1.spendRewards(basicTowerCost);
                    } else {
                        player2.spendRewards(basicTowerCost);
                    }
                    System.out.println("Tower placed at " + towerList.get(towerList.size() - 1).x + " "
                            + towerList.get(towerList.size() - 1).y);
                }
            } else if (towerInfo[1].equals("superTower")) {
                if (legalTowerPlacement((String) towerInfo[1],100 , (int) towerInfo[2], (int) towerInfo[3], playerId)) {
                    towerList.add(new BasicTower((int) towerInfo[2], (int) towerInfo[3], playerId));
                    gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), player1.id);
                    gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), player2.id);
                    System.out.println("Tower placed at " + towerList.get(towerList.size() - 1).x + " "
                            + towerList.get(towerList.size() - 1).y);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
