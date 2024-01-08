file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/model/game/Game.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

action parameters:
offset: 1483
uri: file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/model/game/Game.java
text:
```scala
package dk.dtu.mtd.model.game;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Game implements Runnable {
    public int id;
    public Player player1;
    public Player player2;
    public Space space;

    public Game(int id, int playerID1, int playerID2) {
        this.id = id;
        this.player1 = new Player(playerID1, 150);
        this.player2 = new Player(playerID2, 150);
        space = new SequentialSpace();
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Tuple contens: ("request" , 'type of request' , 'player ID')
                handleGameRequest(space.get(new ActualField("request"),
                        new FormalField(String.class), new FormalField(Integer.class)));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void handleGameRequest(Object[] request) throws InterruptedException {
        if (request[1].toString().equals("exit")) {
            if ((int) request[2] == player1.id) {
                space.put("exit", player1.id);
                space.put("gameClosed", player2.id);
            } else {
                space.put("exit", player2.id);
                space.put("gameClosed", player1.id);
            }
        } else if (request[1].toString().equals("damage")) {
           @@ System.out.println("got damage request"); // TODO: discuss naming conventions in the group
            if ((int) request[2] == player1.id) {
                player2.setHealth(player2.getHealth() - 10);
                // ("damadge", newHealth, playerID)
                space.put("gui","damage", player1.getHealth(), player1.id);
                space.put("gui","damage", player2.getHealth(), player2.id);

                System.out.println("player2 recived damage");
            } else {
                player1.setHealth(player1.getHealth() - 10);
                space.put("gui","damage", player1.getHealth(), player1.id);
                space.put("gui","damage", player2.getHealth(), player2.id);

                System.out.println("player1 recived damage");
            }
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
	scala.meta.internal.pc.HoverProvider$.hover(HoverProvider.scala:34)
	scala.meta.internal.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:342)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator