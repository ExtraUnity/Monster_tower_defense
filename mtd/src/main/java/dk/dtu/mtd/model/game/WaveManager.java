package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedList;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import dk.dtu.mtd.view.GameGui;

public class WaveManager implements Runnable {
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

        Thread player1Wave = new Thread(new Runnable() {
            Wave wave = new Wave(waveGenerator(waveNumber), space, 690);

            @Override
            public void run() {
                try {
                    space.put("gui","wave",20,Game.player1.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wave.run();
                player1Done = true;
            }

        });// new Thread(new Wave(new ArrayList<Enemy>(), space, Game.player1.id));
        Thread player2Wave = new Thread(new Runnable() {
            Wave wave = new Wave(waveGenerator(waveNumber), space,1920-690);

            @Override
            public void run() {
                try {
                    space.put("gui","wave",20,Game.player2.id);
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

}

class Wave {
    ArrayList<Enemy> enemies;
    Space space;
    final int START_X;
    final int START_Y = 0;

    public Wave(ArrayList<Enemy> enemies, Space space, int startX) {
        this.enemies = enemies;
        this.space = space;
        START_X = startX;
    }

    public void run() {
        int spawned = 0;
        long spawnRate = 5000L;
        long deltaTime = 0;
        long previousTime = System.nanoTime()/1_000_000L;

        while (true) {
            if (isComplete()) {
                break;
            }

            try {
                deltaTime = System.nanoTime()/1_000_000L - previousTime;
                System.out.println(deltaTime);
                if (spawned < enemies.size() && deltaTime > spawnRate) {
                    // spawn enemy
                    enemies.get(spawned).setX(START_X);
                    enemies.get(spawned).setY(START_Y);
                    previousTime = System.nanoTime()/1_000_000L;
                    spawned++;
                }
                for (int i = 0; i < spawned; i++) {
                    enemies.get(i).move();
                }
                LinkedList<String> coordinates = new LinkedList<String>();
                for(int i = 0; i < enemies.size(); i++){
                    String xy = "" + enemies.get(i).getX() + " " + enemies.get(i).getY();
                    coordinates.add(xy);
                }

                space.put("gui", "enemyUpdate", coordinates, Game.player1.id);
                space.put("gui", "enemyUpdate", coordinates, Game.player2.id);
                Thread.sleep(40L);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private boolean isComplete() {
        for (Enemy enemy : enemies) {
            if (enemy.getY() < 1080) {
                return false;
            }
        }
        return true;
    }

}