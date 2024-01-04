package dk.dtu.mtd.view;

import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import dk.dtu.mtd.controller.Controller;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    static StackPane root;

    @Override
    public void start(Stage primaryStage) {
        setupStageMeta(primaryStage);
        Gui.stage = primaryStage;
        root = new StackPane();
        button.setText("Click to join lobby");
        button.setOnAction(this::handleClick);
        root.getChildren().add(this.button);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleClick(ActionEvent event) {
        Controller.joinLobby();
    }


    
    void setupStageMeta(Stage stage) {
        stage.setTitle("Monster Tower Defense");
        stage.setMaximized(true);
        stage.setMinWidth(400);
        stage.setMinHeight(400);
    }

}