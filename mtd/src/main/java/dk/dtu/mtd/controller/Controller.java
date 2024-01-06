package dk.dtu.mtd.controller;

import org.jspace.ActualField;

import dk.dtu.mtd.model.Client;

public class Controller {
    private static Controller controller;
    private static Client client = new Client("192.168.1.125");;

    public static void initController() {
        controller = new Controller();

    }

    public static void joinLobby() {
        try {
            client.joinLobby();
            client.lobby.put("hey");
            client.lobby.get(new ActualField("hey"));
            System.out.println("Got hey back");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int joinGame() {
        client.joinGame();
        return -1; 
    }
}
