file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/controller/Controller.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

action parameters:
offset: 891
uri: file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/controller/Controller.java
text:
```scala
package dk.dtu.mtd.controller;

import org.jspace.ActualField;
import org.jspace.FormalField;
import java.io.IOException;
import java.net.UnknownHostException;

import dk.dtu.mtd.model.Client;
import dk.dtu.mtd.view.GameGui;
import dk.dtu.mtd.view.Gui;
import javafx.application.Platform;

public class Controller {
    public static Controller controller;
    private static GUIMonitior guiMonitior;
    private static Thread guiThread;
    private static Client client = new Client("10.209.241.12");

    public static void initController() {
        controller = new Controller();
        guiMonitior = new GUIMonitior(client);
    }

    public static void joinLobby(String serverIp) throws UnknownHostException, IOException, InterruptedException {
        client.joinLobby(serverIp);
        System.out.println(serverIp);
    }

    public static void joinGa@@me() {
        client.requestGame();
        client.joinGame();
        guiThread = new Thread(guiMonitior);
        guiMonitior.playing = true;
        guiThread.start();
        Platform.runLater(() -> {
            Gui.game();
        });

    }

    public static void exitGame() {
        guiMonitior.playing = false;
        Gui.closeGame();
        client.exitGame();
    }

    public static void exit() {
        // exit the application
        guiMonitior.playing = false;
        client.exit();
    }

    public static void damage() {
        client.damage();
    }
}

// hmm
class GUIMonitior implements Runnable {
    Boolean playing = true;
    Client client;

    public GUIMonitior(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        Object[] update;
        while (playing) {
            try {
                // ("gui", (String) type, (int) data, playerId)
                update = client.gameSpace.get(new ActualField("gui"), new FormalField(String.class),
                        new FormalField(Integer.class), new ActualField(client.id));

                if (update[1].toString().equals("damage")) {
                    System.out.println("updating GUI");
                    final int hp = (int) update[2];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            GameGui.updateGameGui(hp);
                        }
                    });
                    // GameGui.updateGameGui((int) update[2]); throws an exeption because it's not
                    // on the GUI thread
                }
            } catch (InterruptedException e) {
                System.out.println("GUImonitor failing");
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