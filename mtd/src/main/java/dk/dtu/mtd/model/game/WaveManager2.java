package dk.dtu.mtd.model.game;

import java.util.ArrayList;

import org.jspace.ActualField;

import dk.dtu.mtd.shared.WaveType;

public class WaveManager2 implements Runnable {

    public volatile ArrayList<Enemy> leftEnemies;
    public volatile ArrayList<Enemy> rightEnemies;
    public ArrayList<Wave2> activeWaves;
    public boolean playing;
    private int waveRound;
    private int nextWaveId;
    private Game game;
    Thread waveRunner;

    public WaveManager2(Game game) {
        this.playing = true;
        this.waveRound = 1;
        this.leftEnemies = new ArrayList<Enemy>();
        this.rightEnemies = new ArrayList<Enemy>();
        this.activeWaves = new ArrayList<Wave2>();
        this.game = game;
        waveRunner = new Thread(new Runnable() {
            @Override
            public void run() {
                runWaves();
            }

        });
    }

    @Override
    public void run() {
        System.out.println("Starting waveManager2");
        waveRunner.start();
        while (playing) {
            System.out.println("Summoning next wave");
            Wave2 wave = new Wave2(game, waveRound, WaveType.REGULAR, nextWaveId);
            activeWaves.add(wave);

            try {
                game.gameSpace.get(new ActualField("waveManager2"), new ActualField("readyForNextRegularWave"));
            } catch (InterruptedException e) {
                System.out.println("WaveManager2 could not get afformation of next wave");
            }
            

            waveRound++;
            nextWaveId++;

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Closing waveManager2");
    }

    public void runWaves() {
        String coordinates;
        while (playing) {
            coordinates =  "";
            for (Wave2 i : activeWaves) {
                if (i.isDone()) {
                    activeWaves.remove(i);
                    if (i.type == WaveType.REGULAR) {
                        try {
                            game.gameSpace.put("waveManager2", "readyForNextRegularWave");
                        } catch (InterruptedException e) {
                            System.out.println("WaveManager2 could not put afformation for next wave");
                        }
                    }
                } else {
                    coordinates += i.getCoordinates();
                }
            }
            if(!coordinates.equals("")){
                try {
                    game.gameSpace.put("gui", "wavesUpdate", coordinates, game.player1.id);
                    game.gameSpace.put("gui", "wavesUpdate", coordinates, game.player2.id);
                    Thread.sleep(40L);
                } catch (InterruptedException e) {
                    System.out.println("failed to deliver next coordinateset for waveGui");
                }
    
            }
        }
    }

    public Object getCurrentWaveNumber() {
        return waveRound;
    }

}

class Wave2 {
    public ArrayList<Enemy> leftEnemies;
    public ArrayList<Enemy> rightEnemies;
    public int waveRound;
    public int spawnRate;
    public int waveId;
    private Game game;
    public WaveType type;

    public Wave2(Game game, int waveRound, WaveType type, int waveId) {
        this.game = game;
        this.waveRound = waveRound;
        this.type = type;
        this.waveId = waveId;
        leftEnemies = getNextWave();
        rightEnemies = getNextWave();
        this.spawnRate = getSpawnRate();
    }

    public boolean isDone() {
        for (int i = 0; i < leftEnemies.size(); i++) {
            if (!leftEnemies.get(i).isDead()) {
                return false;
            }
        }
        for (int i = 0; i < rightEnemies.size(); i++) {
            if (!rightEnemies.get(i).isDead()) {
                return false;
            }
        }
        return true;
    }

    private int getSpawnRate() {
        return 100;
    }

    public String getCoordinates() {
        String coords = "";

        coords += waveId + ",";
        for (int i = 0; i < leftEnemies.size(); i++) {
            leftEnemies.get(i).move(game, "left");
            String xy = leftEnemies.get(i).isDead() ? "4000 4000"
                    : "" + leftEnemies.get(i).getX() + " " + leftEnemies.get(i).getY();
            coords += xy + ",";
        }

        for (int i = 0; i < rightEnemies.size(); i++) {
            rightEnemies.get(i).move(game, "right");
            String xy = rightEnemies.get(i).isDead() ? "4000 4000"
                    : "" + rightEnemies.get(i).getX() + " " + rightEnemies.get(i).getY();
            coords += xy + ",";
        }

        coords +=  "W";
        return coords;
    }

    public ArrayList<Enemy> getNextWave() {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        // every 4th wave will have an additional set of fat skeletons
        if (waveRound % 4 == 0) {
            for (int i = 0; i < (waveRound / 2); i++) {
                enemies.add(new FatSkeleton()); // New enemy type
            }
        }
        if (waveRound % 12 == 0) {
            for (int i = 0; i < (waveRound / 12); i++) {
                enemies.add(new DeerSkull()); // New enemy type
            }
        }
        if (waveRound % 3 == 0) {
            for (int i = 0; i < ((int) waveRound * 1.5); i++) {
                enemies.add(new Devil()); // New enemy type
            }
        }
        if (waveRound == 2) {
            int numberOfNormalEnemies = 3;

            for (int i = 0; i < numberOfNormalEnemies; i++) {
                enemies.add(new Skeleton());
            }

            enemies.add(new FatSkeleton()); // New enemy type

            for (int i = 0; i < numberOfNormalEnemies; i++) {
                enemies.add(new Skeleton());
            }

        } else {
            for (int i = 0; i < waveRound; i++) {
                enemies.add(new Skeleton());
            }
        }
        return enemies;
    }
}
