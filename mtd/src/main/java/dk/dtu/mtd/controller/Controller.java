package dk.dtu.mtd.controller;

import org.jspace.ActualField;

import dk.dtu.mtd.model.Client;

public class Controller {
    private static Controller controller;
    private static Client client = new Client("10.209.240.41");;

    public static void initController() {
        controller = new Controller();

    }
    public static void joinLobby() {
        try {
            client.lobby.put("hey");
            client.lobby.get(new ActualField("hey back"));
            System.out.println("Got hey back");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
