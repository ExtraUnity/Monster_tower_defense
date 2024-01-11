package dk.dtu.mtd.model.game;

public class GameTicker implements Runnable {
    public volatile int gameTick;
    private long deltaTime;
    private long previousTime;
    private long msPerTick;

    Game game;
    public GameTicker(Game game) {
        this.game = game;
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
                System.out.println(this.toString() + " currently at game tick " + gameTick);
                gameTick++;
                deltaTime = 0;
                previousTime = System.nanoTime() / 1_000_000L;
            }
        }
    }

    @Override 
    public String toString(){
        return "GameTicker" + game.id;
    }


    
}
