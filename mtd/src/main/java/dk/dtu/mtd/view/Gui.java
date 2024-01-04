package dk.dtu.mtd.view;

import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {
    private int counter = 0;
    private Button button = new Button();

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
    

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        this.button.setText("Im a counter! Click ME!!!");
        this.button.setOnAction(this::handleClick);
        StackPane root = new StackPane();
        root.getChildren().add(this.button);
        Scene scene = new Scene(root, 300, 250);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleClick(ActionEvent event) {
        this.counter++;
        this.button.setText("" + this.counter);
    }

    private void handleKey(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            this.counter++;
        } else if (event.getCode() == KeyCode.DOWN) {
            this.counter--;
        } else {
            return;
        }
        this.button.setText("" + this.counter);
    }


}