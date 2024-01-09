package dk.dtu.mtd.controller;

import org.jspace.ActualField;
import org.jspace.FormalField;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.model.game.Enemy;
import dk.dtu.mtd.view.GameGui;
import dk.dtu.mtd.view.GameWaveGui;
import dk.dtu.mtd.view.Gui;
import javafx.application.Platform;

public class Controller {
    public static Controller controller;
    private static GUIMonitior guiMonitior;
    private static Thread guiThread;
    private static Client client = new Client();

    public static void initController() {
        controller = new Controller();
        guiMonitior = new GUIMonitior(client);
    }

    public static void joinLobby(String serverIp) throws UnknownHostException, IOException, InterruptedException {
        client.joinLobby(serverIp);
        System.out.println(serverIp);
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
        guiMonitior.playing = false;
        client.exit();
    }

    public static void damage() {
        client.damagePlayer(5);
    }

    public static void damageEnemyToPlayer(int damage) {
        client.damagePlayer(damage); // Inform the client to handle the damage
    }

    public static void rewardEnemyToPlayer(int reward) {
        client.rewardPlayer(reward);
    }

    public static void sendMessage(String msg) {
        System.out.println("Controller handling message");
        client.sendMessage(msg);
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
    @SuppressWarnings("unchecked")
    public void run() {
        Object[] update;
        while (playing) {
            try {
                // ("gui", (String) type, (Object) data, playerId)
                update = client.gameSpace.get(new ActualField("gui"), new FormalField(String.class),
                        new FormalField(Object.class), new ActualField(client.id));

                System.out.println("got");

                // ("gui", "damage", (int) new hp , playerId)
                if (update[1].toString().equals("damage")) {
                    System.out.println("updating GUI");
                    final int hp = (int) update[2];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.updateGameGui(hp);
                        }
                    });
                // ("gui", "chat", (LinkedList<String>) chat log , playerId)
                } else if (update[1].toString().equals("chat")) {
                    LinkedList<String> chat = (LinkedList<String>) update[2];
                    System.out.println("Gui recieved request to update");
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            GameGui.updateGameGui(chat);
                        }

                    });
                // ("gui", "chat", (...) wave info , playerId)
                } else if (update[1].toString().equals("wave")) {
                    // make apropriate gui calls to display wave

                // ("gui", "chat", (...) enemy info , playerId)
                } else if (update[1].toString().equals("enemyUpdate")) {
                    // recive the information that applys to an enemy to update it accordingly
                    // eg. an enemy has died -> it should be removed from the gui / play the death animation
                    ArrayList<Enemy> enemies = (ArrayList<Enemy>) update[2];
                    for(Enemy enemy : enemies) {
                        GameWaveGui.updateEnemies(enemy.getX(), enemy.getY());
                    }
                    

                }
            } catch (InterruptedException e) {
                System.out.println("GUImonitor failing");
            }
        }
    }

}