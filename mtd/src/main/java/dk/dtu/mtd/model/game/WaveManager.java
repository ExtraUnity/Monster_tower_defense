package dk.dtu.mtd.model.game;

public class WaveManager implements Runnable{
    boolean playing;
    int waveRound;
    Thread waveManeger;


    public WaveManager(){
        this.playing = true;
        this.waveRound = 1;
    }

    //TODO: migh not be needed
    void initWaveManager(){
        this.waveManeger = new Thread(new WaveManager());
        waveManeger.start();
    }    


    @Override
    public void run() {
        while (playing){
            spawnWave(waveRound);
            waveRound ++;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println("Wavemanger failed to sleep after round:" + (waveRound - 1));
            }
        }
    }



    void spawnWave(int wave){
        System.out.println("Spawning wave " + wave);
        
    }
    
}
