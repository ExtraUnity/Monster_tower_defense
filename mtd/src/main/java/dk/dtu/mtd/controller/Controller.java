package dk.dtu.mtd.controller;

import dk.dtu.mtd.model.Client;

public class Controller {
    private static Controller controller;
    private static Client client = new Client("192.168.1.125");;

    public static void initController() {
        controller = new Controller();
    }

    public static void joinLobby() {
        client.joinLobby();
    }

    public static void joinGame() {
        client.joinGame();
    }

    public static void exitGame() {
        client.exitGame();
    }
}
