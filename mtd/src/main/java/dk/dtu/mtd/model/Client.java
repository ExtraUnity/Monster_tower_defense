package dk.dtu.mtd.model;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class Client {
    public RemoteSpace lobby;
    RemoteSpace gameSpace;
    int id;
    String hostIP;

    public Client(String hostIP){

        this.hostIP = hostIP;

        start();
    }

    public void start() {
        try {
            // Join lobby
            lobby = new RemoteSpace("tcp://"+ hostIP +":37331/lobby?keep");
            // Get uniqe id from server
            lobby.put("request", "id", -1); // Request new id
            id = (int)lobby.get(new ActualField("id"), new FormalField(Integer.class))[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinGame() {
        try {
            System.out.println("im here2");
            // Get uniqe id from server
            lobby.put("request", "id", -1); // Request new id
            id = (int)lobby.get(new ActualField("id"), new FormalField(Integer.class))[1];
            // Look for a game
            lobby.put("request", "game", id);
            int gameId = (int)lobby.get(new ActualField("game"), new ActualField(id), new FormalField(Integer.class))[2];
            // Join game
            gameSpace = new RemoteSpace("tcp://"+ hostIP +":37331/game"+ gameId +"?keep");
            System.out.println("Successful connection to game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
