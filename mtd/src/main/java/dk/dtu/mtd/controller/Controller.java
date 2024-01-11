package dk.dtu.mtd.controller;

import org.jspace.ActualField;
import org.jspace.FormalField;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.model.game.Game;
import dk.dtu.mtd.model.game.Tower;
//import dk.dtu.mtd.model.game.WaveManager;
import dk.dtu.mtd.shared.EnemyType;
import dk.dtu.mtd.view.GameGui;
import dk.dtu.mtd.view.GameWaveGui;
import dk.dtu.mtd.view.Gui;
import javafx.application.Platform;
import javafx.scene.Node;

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

    public static void placeTower(String type, int x, int y) {
        client.placeTower(type, x, y);
    }

    public static void damageEnemyToPlayer(int damage) {
        client.damagePlayer(damage); // Inform the client to handle the damage
    }

    public static void sendMessage(String msg) {
        System.out.println("Controller handling message");
        client.sendMessage(msg);
    }


    public static void sendEnemies(EnemyType type) {
        client.sendEnemies(type);

    public static void upgradeTower(int towerId) {
        client.upgradeTower(towerId);
    }

    public static int getPlayerId() {
        return client.id;
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

                // ("gui", "damage", (int) new hp , playerId)
                if (update[1].toString().equals("damage")) {
                    // System.out.println("updating GUI");
                    String[] hp = ((String) update[2]).split(" ");
                    final String hp1 = hp[0];
                    final String hp2 = hp[1];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.updateGameGui(hp1, hp2);
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
                    String enemyTypes = (String) update[2];

                    Platform.runLater(() -> {
                        GameGui.gameWaveGuiLeft.initEnemies(enemyTypes);
                        GameGui.gameWaveGuiRight.initEnemies(enemyTypes);
                    });

                    // ("gui", "chat", (...) enemy info , playerId)
                } else if (update[1].toString().equals("sendEnemies")) {
                    String types = (String) update[2];
                    int waveId = (int) client.gameSpace.get(new ActualField("gui"),
                            new ActualField("sendEnemiesWaveId"),
                            new FormalField(Integer.class), new ActualField(client.id))[2];
                    Platform.runLater(() -> {
                        GameWaveGui wave = new GameWaveGui(waveId);
                        GameGui.addNewWaveGui(wave);
                        wave.initEnemies(types);

                    });
                } else if (update[1].toString().equals("enemyUpdateLeft")) {
                    // recive the information that applys to an enemy to update it accordingly
                    // eg. an enemy has died -> it should be removed from the gui / play the death
                    // animation
                    LinkedList<String> coords = (LinkedList<String>) update[2];
                    int waveId = Integer.valueOf(coords.removeLast());

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            for (Node n : GameGui.gameArea.getChildren()) {
                                if (n instanceof GameWaveGui) {
                                    GameWaveGui gui = (GameWaveGui) n;
                                    if (gui.waveGuiId == waveId) {
                                        gui.updateEnemies(coords);
                                    }
                                }
                            }

                        }

                    });
                } else if (update[1].toString().equals("enemyUpdateRight")) {
                    // recive the information that applys to an enemy to update it accordingly
                    // eg. an enemy has died -> it should be removed from the gui / play the death
                    // animation
                    LinkedList<String> coords = (LinkedList<String>) update[2];
                    int waveId = Integer.valueOf(coords.removeLast());
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            for (Node n : GameGui.gameArea.getChildren()) {
                                if (n instanceof GameWaveGui) {
                                    GameWaveGui gui = (GameWaveGui) n;
                                    if (gui.waveGuiId == waveId) {
                                        if (waveId == 2) {
                                            System.out.println("Updating wave " + waveId);
                                        }
                                        gui.updateEnemies(coords);
                                    }
                                }
                            }
                        }

                    });

                } else if (update[1].toString().equals("newTower")) {
                    Tower tower = (Tower) update[2];
                    System.out.println(tower.getType());
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                          //TODO: illigal pass of tower class
                          GameGui.newTower(tower);
                        }

                    });

                }
            } catch (InterruptedException e) {
                System.out.println("GUImonitor failing");
            }
        }
    }

}