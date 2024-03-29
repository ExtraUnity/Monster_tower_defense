package dk.dtu.mtd.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import dk.dtu.mtd.controller.Controller;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    static Text loadingText;
    static VBox prompt;
    static Background background;

    @Override
    public void start(Stage primaryStage) {
        setupStageMeta(primaryStage);
        Gui.stage = primaryStage;
        root = new StackPane();
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);

        root.getChildren().add(lobbyPrompt());
        primaryStage.setScene(scene);
        primaryStage.show();
        background = new Background(new BackgroundImage(new Image("dk/dtu/mtd/assets/TowerDefenceBackground.png"),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,new BackgroundSize(1920, 1080, false, false, false, false)));
        root.setBackground(background);
    }

    public static void loading() {
        root.getChildren().remove(mainMenu);
        loadingText = new Text("Waiting for other player...");
        if(root.getChildren().contains(loadingText)) {
            return;
        }
        root.getChildren().add(loadingText);
        Thread t = new Thread(() -> {
            Controller.joinGame();
        });
        t.start();
    }

    public static void game() {
        root.getChildren().remove(0);
        game = new GameGui();
        root.getChildren().add(game);
    }

    public static void closeGame() {
        System.out.println("Im going back to main menu");

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> 
        Platform.runLater(() -> {
            root.getChildren().remove(game);
            root.getChildren().add(mainMenu);
        })
        );
        pause.play();

    }

    public static void loadMainMenu() {
        root.getChildren().remove(prompt);
        if(root.getChildren().contains(mainMenu)) {
            return;
        }
        mainMenu = new MainMenuGui();
        root.getChildren().add(mainMenu);
    }

    public static VBox lobbyPrompt() {
        prompt = new VBox(5);
        prompt.setAlignment(Pos.CENTER);
        Text textIp = new Text("Lobby IP:");

        String ownIP = "";
        try {
            ownIP = (InetAddress.getLocalHost().getHostAddress()).trim();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        TextField textFieldIp;
        textFieldIp = new TextField(ownIP);
        textFieldIp.setMaxWidth(200);

        Button joinLobbyButton = new Button("Join lobby");
        joinLobbyButton.setOnAction(e -> {
            new Thread() {
                public void run() {
                    String ip = textFieldIp.getText().toString();
                    try {
                        Controller.joinLobby(ip);
                        Platform.runLater(() -> {
                            loadMainMenu();
                        });
                    } catch (IOException | InterruptedException e) {
                        System.out.println("Connection to lobby failed.");
                    }
                }
            }.start();
        });

        prompt.getChildren().add(textIp);
        prompt.getChildren().add(textFieldIp);
        prompt.getChildren().add(joinLobbyButton);

        return prompt;
    }

    void setupStageMeta(Stage stage) {
        stage.setOnCloseRequest(e -> {
            Controller.exitApplication();
        });
        stage.setTitle("Monster Tower Defense");
        stage.setMaximized(true);
        stage.setMinWidth(1800);
        stage.setMinHeight(900);
    }

}