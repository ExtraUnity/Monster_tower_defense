package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jspace.Space;

import dk.dtu.mtd.shared.EnemyType;

public class WaveManager implements Runnable {

    private int totalWaves; // Total number of waves
    private int currentWaveId;
    public Wave waveLeft;
    public Wave waveRight;
    public ArrayList<Enemy> leftEnemies;
    public ArrayList<Enemy> rightEnemies;
    public boolean playing;
    int waveRound;
    volatile AtomicBoolean player1Done = new AtomicBoolean(false);
    volatile AtomicBoolean player2Done = new AtomicBoolean(false);
    Game game;

    public WaveManager(Game game) {
        this.playing = true;
        this.waveRound = 1;
        this.currentWaveId = 2;
        this.leftEnemies = new ArrayList<Enemy>();
        this.rightEnemies = new ArrayList<Enemy>();
        this.game = game;
    }

    @Override
    public void run() {
        while (playing) {
            System.out.println("Summoning wave " + waveRound);
            player1Done.set(false);
            player2Done.set(false);
            game.updateWave();
            spawnWave(waveRound);
            // after each wave both players are given 25 coins
            game.player1.addReward(25);
            game.player2.addReward(25);
            waveRound++;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println("Wavemanger failed to sleep after round:" + (waveRound - 1));
            }
        }
        System.out.println("Wavemaneger " + game.id + " closing");
    }

    String messageGenerator(Wave wave) {
        StringBuilder message = new StringBuilder();
        String lastType = null;
        int count = 0;

        for (Enemy enemy : wave.enemies) {
            String currentType = enemy.getClass().getSimpleName(); // Get the class name as enemy type

            if (lastType != null && !lastType.equals(currentType)) {
                // Append the count and type of the previous enemy batch
                if (message.length() > 0) {
                    message.append(",");
                }
                message.append(lastType).append(" ").append(count);
                count = 0; // Reset count for the new enemy type
            }

            lastType = currentType;
            count++;
        }

        // Append the last batch of enemies
        if (lastType != null) {
            if (message.length() > 0) {
                message.append(",");
            }
            message.append(lastType).append(" ").append(count);
        }
        return message.toString();
    }

    void sendEnemies(EnemyType type, int playerId) {

        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        switch (type) {
            case SKELETON:
                for (int i = 0; i < 6; i++) {
                    enemies.add(new Skeleton());
                }
                break;

            default:
                break;
        }
        Wave attackWave;
        if (playerId == game.player1.id) {
            attackWave = new Wave(enemies, game.gameSpace, 680, game.player1.id, currentWaveId++, game);
            leftEnemies.addAll(enemies);
        } else {
            attackWave = new Wave(enemies, game.gameSpace, 1920 - 680, game.player2.id, currentWaveId++, game);
            rightEnemies.addAll(enemies);
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    System.out.println("Sending enemies to player " + playerId + ". This is wave " + attackWave.waveId);
                    game.gameSpace.put("gui", "sendEnemies", messageGenerator(attackWave), game.player1.id);
                    game.gameSpace.put("gui", "sendEnemiesWaveId", attackWave.waveId, game.player1.id);
                    game.gameSpace.put("gui", "sendEnemies", messageGenerator(attackWave), game.player2.id);
                    game.gameSpace.put("gui", "sendEnemiesWaveId", attackWave.waveId, game.player2.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                attackWave.run();
            }

        }).start();
    }

    void spawnWave(int waveNumber) {
        ArrayList<Enemy> leftSide = waveGenerator(waveNumber);
        ArrayList<Enemy> rightSide = waveGenerator(waveNumber);
        waveLeft = new Wave(leftSide, game.gameSpace, 680, game.player1.id, 0, game);
        waveRight = new Wave(rightSide, game.gameSpace, 1920 - 680, game.player2.id, 1, game);
        leftEnemies.addAll(leftSide);
        rightEnemies.addAll(rightSide);

        String enemyTypes = messageGenerator(waveLeft);

        // left side
        Thread player1Wave = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 10 enemies on each side
                    game.gameSpace.put("gui", "wave", enemyTypes, game.player1.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waveLeft.run();
                player1Done.set(true);
            }

        });// new Thread(new Wave(new ArrayList<Enemy>(), space, Game.player1.id));

        // right side
        Thread player2Wave = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    game.gameSpace.put("gui", "wave", enemyTypes, game.player2.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waveRight.run();
                player2Done.set(true);
            }

        });// new Thread(new Wave(new ArrayList<Enemy>(), space, Game.player2.id));
        player1Wave.start();
        player2Wave.start();
        while (!(player1Done.get() && player2Done.get())) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<Enemy> waveGenerator(int wave) {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        if (wave < 1) {
            throw new InputMismatchException("Wave cannot be non-positive");
        }
        // every 5th wave will have an additional set of fat skeletons
        if (wave % 5 == 0) {
            for (int i = 0; i < wave; i++) {
                enemies.add(new FatSkeleton()); // New enemy type
            }
        }
        if (wave == 2) {
            int numberOfNormalEnemies = 3;

            for (int i = 0; i < numberOfNormalEnemies; i++) {
                enemies.add(new Skeleton());
            }

            enemies.add(new FatSkeleton()); // New enemy type

            for (int i = 0; i < numberOfNormalEnemies; i++) {
                enemies.add(new Skeleton());
            }

        } else {
            for (int i = 0; i < wave; i++) {
                enemies.add(new Skeleton());
            }
        }
        return enemies;
    }

    // Getters and setters
    public int getCurrentWaveNumber() {
        return waveRound;
    }

    public void setCurrentWaveNumber(int currentWaveNumber) {
        this.waveRound = currentWaveNumber;
    }

    public int getTotalWaves() {
        return totalWaves;
    }

    public void setTotalWaves(int totalWaves) {
        this.totalWaves = totalWaves;
    }

}

class Wave {
    ArrayList<Enemy> enemies;
    Space space;
    final int START_X;
    final int START_Y = 100;
    int playerId;
    int waveId;
    Game game;

    public Wave(ArrayList<Enemy> enemies, Space space, int startX, int playerId, int waveId, Game game) {
        this.enemies = enemies;
        this.space = space;
        START_X = startX;
        this.playerId = playerId;
        this.waveId = waveId;
        this.game = game;
    }

    public void run() {
        int spawned = 0;
        int spawnRate = 150; // In ticks
        int lastSpawnTick = game.gameTicker.gameTick;
        int deltaTick = 0;
        while (true) {
            deltaTick = game.gameTicker.gameTick - lastSpawnTick;
            try {
                if (spawned < enemies.size() && deltaTick >= spawnRate) {
                    // spawn enemy
                    enemies.get(spawned).setX(START_X);
                    enemies.get(spawned).setY(START_Y);
                    lastSpawnTick = game.gameTicker.gameTick;
                    spawned++;
                }

                for (int i = 0; i < spawned; i++) {
                    enemies.get(i).move();
                    if (enemies.get(i).reachedFinish() && !enemies.get(i).isDead()) {
                        enemies.get(i).setY(3000);
                        enemies.get(i).transferDamageToPlayer(playerId, game);

                        String newHp = "" + game.player1.getHealth() + " " + game.player2.getHealth();
                        space.put("gui", "damage", newHp, game.player1.id);
                        space.put("gui", "damage", newHp, game.player2.id);
                        enemies.get(i).eliminateFromRoster();
                    }
                }
                LinkedList<String> coordinates = new LinkedList<String>();
                for (int i = 0; i < enemies.size(); i++) {
                    String xy = enemies.get(i).isDead() ? "4000 4000"
                            : "" + enemies.get(i).getX() + " " + enemies.get(i).getY();
                    coordinates.add(xy);
                }
                coordinates.add("" + waveId);

                if (game.player1.id == playerId) {
                    space.put("gui", "enemyUpdateLeft", coordinates, game.player1.id);
                    space.put("gui", "enemyUpdateLeft", coordinates, game.player2.id);

                } else {
                    space.put("gui", "enemyUpdateRight", coordinates, game.player2.id);
                    space.put("gui", "enemyUpdateRight", coordinates, game.player1.id);
                }

                Thread.sleep(40L);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isComplete()) {
                try {
                    space.put("gui", "waveEnded", waveId, game.player1.id);
                    space.put("gui", "waveEnded", waveId, game.player2.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }

        }

    }

    private boolean isComplete() {

        for (Enemy enemy : enemies) {
            if (!enemy.reachedFinish() && !enemy.isDead()) {
                return false;
            }
        }
        return true;
    }

}