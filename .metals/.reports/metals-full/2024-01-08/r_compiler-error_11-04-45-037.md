file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/view/Gui.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

action parameters:
offset: 1946
uri: file:///C:/Users/kaspe/OneDrive/Dokumenter/GitHub/Monster_tower_defense/mtd/src/main/java/dk/dtu/mtd/view/Gui.java
text:
```scala
package dk.dtu.mtd.view;

import java.io.IOException;

import dk.dtu.mtd.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

    static Thread guiMainThread;

    public static void initGui() {
        guiMainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runGui();
            }
        });
        guiMainThread.start();
    }

    static void runGui() {
        launch(new String[] {});
    }

    static Stage stage;
    static StackPane root;
    static GameGui game;
    static MainMenuGui mainMenu;

    @Override
    public void start(Stage primaryStage) {
        setupStageMeta(primaryStage);
        Gui.stage = primaryStage;
        root = new StackPane();

        Button joinLobbyButton = new Button("Join lobby");
        joinLobbyButton.setOnAction(e -> {
            new Thread() {
                public void run() {

                    try {
                        Controller.joinLobby();
                        Platform.runLater(() -> {
                            root.getChildren().remove(0);
                            mainMenu = new MainMenuGui();
                            root.getChildren().add(mainMenu);
                        });
                    } catch (IOException | InterruptedException e) {
                        System.out.println("Connection to lobby failed.");
                    }

                }
            }.start();

        });

        TextField textIp = new TextField();

        Scene scene = new Scene(root);
        root.getChildren().add(joinLobbyButton);
        root@@
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void loading() {
        root.getChildren().remove(mainMenu);
        root.getChildren().add(new Text("Loading..."));
        Thread t = new Thread(() -> {
            Controller.joinGame();
        });
        t.start();
    }

    public static void game() {
        root.getChildren().remove(0);
        game = new GameGui(150);
        root.getChildren().add(game);
    }

    public static void closeGame() {
        System.out.println("Im going back to main menu");
        Platform.runLater(() -> {
            root.getChildren().remove(game);
            root.getChildren().add(mainMenu);
        });

    }

    void setupStageMeta(Stage stage) {
        stage.setOnCloseRequest(e -> {
            Controller.exit();
        });
        stage.setTitle("Monster Tower Defense");
        stage.setMaximized(true);
        stage.setMinWidth(400);
        stage.setMinHeight(400);
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
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:40)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:388)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator