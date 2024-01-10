package dk.dtu.mtd.model.game;

public class GameTicker implements Runnable {
    public volatile int gameTick;
    private long deltaTime;
    private long previousTime;
    private long msPerTick;
    public GameTicker() {
        gameTick = 0;
        previousTime = System.nanoTime() / 1_000_000L;;
        deltaTime = 0;
        msPerTick = 1000/50;
    }
    @Override
    public void run() {
        while(true) {
            deltaTime = System.nanoTime() / 1_000_000L - previousTime;
            if(deltaTime > msPerTick) {
                gameTick++;
                deltaTime = 0;
                previousTime = System.nanoTime() / 1_000_000L;
            }
        }
    }
    
}
