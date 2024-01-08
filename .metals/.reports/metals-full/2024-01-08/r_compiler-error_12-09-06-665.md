file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/view/GameGui.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

action parameters:
offset: 825
uri: file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/view/GameGui.java
text:
```scala
package dk.dtu.mtd.view;

import java.io.InputStream;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameGui extends StackPane {
    static VBox game;
    static Text hp;

    public GameGui(int health) {
        game = new VBox();
        game.setAlignment(Pos.CENTER);

        hp = new Text("" + health);

        Image skel = new Image("dk/dtu/mtd/assets/skelly.gif", 100 , 0, true, true);
        ImageView skelly = new ImageView(skel);

        Button counter = new Button("-10 for opponent");
        counter.setOnAction( e -> {
            Cont@@roller.damage();
        });

        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });

        game.getChildren().add(new Text("Game joined"));
        game.getChildren().addAll(skelly, hp ,counter,exitGameButton);
        this.getChildren().add(game);
    }

    public static void updateGameGui(int newHealth){
       game.getChildren().remove(hp);
       hp = new Text("" + newHealth);
       game.getChildren().add(2 , hp);
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