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

    public static void main(String[] args) {
        Client client = new Client("10.209.240.41");
    }

    public Client(String hostIP){
        try {
            lobby = new RemoteSpace("tcp://"+ hostIP +":37331/lobby?keep");
            start(hostIP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(String hostIP) {
        try {
            // Get uniqe id from server
            lobby.put("request", "id", -1); // Request new id
            id = (int)lobby.get(new ActualField("id"), new FormalField(Integer.class))[1];
            // Look for a game
            lobby.put("request", "game", id);
            int gameId = (int)lobby.get(new ActualField("game"), new ActualField(id), new FormalField(Integer.class))[2];
            // Join game
            try {
                gameSpace = new RemoteSpace("tcp://"+ hostIP +":37331/game"+ gameId +"?keep");
                System.out.println("Successful connection to game");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
