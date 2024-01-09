package dk.dtu.mtd.model.game;

public class WaveManager implements Runnable{

    private int currentWaveNumber;
    private int totalWaves; // Total number of waves
    private long waveInterval; // Time between waves
    private long lastWaveTime; // Time of last wave
    private boolean isWaveActive;
    //public Thread waveManagerThread;
    private boolean running;



    // Constructor
    public WaveManager(int totalWaves, long waveInterval){
        this.totalWaves = totalWaves;
        this.waveInterval = waveInterval;
        this.currentWaveNumber = 0;
        this.lastWaveTime = System.currentTimeMillis();
        this.isWaveActive = false;
        //this.waveManagerThread = new Thread(new WaveManager(totalWaves, waveInterval));
        this.running = true;
    }

    // Called every game tick
    public void update() {
        if (isTimeForNextWave() && !isWaveActive) {
            startNextWave();
        }
        
    }

    // Starts the next wave if time is right
    private boolean isTimeForNextWave() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastWaveTime) >= waveInterval;
    }

    private void startNextWave() {
        if (currentWaveNumber < totalWaves) {
            currentWaveNumber++;
            isWaveActive = true;
            lastWaveTime = System.currentTimeMillis();
            spawnWave(currentWaveNumber);
        }
    }

    private void spawnWave(int waveNumber) {
        // Logic to spawn enemies based on the wave number
        System.out.println("Spawning wave " + currentWaveNumber);

        // Increase difficulty, change enemy types, etc.
    }

    // Method to signal the end of a wave. IMPLEMENT THIS IN GUI TO SIGNAL NEXT WAVE WILL START AFTER A CERTAIN TIME
    public void endWave() {
        isWaveActive = false;
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

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            update();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }   
        }
    }
   
}
