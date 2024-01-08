package dk.dtu.mtd.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import dk.dtu.mtd.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    static VBox root;
    static GameGui game;
    static MainMenuGui mainMenu;

    @Override
    public void start(Stage primaryStage) {
        setupStageMeta(primaryStage);
        Gui.stage = primaryStage;
        root = new VBox();
        root.setAlignment(Pos.CENTER);

        Text textIp = new Text("Lobby IP:");

        String ownIp = "";
        try {
            ownIp = (InetAddress.getLocalHost().getHostAddress()).trim();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        TextField textFieldIp = new TextField(ownIp);
        textFieldIp.setPrefWidth(100);
        textFieldIp.setMaxWidth(100);

        Button joinLobbyButton = new Button("Join lobby");
        joinLobbyButton.setOnAction(e -> {
            new Thread() {
                public void run() {
                    String ip = textFieldIp.getText().toString();
                    try {
                        Controller.joinLobby(ip);
                        Platform.runLater(() -> {
                            root.getChildren().remove(textIp);
                            root.getChildren().remove(textFieldIp);
                            root.getChildren().remove(joinLobbyButton);
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
        root.getChildren().add(textIp);
        root.getChildren().add(textFieldIp);
        root.getChildren().add(joinLobbyButton);
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