package dk.dtu.mtd.model;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import dk.dtu.mtd.controller.Controller;

public class Client {
    public RemoteSpace lobby;
    public RemoteSpace gameSpace;
    GameMonitor gameMonitor;
    public int id;
    private int gameId;
    String hostIP;

    public static void main(String[] args) {
        Client client = new Client("192.168.1.125");
        client.joinLobby();
        client.joinGame();
    }

    public Client(String hostIP) {
        this.hostIP = hostIP;
    }

    public void joinLobby() {
        try {
            // Join lobby
            lobby = new RemoteSpace("tcp://" + hostIP + ":37331/lobby?keep");
            // Get uniqe id from server
            lobby.put("request", "id", -1); // Request new id
            id = (int) lobby.get(new ActualField("id"), new FormalField(Integer.class))[1];
            System.out.println("Successful connection to lobby");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinGame() {
        try {
            // Look for a game
            lobby.put("request", "game", id);
            gameId = (int) lobby.get(new ActualField("game"), new ActualField(id),
                    new FormalField(Integer.class))[2];
            // Join game
            gameSpace = new RemoteSpace("tcp://" + hostIP + ":37331/game" + gameId + "?keep");
            gameMonitor = new GameMonitor(gameSpace, lobby, id, gameId);
            new Thread(gameMonitor).start();
            System.out.println("Successful connection to game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitGame() {
        try {
            // Exit a game
            System.out.println("exeting game");
            gameSpace.put("request", "exit", id);
            gameSpace.get(new ActualField("exit"), new ActualField(id));
            gameSpace.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class GameMonitor implements Runnable {
    RemoteSpace gameSpace;
    RemoteSpace lobby;
    int gameId;
    int playerId;

    GameMonitor(RemoteSpace gameSpace, RemoteSpace lobby, int playerId, int gameId) {
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

            lobby.put("request", "exit", gameId);
        } catch (Exception e) {

        }
    }

}