package dk.dtu.mtd.view;

import java.util.LinkedList;

import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;

public class GameGui extends StackPane {
    static StackPane root;
    static VBox layout;
    static public StackPane gameArea;
    public static GameWaveGui gameWaveGuiLeft;
    public static GameWaveGui gameWaveGuiRight;

    static InteractionLayer interactionLayer;
    static Pane towerLayer;
    public static GameTopGui gameTop;
    public static GameBottomGui bottom;
    static GameChat gameChat;

    public static double gameAreaHeight;
    public static double gameAreaWidth;

    public GameGui() {
        gameAreaHeight = Screen.getPrimary().getBounds().getHeight() - 250;
        gameAreaWidth = (gameAreaHeight / 9) * 16;

        root = new StackPane();
        layout = new VBox();
        gameArea = new StackPane();
        gameTop = new GameTopGui("150", "150", 0, "left");
        gameChat = new GameChat();

        interactionLayer = new InteractionLayer(gameAreaWidth, gameAreaHeight);
        towerLayer = new Pane();
        towerLayer.setMaxSize(gameAreaWidth, gameAreaHeight);

        // confine the game area to be the same on all screens:
        gameArea.setMaxSize(gameAreaWidth, gameAreaHeight);

        gameWaveGuiLeft = new GameWaveGui(0, gameAreaWidth, gameAreaHeight);
        gameWaveGuiRight = new GameWaveGui(1, gameAreaWidth, gameAreaHeight);

        gameArea.getChildren().addAll(gameAreaBackground(gameAreaWidth, gameAreaHeight), gameWaveGuiLeft,
                gameWaveGuiRight, towerLayer, gameAreaForeground(gameAreaWidth, gameAreaHeight), interactionLayer);

        bottom = new GameBottomGui();

        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(gameTop, gameArea, bottom);

        root.getChildren().add(layout);
        getChildren().add(root);
        setAlignment(Pos.CENTER);
        setBackground(background());
        minWidth(2000);
        minHeight(2000);
        root.setAlignment(Pos.CENTER);
        root.setBackground(background());
        root.minWidth(2000);
        root.minHeight(2000);

    }

    public static void addPath (LinkedList<String> pathSections) {
        for (int i = 0 ; i < pathSections.size() ; i++ ) {
            String[] coordinates = pathSections.get(i).split("\\s+");

            double cordX = (gameAreaWidth * Integer.valueOf(coordinates[0])) / 1920;
            double cordY = (gameAreaHeight * Integer.valueOf(coordinates[1])) / 1080;
            double sizeX = (gameAreaWidth * Integer.valueOf(coordinates[2])) / 1920;
            double sizeY = (gameAreaHeight * Integer.valueOf(coordinates[3])) / 1080;

            Rectangle pathSquare =  new Rectangle(cordX, cordY, sizeX, sizeY);
            interactionLayer.getChildren().add(pathSquare);

            pathSquare.setOpacity(0);

            pathSquare.setOnDragEntered(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    interactionLayer.legalPlacmentHover(false);
                }
            });

            pathSquare.setOnDragExited(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    interactionLayer.legalPlacmentHover(true);
                }
            });
            
        }
    }

    public static void addNewWaveGui(GameWaveGui newWaveGui) {
        gameArea.getChildren().add(3,newWaveGui);
    }

    public void removeWaveGui(GameWaveGui waveGui) {
        gameArea.getChildren().remove(waveGui);
    }

    public void handleDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
    }

    public static void displayWin() {
        WinnerDisplay winnerDisplay = new WinnerDisplay("YOU WON!!!");
        root.getChildren().add(winnerDisplay);
    }

    public static void displayLose() {
        WinnerDisplay loserDisplay = new WinnerDisplay("YOU LOST :(");
        root.getChildren().add(loserDisplay);
    }

    public static void updateUpgradePrice(int towerId, int newPrice, String newStats){
        TowerGui tower = (TowerGui) towerLayer.lookup("#" + towerId);
        tower.updateUpgradePrice(newPrice);
        tower.updateStatsAsString(newStats);
        InteractionLayer.towerSelectedGui.updateUpgradePrice(newPrice);
        InteractionLayer.towerSelectedGui.updateStats(newStats);
    }

    public static void removeTower(int towerId) {
        TowerGui tower = (TowerGui) towerLayer.lookup("#" + towerId);
        towerLayer.getChildren().remove(tower);
        InteractionLayer.towerSelectedGui.setVisible(false);
        InteractionLayer.lastSelected = -1;
        Rectangle towerClickBox = (Rectangle) interactionLayer.lookup("#" + towerId);
        interactionLayer.getChildren().remove(towerClickBox);

    }


    public ImageView gameAreaBackground(double width, double height) {
        return new ImageView(new Image("dk/dtu/mtd/assets/GameArea.png", width, height, false, true));
    }

    public ImageView gameAreaForeground(double width, double height) {
        return new ImageView(new Image("dk/dtu/mtd/assets/GameAreaForeground.png", width, height, false, true));
    }

    public Background background() {

        Image background = new Image("dk/dtu/mtd/assets/Green.png", Gui.stage.getWidth(),
                Gui.stage.getHeight(), true, false);
        Background backgoundView = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(Gui.stage.getHeight(), Gui.stage.getWidth(), false, false, false, true)));

        return backgoundView;
    }

    public static void updateGameGui(String msg) {
        gameChat.chatList.add(msg);
        gameChat.displayChat();
    }

    public static void newTower(String type, int size, int radius, int towerId, int playerId, int x, int y) {
        TowerGui tower = new TowerGui(type, size, (int) ((gameAreaWidth * radius) / 1920), towerId, playerId,
                (int) ((gameAreaWidth * x) / 1920), (int) ((gameAreaHeight * y) / 1080));
        towerLayer.getChildren().add(tower);
        
        Rectangle clickArea = new Rectangle(size, size);

        clickArea.setX(((gameAreaWidth * (x - (size / 2))) / 1920));
        clickArea.setY(((gameAreaHeight * (y - (size /2))) / 1080));

        clickArea.setId("" + towerId);
        clickArea.setFill(Color.TRANSPARENT);

        clickArea.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                interactionLayer.getChildren().remove(InteractionLayer.towerSelectedGui);
                interactionLayer.getChildren().add(InteractionLayer.towerSelectedGui);
                interactionLayer.towerClicked(towerId, playerId);
                event.consume();
            }
        });

        clickArea.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                interactionLayer.legalPlacmentHover(false);
            }
        });

        clickArea.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                interactionLayer.legalPlacmentHover(true);
            }
        });

        interactionLayer.getChildren().add(0,clickArea);
    }

    public static void towerShoot(int towerId) {
        TowerGui tower = (TowerGui) towerLayer.lookup("#" + towerId);
        tower.shoot();
    }

    public static void returnToLobbyPrompt() {
        Gui.root.getChildren().remove(0);
        Gui.root.getChildren().add(Gui.lobbyPrompt());
    }
}
