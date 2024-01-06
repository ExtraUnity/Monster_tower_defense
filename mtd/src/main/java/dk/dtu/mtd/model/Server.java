package dk.dtu.mtd.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

import org.jspace.*;

import dk.dtu.mtd.model.game.Game;

public class Server {

    SpaceRepository server;
    Space lobby;
    String ip;
    int IDrequest = 0;
    int IDgame = 0;
    Queue<Integer> gameQueue;


    public Server() {
        server = new SpaceRepository();
        lobby = new SequentialSpace();
        gameQueue = new LinkedList<Integer>();

        server.add("lobby", lobby);
        try {
            ip = (InetAddress.getLocalHost().getHostAddress()).trim();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        server.addGate("tcp://" + ip + ":37331/?keep");
        System.out.println("Server ip: " + ip);
    }

    public void launch() {
        while (true) {
            try {
                handleRequest(lobby.get(new ActualField("request"), new FormalField(String.class),
                        new FormalField(Integer.class)));
                if (gameQueue.size() >= 2){
                    createGame(gameQueue.poll(), gameQueue.poll());
                }
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void handleRequest(Object[] request) {
        try {
            if ((request[1].toString()).equals("id")) {
                System.out.println("New player joining id: " + IDrequest);
                lobby.put("id", IDrequest);
                IDrequest++;
            } else if ((request[1].toString()).equals("game")) {
                System.out.println("Game request received by: " + request[2].toString());
                gameQueue.add((Integer) request[2]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createGame(int playerID1, int playerID2) {
        Game newGame = new Game(IDgame, playerID1, playerID2);
        System.out.println("Creating game...");
        server.add(("game" + IDgame), newGame.space);
        new Thread(newGame).start();
        try {
            lobby.put("game", playerID1, newGame.id);
            lobby.put("game", playerID2, newGame.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Sent player " +playerID1 + " and player " + playerID2 + " to Game" + newGame.id);
        IDgame++;
    }

    public static void main(String[] args) {
        Server ser = new Server();
        ser.launch();
    }
}
