package dk.dtu.mtd.model.game;

public class GameTicker implements Runnable {
    public volatile int gameTick;
    private long deltaTime;
    private long previousTime;
    private long msPerTick;
    final int INCOME_TICK_RATE = 500;
    boolean playing;

    Game game;
    public GameTicker(Game game) {
        this.playing = true;
        this.game = game;
        gameTick = 0;
        previousTime = System.nanoTime() / 1_000_000L;;
        deltaTime = 0;
        msPerTick = 1000/50;
    }
    @Override
    public void run() {
        int deltaIncomeTick = 0;
        int lastIncomeTick = gameTick;
        while(playing) {
            deltaTime = System.nanoTime() / 1_000_000L - previousTime;
            if(deltaTime > msPerTick) {
                gameTick++;
                deltaTime = 0;
                previousTime = System.nanoTime() / 1_000_000L;
            }
            deltaIncomeTick = gameTick - lastIncomeTick;
            if(deltaIncomeTick >= INCOME_TICK_RATE) {
                lastIncomeTick = gameTick;
                game.addIncome();
            }
        }
        try {
            game.gameSpace.put("gameTickerClosed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Game Ticker" + game.id + " closing");
    }

    @Override 
    public String toString(){
        return "GameTicker" + game.id;
    }


    
}
