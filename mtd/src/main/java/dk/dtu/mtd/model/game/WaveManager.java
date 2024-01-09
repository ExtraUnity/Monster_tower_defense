package dk.dtu.mtd.model.game;

public class WaveManager implements Runnable{

    private int currentWaveNumber;
    private int totalWaves; // Total number of waves
    private long waveInterval; // Time between waves
    private long lastWaveTime; // Time of last wave
    private boolean isWaveActive;

    // Constructor
    public WaveManager(int totalWaves, long waveInterval){
        this.totalWaves = totalWaves;
        this.waveInterval = waveInterval;
        this.currentWaveNumber = 0;
        this.lastWaveTime = System.currentTimeMillis();
        this.isWaveActive = false;
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
        


        // Increase difficulty, change enemy types, etc.
    }

    // Method to signal the end of a wave
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


    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }




    
}
