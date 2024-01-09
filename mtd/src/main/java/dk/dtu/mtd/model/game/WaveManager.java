package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class WaveManager implements Runnable {
    boolean playing;
    int waveRound;
    Thread waveManeger;

    public WaveManager() {
        this.playing = true;
        this.waveRound = 1;
    }

    // TODO: migh not be needed
    void initWaveManager() {
        this.waveManeger = new Thread(new WaveManager());
        waveManeger.start();
    }

    @Override
    public void run() {
        while (playing) {
            spawnWave(waveRound);

            waveRound++;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println("Wavemanger failed to sleep after round:" + (waveRound - 1));
            }
        }
    }

    void spawnWave(int wave) {
        System.out.println("Spawning wave " + wave);

    }

}

class Wave implements Runnable {
    ArrayList<Enemy> enemies;
    Space space;
    int playerId;
    final int START_X = 0;
    final int START_Y = 0;

    public Wave(ArrayList<Enemy> enemies, Space space, int playerId) {
        this.enemies = enemies;
        this.space = space;
        this.playerId = playerId;
    }

    @Override
    public void run() {
        int spawned = 0;
        long spawnRate = 250L;
        long deltaTime = 0;
        long previousTime = System.nanoTime();

        while(true) {
            if(isComplete()) {
                break;
            }
            
            try {
                deltaTime = System.nanoTime() - previousTime;
                
                if(spawned < enemies.size() && deltaTime > spawnRate) {
                    //spawn enemy
                    enemies.get(spawned).setX(START_X);
                    enemies.get(spawned).setY(START_Y);
                    previousTime = System.nanoTime();
                    spawned++;
                }
                for(int i = 0; i < spawned; i++) {
                    enemies.get(i).move();
                }
                space.put("gui","enemyUpdate",enemies, playerId);
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