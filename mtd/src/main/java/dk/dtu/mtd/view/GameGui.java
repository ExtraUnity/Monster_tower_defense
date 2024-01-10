package dk.dtu.mtd.view;

import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
import javafx.stage.Screen;

public class GameGui extends StackPane {
    static VBox layout;
    StackPane gameArea;
    public static GameWaveGui gameWaveGuiLeft;
    public static GameWaveGui gameWaveGuiRight;

    static Pane towerLayer;
    static GameTopGui gameTop;
    static GameChat gameChat;

    public GameGui(String health1, String health2) {
        layout = new VBox();
        gameArea = new StackPane();
        gameTop = new GameTopGui(health1, health2, 0);

        gameChat = new GameChat();
        towerLayer = towerLayer();
        gameWaveGuiLeft = new GameWaveGui();
        gameWaveGuiRight = new GameWaveGui();

        // confine the game area to be the same on all screens:
        double gameAreaHeight = Screen.getPrimary().getBounds().getHeight() - 300;
        double gameAreaWidth = (gameAreaHeight / 3) * 6;
        gameArea.setMaxWidth(gameAreaWidth);
        gameArea.setMaxHeight(gameAreaHeight);

        // this defines the area towera can be placed in:
        // towerLayer.setMaxWidth(gameAreaWidth);
        // towerLayer.setMaxHeight(gameAreaHeight);

        gameArea.getChildren().addAll(gameAreaBackground(), gameWaveGuiLeft, gameWaveGuiRight, towerLayer);

        // TowerGui testTower = new TowerGui("basicTower", 200, 200);
        // towerLayer.getChildren().add(testTower);

        BorderPane bottom = new BorderPane();
        bottom.setMaxHeight(200);
        ImageView chatButton = chatButton();
        bottom.setCenter(new GameShop());
        bottom.setRight(chatButton);
        BorderPane.setAlignment(chatButton, Pos.CENTER_RIGHT);

        // remember to re-add bottom
        layout.getChildren().addAll(gameTop, gameArea);

        setBackground(background());
        getChildren().add(layout);

    }

    public void handleDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
    }

    public static void updateGameGui(String newHealth1, String newHealth2) {
        gameTop.layout.getChildren().remove(gameTop.healthPlayer1);
        gameTop.layout.getChildren().remove(gameTop.healthPlayer2);

        gameTop.healthPlayer1 = new Text("" + newHealth1);
        gameTop.healthPlayer2 = new Text("" + newHealth2);

        gameTop.layout.getChildren().add(0, gameTop.healthPlayer1);
        gameTop.layout.getChildren().add(2, gameTop.healthPlayer2);
    }

    public ImageView gameAreaBackground() {
        return new ImageView(new Image("dk/dtu/mtd/assets/gameBackground.png", 1800, 900, true, false));
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

    public Pane towerLayer() {
        Pane newTowerLayer = new Pane();
        newTowerLayer.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString()) {
                    Controller.placeTower(dragboard.getString(), (int) event.getX(), (int) event.getY());
                }
                event.consume();
            }
        });

        newTowerLayer.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                // System.out.println("it owrk?");
                event.consume();
            }
        });
        return newTowerLayer;
    }

    public ImageView chatButton() {
        ImageView chatButton = new ImageView(new Image("dk/dtu/mtd/assets/chatButton.jpg", 50, 50, true, false));
        chatButton.setOnMouseClicked(e -> {
            if (gameTop.getChildren().contains(gameChat)) {
                gameTop.getChildren().remove(gameChat);
            } else {
                gameTop.getChildren().add(gameChat);
            }
        });

        return chatButton;
    }
}
