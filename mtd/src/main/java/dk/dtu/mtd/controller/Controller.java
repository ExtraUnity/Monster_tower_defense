package dk.dtu.mtd.controller;

import org.jspace.ActualField;
import org.jspace.FormalField;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.view.GameGui;
import dk.dtu.mtd.view.Gui;

public class Controller {
    public static Controller controller;
    private static GUIMonitior guiMonitior;
    private static Client client = new Client("192.168.1.125");

    public static void initController() {
        controller = new Controller();
        guiMonitior = new GUIMonitior(client);
    }

    public static void joinLobby() {
        client.joinLobby();
    }

    public static void joinGame() {
        client.requestGame();
        client.joinGame();
        new Thread(guiMonitior).start();
    }

    public static void exitGame() {
        Gui.closeGame();
        client.exitGame();
        guiMonitior.playing = false;
    }

    public static void exit() {
        // exit the application
        client.exit();
    }

    public static void damagde() {
        client.damagde();
        //GameGui.updateGameGui(10); // TODO: noooooo this is wrong plz fix me!!!!
    }
}

// hmm
class GUIMonitior implements Runnable {
    Boolean playing = true;
    Client client;

    public GUIMonitior(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        Object[] update;
        while (playing) {
            try {
                // ("gui", (String) type, (int) data, playerId)
                update = client.gameSpace.get(new ActualField("gui"), new FormalField(String.class),
                        new FormalField(Integer.class), new ActualField(client.id));

                if (update[1].toString().equals("damagde")){
                    System.out.println("updating GUI");
                    //GameGui.updateGameGui((int) update[2]); throws an exeption because it's not on the GUI thread
                }
            } catch (InterruptedException e) {
                System.out.println("GUImonitor failing");
            }
        }
    }

}