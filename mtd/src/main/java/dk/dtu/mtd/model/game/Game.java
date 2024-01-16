package dk.dtu.mtd.model.game;

import java.rmi.UnexpectedException;
import java.util.LinkedList;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import dk.dtu.mtd.shared.EnemyType;

public class Game implements Runnable {
    public int id;
    public GameTicker gameTicker;
    public Player player1;
    public Player player2;
    public Space gameSpace;
    private Space lobby;
    public WaveManager waveManager;
    public TowerManager towerManager;
    public Path path;
    private volatile boolean playing;

    public Game(int id, int playerID1, int playerID2, Space lobby) {
        this.id = id;
        this.lobby = lobby;
        player1 = new Player(playerID1, 150, 150);
        player2 = new Player(playerID2, 150, 150);
        gameSpace = new SequentialSpace();
        path = new Path();
        playing = true;
        LinkedList<String> chat = new LinkedList<String>();
        try {
            gameSpace.put("chatList", chat);
            gameSpace.put("gui", "pathList", path.player1Path, player1.id);
            gameSpace.put("gui", "pathList", path.player2Path, player2.id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gameTicker = new GameTicker(this);
        new Thread(gameTicker).start();
        // create new waveManager, this can be run as a thread:
        waveManager = new WaveManager(this);
        new Thread(waveManager).start();

        towerManager = new TowerManager(this, path);
        new Thread(towerManager).start();
        updateReward();

        try {
            gameSpace.put("gui", "sides", "left", player1.id);
            gameSpace.put("gui", "sides", "right", player2.id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeGame() {
        waveManager.playing = false;
        gameTicker.playing = false;
        towerManager.playing = false;
        try {
            gameSpace.put("waveDoneToken");
            gameSpace.put("waveDoneToken");
            gameSpace.get(new ActualField("gameTickerClosed"));
            gameSpace.get(new ActualField("waveManagerClosed"));
            gameSpace.get(new ActualField("towerManagerClosed"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        playing = false;
        try {
            gameSpace.put("request", "displayFinish", 0);
        } catch (InterruptedException e) {
            System.out.println("lol, hvorfor må vi ikke lukke spillet");
        }
    }

    public void updateReward() {
        try {
            gameSpace.put("gui", "reward", player1.getRewards(), player1.id);
            gameSpace.put("gui", "reward", player2.getRewards(), player2.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateWave() {
        try {
            gameSpace.put("gui", "waveNumber", waveManager.getCurrentWaveNumber(), player1.id);
            gameSpace.put("gui", "waveNumber", waveManager.getCurrentWaveNumber(), player2.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDamage(String newHealth) {
        try {
            gameSpace.put("gui", "damage", newHealth, player1.id);
            gameSpace.put("gui", "damage", newHealth, player2.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateWinner(int id1, int id2) {
        try {
            gameSpace.put("gui", "playerLost", "", id1);
            gameSpace.put("gui", "playerWon", "", id2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateExit(int id1, int id2) {
        try {
            gameSpace.put("exit", id1);
            gameSpace.put("gameClosed", id2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayWinner() throws UnexpectedException {
        if (player1.hasLost) {
            updateWinner(player1.id, player2.id);
        } else if (player2.hasLost) {
            updateWinner(player2.id, player1.id);
        } else {
            throw new UnexpectedException("Nobody lost, it was a tie and now we can go be happy.");
        }

        try {
            closeGame();
            //Thread.sleep(5000L);
            System.out.println("Ending game!");

            gameSpace.put("gameClosed", player1.id);
            gameSpace.put("gameClosed", player2.id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        while (playing) {
            try {
                // recive and handle all incoming tuples marced with "request".
                // Tuple contens: ("request" , 'type of request' , 'player ID')
                handleGameRequest(gameSpace.get(new ActualField("request"),
                        new FormalField(String.class), new FormalField(Integer.class)));
            } catch (InterruptedException e) {
                System.out.println("Game has failed on server side");
            }
        }

        // When the playing loop is finished running the game should be closed from the
        // lobby:
        try {
            lobby.put("request", "closeGame", id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Game " + id + " ending it's run loop");
    }

    @SuppressWarnings("unchecked")
    void handleGameRequest(Object[] request) throws InterruptedException {
        if (request[1].toString().equals("exit")) {

            if ((int) request[2] == player1.id) {
                updateExit(player1.id, player2.id);
            } else {
                updateExit(player2.id, player1.id);
            }
        } else if (request[1].toString().equals("hostChat")) { //Recieve message from chatHost that chat server is ready
            //Give guest message that chat is now hosted
            gameSpace.put("connectionStatus", "hostSuccess");

        } else if (request[1].toString().equals("damage")) {
            // Reciving a damage request
            int damage = (int) gameSpace.get(new ActualField("data"), new ActualField("damage"),
                    new FormalField(Integer.class))[2];
            if ((int) request[2] == player1.id) {
                player2.setHealth(player2.getHealth() - damage);
            } else {
                player1.setHealth(player1.getHealth() - damage);
            }
            String newHealth = "" + player1.getHealth() + " " + player2.getHealth();
            updateDamage(newHealth);

        } else if (request[1].toString().equals("reward")) {
            // Reciving a reward request
            int reward = (int) request[2];
            if ((int) request[2] == player1.id) {
                player2.setRewards(player2.getRewards() + reward);
                System.out.println("player2 recived" + reward + "rewards");
            } else {
                player1.setRewards(player1.getRewards() + reward);
                System.out.println("player1 recived" + reward + "rewards");
            }
            updateReward();
        } else if (request[1].toString().equals("placeTower")) {
            towerManager.placeTower((int) request[2]);

        } else if (request[1].toString().equals("upgradeTower")) {
            towerManager.upgradeTower((int) request[2]); // request[2] = playerId

        } else if (request[1].toString().equals("getTowerStats")) {
            towerManager.getTowerStats((int) request[2]); // request[2] = playerId

        } else if (request[1].toString().equals("sellTower")) {
            towerManager.removeTower((int) request[2]);
            
        } else if (request[1].toString().equals("chat")) {

            String msg = (String) gameSpace.get(new ActualField("data"), new ActualField("chat"),
                    new FormalField(String.class))[2];

            String player = String.valueOf((int) request[2]);
            Object[] res = gameSpace.get(new ActualField("chatList"), new FormalField(LinkedList.class));
            LinkedList<String> chat = (LinkedList<String>) res[1];
            chat.add("Player " + player + ": " + msg);
            gameSpace.put("chatList", chat);
            // One for each player
            gameSpace.put("gui", "chat", chat, player1.id);
            gameSpace.put("gui", "chat", chat, player2.id);

        } else if (request[1].toString().equals("sendEnemies")) {

            Object[] res = gameSpace.get(new ActualField("data"), new ActualField("sendEnemies"),
                    new FormalField(EnemyType.class));
            int senderId = (int) request[2];
            EnemyType type = (EnemyType) res[2];
            Player sendingPlayer = senderId == player1.id ? player1 : player2;
            Enemy enemy;
            switch (type) {
                case SKELETON:
                    enemy = new Skeleton();
                    break;
                case FAT_SKELETON:
                    enemy = new FatSkeleton();
                default:
                    enemy = new Skeleton();
                    break;
            }
            if (sendingPlayer.getRewards() < enemy.cost) {
                return;
            } else {
                sendingPlayer.spendRewards(enemy.cost);
                updateReward();
                // TODO: Implement increase income
            }
            int recieverId = senderId == player1.id ? player2.id : player1.id;
            waveManager.sendEnemies(type, recieverId);

        } else if (request[1].toString().equals("resign")) {
            if ((int) request[2] == player1.id) {
                player1.hasLost = true;
            } else {
                player2.hasLost = true;
            }
            try {
                displayWinner();
            } catch (UnexpectedException e) {
                e.printStackTrace();
            }
        }
    }

}
