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
import javafx.scene.input.MouseEvent;
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
    GameBottomGui bottom;
    static GameChat gameChat;
    static int lastSelected;
    static Button upgradeButton;
    private ImageView hoverImage;
    private Circle hoverCircle;

    public static double gameAreaHeight;
    public static double gameAreaWidth;

    public GameGui(String health1, String health2) {
        gameAreaHeight = Screen.getPrimary().getBounds().getHeight() - 200;
        gameAreaWidth = (gameAreaHeight / 9) * 16;
        layout = new VBox();
        gameArea = new StackPane();
        gameTop = new GameTopGui(health1, health2, 0);

        gameChat = new GameChat();
        towerLayer = towerLayer(gameAreaWidth, gameAreaHeight);
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

        gameWaveGuiLeft = new GameWaveGui(0, gameAreaWidth, gameAreaHeight);
        gameWaveGuiRight = new GameWaveGui(1, gameAreaWidth, gameAreaHeight);

        gameArea.getChildren().addAll(gameAreaBackground(gameAreaWidth, gameAreaHeight), gameWaveGuiLeft,
                gameWaveGuiRight, towerLayer);

        bottom = new GameBottomGui();

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
        setBackground(background());
        minWidth(2000);
        minHeight(2000);

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
        return new ImageView(new Image("dk/dtu/mtd/assets/GameArea.png", width, height, false, true));
    }

    public Background background() {

        Image background = new Image("dk/dtu/mtd/assets/Green.png", Gui.stage.getWidth(),
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


    public static void newTower(String type, int size, int radius, int towerId, int playerId, int x, int y) {
        System.out.println("I got a new tower!");
        TowerGui tower = new TowerGui(type, size, (int) ((gameAreaWidth * radius)/1920), towerId, playerId, (int) ((gameAreaWidth * x)/1920),  (int) ((gameAreaHeight * y)/1080));
        towerLayer.getChildren().add(0,tower.getCircle());
        towerLayer.getChildren().add(tower);
    }

    public static void towerClicked(int towerId, int playerId) {
        if (playerId != Controller.getPlayerId()) {
            return;
        }
        TowerGui tower = (TowerGui) towerLayer.lookup("#" + towerId);
        if (lastSelected == towerId) {
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

        newTowerLayer.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString()) {
                    try {
                        Controller.placeTower(dragboard.getString(), (int) ((1920 * event.getX()) / width),
                                (int) ((1080 * event.getY()) / height));
                    } catch (Exception e) {
                        System.out.println("Tower placement has failed");
                    }
                }
                hoverImage.setVisible(false);
                hoverCircle.setVisible(false);
                event.consume();
            }
        });

        newTowerLayer.setOnDragEntered(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString() && dragboard.getString() == "basicTower") {
                    hoverImage.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
                    hoverCircle.setRadius((gameAreaWidth * 300)/1920);
                } else if (dragboard.hasString() && dragboard.getString() == "aoeTower") {
                    hoverImage.setImage(new Image("dk/dtu/mtd/assets/dartMonkey.png"));
                    hoverCircle.setRadius((gameAreaWidth * 200)/1920);
                }
            }
            
        });

        newTowerLayer.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
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
                hoverImage.setVisible(false);
                ;
                hoverCircle.setVisible(false);
            }
        });

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (lastSelected != -1) {
                    TowerGui lastTower = (TowerGui) towerLayer.lookup("#" + lastSelected);
                    lastTower.setCircleVisible(false);
                    upgradeButton.setVisible(false);
                    lastSelected = -1;
                }
                System.out.println("Clicked at: (" + (int) ((1920 * event.getX()) / width) + "," + (int) ((1080 * event.getY()) / height) + ")");
                event.consume();
            }
       });

        return newTowerLayer;
    }


    public static void returnToLobbyPrompt() {
        Gui.root.getChildren().remove(0);
        Gui.root.getChildren().add(Gui.lobbyPrompt());
    }
}
