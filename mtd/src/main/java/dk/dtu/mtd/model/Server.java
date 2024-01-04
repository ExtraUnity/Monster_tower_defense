package dk.dtu.mtd.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.jspace.*;

import dk.dtu.mtd.model.game.Game;

public class Server {

    SpaceRepository server;
    Space lobby;
    String ip;
    int IDrequest = 0;
    int IDgame = 0;

    public Server() {
        server = new SpaceRepository();
        lobby = new SequentialSpace();
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

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void handleRequest(Object[] request) {
        try {
            if ((request[1].toString()).equals("id")) {
                lobby.put("id", IDrequest);
                IDrequest++;
            } else if ((request[1].toString()).equals("game")) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createGame(int playerID1, int playerID2) {
        Game newGame = new Game(IDgame);

        server.add(("game" + IDgame), newGame.space);
        new Thread(newGame).start();
        try {
            lobby.put("game", playerID1, newGame.id);
            lobby.put("game", playerID2, newGame.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IDgame++;
    }

    public static void main(String[] args) {
        Server ser = new Server();
        ser.launch();
    }
}
