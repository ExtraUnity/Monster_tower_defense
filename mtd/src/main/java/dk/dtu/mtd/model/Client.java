package dk.dtu.mtd.model;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.shared.EnemyType;

public class Client {
    public RemoteSpace lobby;
    public RemoteSpace gameSpace;
    GameMonitor gameMonitor;
    public int id;
    private int gameId = -1;
    String hostIP;

    public Client() {

    }

    public void joinLobby(String serverIp) throws UnknownHostException, IOException, InterruptedException {
        // Join lobby
        this.hostIP = serverIp;
        lobby = new RemoteSpace("tcp://" + hostIP + ":37331/lobby?keep");
        // Get uniqe id from server
        lobby.put("request", "id", -1); // Request new id
        id = (int) lobby.get(new ActualField("id"), new FormalField(Integer.class))[1];
        System.out.println("Successful connection to lobby");
    }

    public void requestGame() {
        try {
            // Look for a game
            lobby.put("request", "game", id);
            gameId = (int) lobby.get(new ActualField("game"), new ActualField(id),
                    new FormalField(Integer.class))[2];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinGame() {
        try {
            // Join game
            gameSpace = new RemoteSpace("tcp://" + hostIP + ":37331/game" + gameId + "?keep");
            gameMonitor = new GameMonitor(this, gameSpace, lobby, id, gameId);
            new Thread(gameMonitor).start();
            System.out.println("Successful connection to game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void damagePlayer(int damage) {
        try {
            gameSpace.put("request", "damage", id); // Send request to damage
            gameSpace.put("data", "damage", damage); // Send damage amount
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void placeTower(String type, int x, int y) {
        try {
            gameSpace.put("request", "placeTower", id);
            gameSpace.put("towerInfo", type, x, y);
        } catch (InterruptedException e) {
            System.out.println("This is a problem");
        }
    }

    public void sendEnemies(EnemyType type) {
        try {
            gameSpace.put("request", "sendEnemies", id);
            gameSpace.put("data", "sendEnemies", type);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }


    public int upgradeTower(int towerId) {
        try {
            gameSpace.put("request", "upgradeTower", id);
            gameSpace.put("towerId", towerId);
            int newPrice = (int) gameSpace.get(new ActualField("towerUpgradeSucces"), new FormalField(Integer.class) , new ActualField(towerId))[1];
            return newPrice;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getTowerStats(int towerId) {
        try {
            gameSpace.put("request", "getTowerStats", id);
            gameSpace.put("towerId", towerId);
            String newStats = (String) gameSpace.get(new ActualField("towerStats"), new FormalField(String.class) , new ActualField(towerId))[1];
            return newStats;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    public void sellTower(int towerId) {
        try {
            gameSpace.put("request", "sellTower", id);
            gameSpace.put("towerId", towerId);
            int opponentId = (int) gameSpace.get(new ActualField("towerSellSuccess"), new ActualField(id), new FormalField(Integer.class), new ActualField(towerId))[2];
            gameSpace.put("gui", "removeTower", towerId, opponentId);
            Controller.removeTower(towerId);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resign() {
        try {
            gameSpace.put("request","resign", id);
        } catch (InterruptedException e) {
            System.out.println("Gamespace is already closed");
        }
    }

    public void exitGame() {
        try {
            // Exit a game and return to main meny
            resign();
            gameSpace.close();
        } catch (Exception e) {
            System.out.println("Not able to close a game");
        }
    }

    public void exit() {
        // Exit the application
        try {
            if (lobby != null) {
                exitGame();
                lobby.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getGameID() {
        return gameId;
    }

    public void sendMessage(String msg) {
        try {
            gameSpace.put("request", "chat", id);
            gameSpace.put("data", "chat", msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

class GameMonitor implements Runnable {
    Client client;
    RemoteSpace gameSpace;
    RemoteSpace lobby;
    int gameId;
    int playerId;

    GameMonitor(Client client, RemoteSpace gameSpace, RemoteSpace lobby, int playerId, int gameId) {
        this.client = client;
        this.gameSpace = gameSpace;
        this.lobby = lobby;
        this.playerId = playerId;
        this.gameId = gameId;

    }

    @Override
    public void run() {
        try {
            gameSpace.get(new ActualField("gameClosed"), new ActualField(playerId));

            System.out.println("Other player left the game");
            Controller.exitGame();

        } catch (InterruptedException e) {
            System.out.println("The game has been closed");
        }
        try {
            
            lobby.put("request", "closeGame", gameId);
            lobby.get(new ActualField("closedGame"), new ActualField(gameId));
            gameId = -1;

        } catch (Exception e) {
            System.out.println("Lobby not found");
        }
    }
}
