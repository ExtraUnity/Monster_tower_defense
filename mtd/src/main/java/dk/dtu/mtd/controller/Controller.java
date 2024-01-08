package dk.dtu.mtd.controller;

import java.io.IOException;
import java.net.UnknownHostException;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.view.Gui;

public class Controller {
    public static Controller controller;
    private static Client client = new Client("10.209.248.191");

    public static void initController() {
        controller = new Controller();
    }

    public static void joinLobby() throws UnknownHostException, IOException, InterruptedException {
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
