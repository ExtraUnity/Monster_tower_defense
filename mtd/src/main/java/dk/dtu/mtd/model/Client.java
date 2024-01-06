package dk.dtu.mtd.model;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

public class Client {
    public RemoteSpace lobby;
    public RemoteSpace gameSpace;
    public int id;
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
            int gameId = (int) lobby.get(new ActualField("game"), new ActualField(id),
                    new FormalField(Integer.class))[2];
            // Join game
            gameSpace = new RemoteSpace("tcp://" + hostIP + ":37331/game" + gameId + "?keep");
            System.out.println("Successful connection to game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitGame(){
        try {
            // Exit a game
            gameSpace.put("request", "exit", id);
            System.out.println(gameSpace.get(new ActualField("exit"), new ActualField(id))[0].toString());
            gameSpace.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
