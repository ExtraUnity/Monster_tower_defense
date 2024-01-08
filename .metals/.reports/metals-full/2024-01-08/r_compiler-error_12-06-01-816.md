file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/model/Client.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

action parameters:
uri: file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/model/Client.java
text:
```scala
package dk.dtu.mtd.model;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;

import dk.dtu.mtd.controller.Controller;

public class Client {
    public RemoteSpace lobby;
    public RemoteSpace gameSpace;
    GameMonitor gameMonitor;
    public int id;
    private int gameId = -1;
    String hostIP;

    public Client() {

    }

    public void joinLobby(String serverIp) throws UnknownHostException, IOException, InterruptedException {
            // Join lobby
            this.hostIP = serverIp;
            lobby = new RemoteSpace("tcp://" + hostIP + ":37331/lobby?keep");
            // Get uniqe id from server
            lobby.put("request", "id", -1); // Request new id
            id = (int) lobby.get(new ActualField("id"), new FormalField(Integer.class))[1];
            System.out.println("Successful connection to lobby");

    }

    public void requestGame(){
        try {
            // Look for a game
            lobby.put("request", "game", id);
            gameId = (int) lobby.get(new ActualField("game"), new ActualField(id),
                    new FormalField(Integer.class))[2];
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("requesting a failed");
        }
    }

    public void joinGame() {
        try {
            // Join game
            gameSpace = new RemoteSpace("tcp://" + hostIP + ":37331/game" + gameId + "?keep");
            gameMonitor = new GameMonitor(this, gameSpace, lobby, id, gameId);
            new Thread(gameMonitor).start();
            System.out.println("Successful connection to game");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void damage(){
        try {
            System.out.println("send damage");
            gameSpace.put("request", "damage", 10, id);
            gameSpace.get(null)
            System.out.println(gameSpace.hashCode());

        } catch (Exception e) {
            System.out.println("Could not deal damage");
        }
    }

    public void exitGame() {
        try {
            // Exit a game
            System.out.println("exiting game");
            gameSpace.put("request", "exit", id);
            gameSpace.get(new ActualField("exit"), new ActualField(id));
            gameSpace.close();
        } catch (Exception e) {
            System.out.println("Not able to close a game");
        }
    }

    public void exit() {
        try {
            if (lobby != null) {
                exitGame();
                lobby.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getGameID() {
        return gameId;
    }

}

class GameMonitor implements Runnable {
    Client client;
    RemoteSpace gameSpace;
    RemoteSpace lobby;
    int gameId;
    int playerId;

    GameMonitor(Client client, RemoteSpace gameSpace, RemoteSpace lobby, int playerId, int gameId) {
        this.client = client;
        this.gameSpace = gameSpace;
        this.lobby = lobby;
        this.playerId = playerId;
        this.gameId = gameId;

    }

    @Override
    public void run() {
        try {
            gameSpace.get(new ActualField("gameClosed"), new ActualField(playerId));

            System.out.println("Other player left the game");
            Controller.exitGame();

            lobby.put("request", "closeGame", gameId);
            lobby.get(new ActualField("closedGame"), new ActualField(gameId));
            gameId = -1;

        } catch (InterruptedException e) {
            System.out.println("The game has been closed");
        }
    }
}


```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:933)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:168)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:45)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:90)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:99)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator