package dk.dtu.mtd.view;

import java.io.IOException;

import dk.dtu.mtd.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
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

        Scene scene = new Scene(root);
        root.getChildren().add(joinLobbyButton);
        primaryStage.setScene(scene);
        primaryStage.show();
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