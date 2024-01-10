package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;

import org.jspace.Space;

public class WaveManager implements Runnable {

    // TODO: remove unused fields
    private int currentWaveNumber;
    private int totalWaves; // Total number of waves

    boolean playing;
    int waveRound;
    boolean player1Done;
    boolean player2Done;
    Space space;

    public WaveManager(Space space) {
        this.playing = true;
        this.waveRound = 1;
        this.space = space;
    }

    @Override
    public void run() {
        while (playing) {
            player1Done = false;
            player2Done = false;
            spawnWave(waveRound);

            waveRound++;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println("Wavemanger failed to sleep after round:" + (waveRound - 1));
            }
        }
    }

    void spawnWave(int waveNumber) {
        // left side
        Thread player1Wave = new Thread(new Runnable() {
            Wave wave = new Wave(waveGenerator(waveNumber), space, 660, Game.player1.id);

            @Override
            public void run() {
                try {
                    // 10 enemies on each side
                    space.put("gui", "wave", 10, Game.player1.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wave.run();
                player1Done = true;
            }

        });// new Thread(new Wave(new ArrayList<Enemy>(), space, Game.player1.id));

        // right side
        Thread player2Wave = new Thread(new Runnable() {
            Wave wave = new Wave(waveGenerator(waveNumber), space, 1800 - 660, Game.player2.id);

            @Override
            public void run() {
                try {
                    space.put("gui", "wave", 10, Game.player2.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wave.run();
                player2Done = true;
            }

        });// new Thread(new Wave(new ArrayList<Enemy>(), space, Game.player2.id));
        player1Wave.start();
        player2Wave.start();
        while (!(player1Done && player2Done)) {

        }

        System.out.println("Spawning wave " + waveNumber);

    }

    ArrayList<Enemy> waveGenerator(int wave) {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        if (wave < 1) {
            throw new InputMismatchException("Wave cannot be non-positive");
        }
        if (wave == 1) {
            for (int i = 0; i < 10; i++) {
                enemies.add(new Skeleton());
            }

        }
        return enemies;
    }

    // Getters and setters
    public int getCurrentWaveNumber() {
        return currentWaveNumber;
    }

    public void setCurrentWaveNumber(int currentWaveNumber) {
        this.currentWaveNumber = currentWaveNumber;
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
    final int START_Y = 0;
    int playerId;

    public Wave(ArrayList<Enemy> enemies, Space space, int startX, int playerId) {
        this.enemies = enemies;
        this.space = space;
        START_X = startX;
        this.playerId = playerId;
    }

    public void run() {
        int spawned = 0;
        long spawnRate = 3000L;
        long deltaTime = 0;
        long previousTime = System.nanoTime() / 1_000_000L;

        while (true) {

            try {
                deltaTime = System.nanoTime() / 1_000_000L - previousTime;
                // System.out.println(deltaTime);
                if (spawned < enemies.size() && deltaTime > spawnRate) {
                    // spawn enemy
                    enemies.get(spawned).setX(START_X);
                    enemies.get(spawned).setY(START_Y);
                    previousTime = System.nanoTime() / 1_000_000L;
                    spawned++;
                }
                for (int i = 0; i < spawned; i++) {
                    enemies.get(i).move();
                    if (enemies.get(i).reachedFinish()) {
                        enemies.get(i).setY(3000);
                        enemies.get(i).transferDamageToPlayer(playerId);
                        int hp = playerId == Game.player1.id ? Game.player1.getHealth() : Game.player2.getHealth();
                        space.put("gui", "damage", hp, playerId);
                    }
                }
                LinkedList<String> coordinates = new LinkedList<String>();
                for (int i = 0; i < enemies.size(); i++) {
                    String xy = "" + enemies.get(i).getX() + " " + enemies.get(i).getY();
                    coordinates.add(xy);
                }

                if (Game.player1.id == playerId) {
                    space.put("gui", "enemyUpdateLeft", coordinates, Game.player1.id);
                    space.put("gui", "enemyUpdateRight", coordinates, Game.player2.id);
                } else {
                    space.put("gui", "enemyUpdateLeft", coordinates, Game.player2.id);
                    space.put("gui", "enemyUpdateRight", coordinates, Game.player1.id);
                }

                Thread.sleep(40L);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isComplete()) {
                break;
            }

        }

    }

    private boolean isComplete() {
        for (Enemy enemy : enemies) {
            if (!enemy.reachedFinish()) {
                return false;
            }
        }
        return true;
    }

}