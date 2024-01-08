file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/view/MainMenuGui.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

action parameters:
uri: file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/view/MainMenuGui.java
text:
```scala
package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class MainMenuGui extends StackPane {
    static VBox menu;

    public MainMenuGui() {
        menu = new VBox();
        menu.setAlignment(Pos.CENTER);

        Button joinButton = new Button();
        joinButton.setText("Join Game");


        joinButton.setOnAction(e -> {
            Gui.loading();
        });

        menu.getChildren().add(joinButton);
        this.getChildren().add(menu);

    }


}
// kommenteret ud fordi man kunne joine sit eget spil.
/*
 * joinButton.setOnAction(e -> {
 * final Object mainMenu = this;
 * // Set new thread for joining game to avoid freezing the window.
 * Thread t = new Thread(new Runnable() {
 * Object p = mainMenu;
 * 
 * public void run() {
 * Controller.joinGame();
 * 
 * // Update gui on application thread as soon as this thread is available.
 * Platform.runLater(() -> {
 * Gui.root.getChildren().remove(p);
 * Gui.game = new GameGui(150); // TODO: plz fix
 * Gui.root.getChildren().add(Gui.game);
 * });
 * 
 * }
 * });
 * t.start();
 * 
 * });
 */
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