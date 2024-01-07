package dk.dtu.mtd.controller;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.view.Gui;

public class Controller {
    public static Controller controller;
    private static Client client = new Client("82.211.211.218");

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

    public static void exit() {
        // exit the application
        client.exit();
    }

    public static void increaseScore() {
        //TODO: make a call to increase a score
    }

}
