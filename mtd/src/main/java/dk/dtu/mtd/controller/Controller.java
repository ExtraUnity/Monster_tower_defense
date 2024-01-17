package dk.dtu.mtd.controller;

import org.jspace.ActualField;
import org.jspace.FormalField;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.model.game.Tower;
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
        String type = client.requestGame();

        client.joinGame(type);
        if (type.equals("host")) {
            client.hostChat();
        } else {
            client.joinChat();
        }
        guiThread = new Thread(guiMonitior);
        guiMonitior.playing = true;
        guiThread.start();
        Platform.runLater(() -> {
            Gui.game();
        });

    }

    //Returns gui to main menu and tells client to exit game
    public static void closeGame() {
        // Return GUI to main menu
        guiMonitior.playing = false;
        guiThread.interrupt();
        Gui.closeGame();

        //Tell client to close game
        client.exitGame();
    }

    //Close game if running and tell client to disconnect from lobby
    public static void exit() {

        // if a game is running close the game
        if (guiMonitior != null && guiMonitior.playing) {
            closeGame();
        }

        client.disconnectLobby();
        System.out.println("Exited");
    }

    public static void resign() {
        client.resign();
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
        client.sendMessage(msg);
    }

    public static void sendEnemies(EnemyType type) {
        client.sendEnemies(type);
    }

    public static void upgradeTower(int towerId) {
        int newPrice = client.upgradeTower(towerId);
        String newStats = client.getTowerStats(towerId);
        GameGui.updateUpgradePrice(towerId, newPrice, newStats);
    }

    public static void sellTower(int towerId) {
        client.sellTower(towerId);
    }

    public static void removeTower(int towerId) {
        GameGui.removeTower(towerId);
    }

    public static int getPlayerId() {
        return client.id;
    }

}

// hmm
class GUIMonitior implements Runnable {
    int counter = 0;
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
                    String[] hp = ((String) update[2]).split(" ");
                    final String hp1 = hp[0];
                    final String hp2 = hp[1];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.gameTop.updateHealth(hp1, hp2);
                        }
                    });
                    // ("gui", "chat", (LinkedList<String>) chat log , playerId)
                } else if (update[1].toString().equals("wave")) {
                    // make apropriate gui calls to display wave
                    String enemyTypes = (String) update[2];

                    Platform.runLater(() -> {
                        GameGui.gameWaveGuiLeft.initEnemies(enemyTypes);
                        GameGui.gameWaveGuiRight.initEnemies(enemyTypes);
                    });

                    // ("gui", "sendEnemies", String enemy info , playerId)
                } else if (update[1].toString().equals("waveNumber")) {
                    // make apropriate gui calls to display wave
                    int waveNumber = (Integer) update[2];

                    Platform.runLater(() -> {
                        GameGui.gameTop.updateWaveNumber(waveNumber);
                    });

                    // ("gui", "sendEnemies", String enemy info , playerId)
                } else if (update[1].toString().equals("sides")) {
                    // make apropriate gui calls to display wave
                    String side = (String) update[2];

                    Platform.runLater(() -> {
                        GameGui.gameTop.youOpponent(side);
                    });

                    // ("gui", "sendEnemies", String enemy info , playerId)
                } else if (update[1].toString().equals("sendEnemies")) {
                    String types = (String) update[2];
                    int waveId = (int) client.gameSpace.get(new ActualField("gui"),
                            new ActualField("sendEnemiesWaveId"),
                            new FormalField(Integer.class), new ActualField(client.id))[2];
                    Platform.runLater(() -> {
                        GameWaveGui wave = new GameWaveGui(waveId, GameGui.gameAreaWidth, GameGui.gameAreaHeight);
                        GameGui.addNewWaveGui(wave);
                        wave.initEnemies(types);

                    });
                    // ("gui", "enemyUpdateLeft", LinkedList<String> coordinates , playerId)
                    // Update the positions of every monster in the gui. (Left side)
                } else if (update[1].toString().equals("enemyUpdateLeft")) {
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
                    // ("gui", "enemyUpdateRight", LinkedList<String> coordinates , playerId)
                    // Update the positions of every monster in the gui. (Right side)
                } else if (update[1].toString().equals("enemyUpdateRight")) {
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

                } else if (update[1].toString().equals("newTower")) {
                    Tower tower = (Tower) update[2];

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.newTower(tower.getType(), tower.getSize(), tower.getRadius(),
                                    tower.getTowerId(), tower.getPlayerId(),
                                    tower.getX(), tower.getY());
                        }

                    });
                } else if (update[1].toString().equals("towerShoot")) {
                    int[] coordinates = (int[]) update[2];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.towerShoot(coordinates[0], coordinates[1], coordinates[2]);
                        }

                    });

                } else if (update[1].toString().equals("removeTower")) {
                    int towerId = (int) update[2];

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            GameGui.removeTower(towerId);
                        }

                    });

                } else if (update[1].toString().equals("playerLost")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.displayLose();
                        }
                    });
                } else if (update[1].toString().equals("playerWon")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.displayWin();
                        }
                    });
                } else if (update[1].toString().equals("waveEnded")) {
                    int waveId = (int) update[2];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < GameGui.gameArea.getChildren().size(); i++) {
                                Node n = GameGui.gameArea.getChildren().get(i);
                                if (n instanceof GameWaveGui) {
                                    GameWaveGui gui = (GameWaveGui) n;
                                    if (gui.waveGuiId == waveId) {
                                        gui.removeWave();
                                        break;
                                    }
                                }
                            }
                        }
                    });
                } else if (update[1].toString().equals("reward")) {
                    int reward = (int) update[2];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.bottom.updateGameBottomGui("" + reward);
                        }
                    });

                } else if (update[1].toString().equals("pathList")) {
                    LinkedList<String> pathSections = (LinkedList<String>) update[2];

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("got path from server");
                            GameGui.addPath(pathSections);
                        }
                    });

                }
            } catch (InterruptedException e) {
                counter++;

                if (counter == 100) {
                    // These prints are to let the player know they have been disconected
                    System.out.println("GuiMonitor failing, assuming disconnected");
                    System.out.println("Returning to main menu");
                    // Platform.runLater(new Runnable() {
                    //     @Override
                    //     public void run() {
                    //         if (client.lobby == null) {
                    //             GameGui.returnToLobbyPrompt();
                    //         }

                    //     }
                    // });
                    playing = false;
                }
            }
        }
    }

}