package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Gui extends Application {
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
    static GameGui game;

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

        //temporary for testing purpose
        Button joinGameButton = new Button("join game");
        
        joinGameButton.setOnAction(e -> {
            root.getChildren().remove(0);
            game = new GameGui();
            root.getChildren().add(game);
            Controller.joinGame();
        });
        root.getChildren().remove(0);
        root.getChildren().add(joinGameButton);
    }

    public static void closeGame(){
        root.getChildren().remove(game);
    }


    
    void setupStageMeta(Stage stage) {
        stage.setTitle("Monster Tower Defense");
        stage.setMaximized(true);
        stage.setMinWidth(400);
        stage.setMinHeight(400);
    }

}