package dk.dtu.mtd.controller;

import org.jspace.ActualField;
import org.jspace.FormalField;
import java.io.IOException;
import java.net.UnknownHostException;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.view.GameGui;
import dk.dtu.mtd.view.Gui;
import javafx.application.Platform;

public class Controller {
    public static Controller controller;
    private static GUIMonitior guiMonitior;
    private static Thread guiThread;
    private static Client client = new Client("10.209.248.191");

    public static void initController() {
        controller = new Controller();
        guiMonitior = new GUIMonitior(client);
    }

    public static void joinLobby() throws UnknownHostException, IOException, InterruptedException {
        client.joinLobby();
    }

    public static void joinGame() {
        client.requestGame();
        client.joinGame();
        guiThread = new Thread(guiMonitior);
        guiMonitior.playing = true;
        guiThread.start();
        Platform.runLater(() -> {
            Gui.game();
        });

    }

    public static void exitGame() {
        guiMonitior.playing = false;
        Gui.closeGame();
        client.exitGame();
    }

    public static void exit() {
        // exit the application
        client.exit();
    }

    public static void damageEnemyToPlayer(int damage) {
        client.damagePlayer(damage); // Inform the client to handle the damage
    }

    public static void rewardEnemyToPlayer(int reward) {
        client.rewardPlayer(reward);
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

                if (update[1].toString().equals("damage")) {
                    System.out.println("updating GUI");
                    final int hp = (int) update[2];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.updateGameGui(hp);
                        }
                    });
                    // GameGui.updateGameGui((int) update[2]); throws an exeption because it's not
                    // on the GUI thread
                }
            } catch (InterruptedException e) {
                System.out.println("GUImonitor failing");
            }
        }
    }

}