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
    ImageView hoverImage;

    public double gameAreaHeight;
    public double gameAreaWidth;

    public GameGui(String health1, String health2) {
        layout = new VBox();
        gameArea = new StackPane();
        gameTop = new GameTopGui(health1, health2, 0);

        gameChat = new GameChat();
        towerLayer = towerLayer();
        hoverImage = new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif"));


        // confine the game area to be the same on all screens:
        gameAreaHeight = Screen.getPrimary().getBounds().getHeight() - 200;
        gameAreaWidth = (gameAreaHeight / 3) * 6;
        gameArea.setMaxWidth(gameAreaWidth);
        gameArea.setMaxHeight(gameAreaHeight);

        gameWaveGuiLeft = new GameWaveGui(gameAreaWidth, gameAreaHeight);
        gameWaveGuiRight = new GameWaveGui(gameAreaWidth, gameAreaHeight);

        gameArea.getChildren().addAll(gameAreaBackground(gameAreaWidth, gameAreaHeight), gameWaveGuiLeft, gameWaveGuiRight, towerLayer);

        BorderPane bottom = new BorderPane();
        bottom.setMaxHeight(100);
        ImageView chatButton = chatButton();
        bottom.setCenter(new GameShop());
        bottom.setRight(chatButton);
        BorderPane.setAlignment(chatButton, Pos.CENTER_RIGHT);

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(gameTop, gameArea, bottom);

        towerLayer.getChildren().add(hoverImage);
        hoverImage.setOpacity(0);
        hoverImage.setFitHeight(100);
        hoverImage.setFitWidth(100);

        getChildren().add(layout);
        setAlignment(Pos.CENTER);

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

    public ImageView gameAreaBackground(double width, double height) {
        return new ImageView(new Image("dk/dtu/mtd/assets/gameBackground.png", width, height, true, false));
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

    public static void newTower(String type, int size, int x, int y) {
        System.out.println("I got a new tower!");
        TowerGui tower = new TowerGui(type, size, x, y);
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
                hoverImage.setOpacity(0);
                event.consume();
            }
        });

        newTowerLayer.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString() && dragboard.getString() == "basicTower") {
                    hoverImage.setImage(new Image("dk/dtu/mtd/assets/dartMonkey.png"));
                }
                hoverImage.setOpacity(0.5);
                hoverImage.setX(event.getX() - hoverImage.getFitWidth() / 2);
                hoverImage.setY(event.getY() - hoverImage.getFitWidth() / 2);
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
