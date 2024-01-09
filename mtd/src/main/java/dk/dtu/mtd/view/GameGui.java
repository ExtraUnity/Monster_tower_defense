package dk.dtu.mtd.view;

import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameGui extends StackPane {
    static BorderPane layout;
    StackPane gameArea;
    public static GameWaveGui gameWaveGuiLeft;
    public static GameWaveGui gameWaveGuiRight;

    static Pane towerLayer;
    static VBox game;
    static Text hp;
    static GameChat gameChat;

    public GameGui(int health) {
        layout = new BorderPane();
        gameArea = new StackPane();
        game = new VBox();
        hp = new Text("" + health);
        gameChat = new GameChat();
        towerLayer = new Pane();
        gameWaveGuiLeft = new GameWaveGui();
        gameWaveGuiRight = new GameWaveGui();

        gameArea.setMinWidth(1800);
        gameArea.setMinHeight(1000);
        gameArea.setMaxWidth(1800);
        gameArea.setMaxHeight(1000);
        towerLayer.setMinWidth(1800);
        towerLayer.setMinHeight(1000);
        towerLayer.setMaxWidth(1800);
        towerLayer.setMaxHeight(1000);

        gameArea.setBackground(background());
        gameArea.getChildren().addAll(gameWaveGuiLeft, gameWaveGuiRight);
        getChildren().add(gameArea);

        game.setAlignment(Pos.CENTER);

        Button counter = new Button("-10 for opponent");
        counter.setOnAction(e -> {
            Controller.damageEnemyToPlayer(10);
        });

        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });

        game.getChildren().addAll(hp, counter, exitGameButton);

        game.setPrefSize(500, 500);
        towerLayer.getChildren().add(game);

        layout.setCenter(towerLayer);
        TowerGui testTower = new TowerGui("basicTower", 200, 200);
        towerLayer.getChildren().add(testTower);

        ImageView chatButton = new ImageView(new Image("dk/dtu/mtd/assets/chatButton.jpg", 50, 50, true, false));
        chatButton.setOnMouseClicked(e -> {
            if (game.getChildren().contains(gameChat)) {
                game.getChildren().remove(gameChat);
            } else {
                game.getChildren().add(gameChat);
            }
        });
        BorderPane bottom = new BorderPane();
        bottom.setCenter(new GameShop());
        bottom.setRight(chatButton);
        BorderPane.setAlignment(chatButton, Pos.BOTTOM_RIGHT);
        layout.setBottom(bottom);

        this.setBackground(background());
        this.getChildren().add(layout);

        towerLayer.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString()) {
                    Controller.placeTower(dragboard.getString(), (int) event.getX(), (int) event.getY());
                }
                event.consume();
            }

        });

        towerLayer.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                // System.out.println("it owrk?");
                event.consume();
            }
        });
    }

    public void handleDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
    }

    public static void updateGameGui(int newHealth) {
        game.getChildren().remove(hp);
        hp = new Text("" + newHealth);
        game.getChildren().add(0, hp);
    }

    public Background background() {
        Image background = new Image("dk/dtu/mtd/assets/gameBackground.png", Gui.stage.getWidth(),
                Gui.stage.getHeight(), true, false);
        Background backgoundView = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(Gui.stage.getHeight(), Gui.stage.getWidth(), false, false, false, true)));
        return backgoundView;
    }

    public static void updateGameGui(LinkedList<String> newChat) {
        System.out.println("Got new chat list!");
        gameChat.chatList = newChat;
        gameChat.displayChat();
    }

    public static void newTower(String type, int x, int y) {
        System.out.println("I got a new tower!");
        TowerGui tower = new TowerGui(type, x, y);
        towerLayer.getChildren().add(tower);
    }
}
