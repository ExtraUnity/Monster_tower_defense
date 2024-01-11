package dk.dtu.mtd.view;

import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.model.game.Tower;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class GameGui extends StackPane {
    static VBox layout;
    static public StackPane gameArea;
    public static GameWaveGui gameWaveGuiLeft;
    public static GameWaveGui gameWaveGuiRight;

    static Pane towerLayer;
    static GameTopGui gameTop;
    static GameChat gameChat;
    static int lastSelected;
    static Button upgradeButton;
    private ImageView hoverImage;
    private Circle hoverCircle;

    public static double gameAreaHeight;
    public static double gameAreaWidth;

    public GameGui(String health1, String health2) {
        gameAreaHeight = Screen.getPrimary().getBounds().getHeight() - 200;
        gameAreaWidth = (gameAreaHeight / 3) * 5;
        layout = new VBox();
        gameArea = new StackPane();
        gameTop = new GameTopGui(health1, health2, 0);

        gameChat = new GameChat();
        towerLayer = towerLayer(gameAreaWidth, gameAreaHeight);
        towerLayer.setBorder(new Border(new BorderStroke(Color.BLACK, 
            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        hoverImage = new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif"));
        upgradeButton = new Button();

        lastSelected = -1;

        upgradeButton.setVisible(false);
        towerLayer.getChildren().add(upgradeButton);
        upgradeButton.setOnAction(e -> {
            Controller.upgradeTower(lastSelected);
            System.out.println(lastSelected);
        });
        hoverCircle = new Circle(0, 0, 300);



        // confine the game area to be the same on all screens:
        gameArea.setMaxWidth(gameAreaWidth);
        gameArea.setMaxHeight(gameAreaHeight);
      
        gameWaveGuiLeft = new GameWaveGui(0 ,gameAreaWidth, gameAreaHeight);
        gameWaveGuiRight = new GameWaveGui(1 ,gameAreaWidth, gameAreaHeight);

        gameArea.getChildren().addAll(gameAreaBackground(gameAreaWidth, gameAreaHeight), gameWaveGuiLeft,
                gameWaveGuiRight, towerLayer);

        BorderPane bottom = new BorderPane();
        bottom.setMaxHeight(100);
        ImageView chatButton = chatButton();
        bottom.setCenter(new GameShop());
        bottom.setRight(chatButton);
        BorderPane.setAlignment(chatButton, Pos.CENTER_RIGHT);

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(gameTop, gameArea, bottom);

        towerLayer.getChildren().add(hoverImage);
        towerLayer.getChildren().add(hoverCircle);

        hoverImage.setOpacity(0.5);
        hoverImage.setFitHeight(100);
        hoverImage.setFitWidth(100);
        hoverImage.setVisible(false);

        hoverCircle.setOpacity(0.2);
        hoverCircle.setVisible(false);

        getChildren().add(layout);
        setAlignment(Pos.CENTER);

    }

    public static void addNewWaveGui(GameWaveGui newWaveGui) {
        gameArea.getChildren().remove(towerLayer);
        gameArea.getChildren().add(newWaveGui);
        gameArea.getChildren().add(towerLayer);
    }

    public void removeWaveGui(GameWaveGui waveGui) {
        gameArea.getChildren().remove(waveGui);
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
        return new ImageView(new Image("dk/dtu/mtd/assets/gameBackground.png", width, height, false, false));
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

    public static void newTower(Tower objektTower) {
        System.out.println("I got a new tower!");
        TowerGui tower = new TowerGui(objektTower, (int) ((gameAreaWidth * objektTower.getX())/1920),  (int) ((gameAreaHeight * objektTower.getY())/1080));
        towerLayer.getChildren().add(0,tower.getCircle());
        towerLayer.getChildren().add(tower);
    }

    public static void towerClicked(int towerId, int playerId) {
        if (playerId != Controller.getPlayerId()) {
            return;
        }
        TowerGui tower = (TowerGui) towerLayer.lookup("#" + towerId);
        if(lastSelected == towerId) {
            upgradeButton.setVisible(false);
            tower.setCircleVisible(false);
            lastSelected = -1;
        } else if (lastSelected == -1) {
            lastSelected = towerId;
            upgradeButton.setVisible(true);
            tower.setCircleVisible(true);
        } else {
            TowerGui lastTower = (TowerGui) towerLayer.lookup("#" + lastSelected);
            lastTower.setCircleVisible(false);
            lastSelected = towerId;
            tower.setCircleVisible(true);
        }
    }

    public Pane towerLayer(double width, double height) {
        setMaxSize(width, height);

        Pane newTowerLayer = new Pane();
        Robot robot = new Robot();
        newTowerLayer.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                Color color = robot.getPixelColor(robot.getMouseX(), robot.getMouseY());
                if (dragboard.hasString() && color.getGreen() > 0.27 && color.getGreen() < 0.37) {
                    Controller.placeTower(dragboard.getString(), (int) ((1920*event.getX())/width), (int) ((1080*event.getY())/height));
                }
                hoverImage.setVisible(false);
                hoverCircle.setVisible(false);
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
                hoverImage.setVisible(true);
                hoverImage.setX(event.getX() - hoverImage.getFitWidth() / 2);
                hoverImage.setY(event.getY() - hoverImage.getFitWidth() / 2);
                hoverCircle.setVisible(true);
                hoverCircle.setCenterX(event.getX());
                hoverCircle.setCenterY(event.getY());
                event.consume();
            }
        });

        newTowerLayer.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                hoverImage.setVisible(false);;
                hoverCircle.setVisible(false);
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
