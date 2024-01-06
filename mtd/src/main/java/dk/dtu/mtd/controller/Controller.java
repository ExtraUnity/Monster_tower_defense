package dk.dtu.mtd.controller;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.view.GameGui;
import dk.dtu.mtd.view.Gui;

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
        Gui.closeGame();
        client.exitGame();
    }
}
