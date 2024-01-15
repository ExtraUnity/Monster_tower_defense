package dk.dtu.mtd.view;

import javax.swing.GroupLayout.Alignment;

import dk.dtu.mtd.controller.Controller;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class InteractionLayer extends Pane {
    private ImageView hoverImage;
    private Circle hoverCircle;

    static TowerSelectedGui towerSelectedGui;
    static int lastSelected;

    public InteractionLayer(double width, double height) {
        setMaxSize(width, height);
        hoverImage = new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif"));
        hoverCircle = new Circle(0, 0, ((300 * width) / 1920));
        
        towerSelectedGui = new TowerSelectedGui();
        lastSelected = -1;
        towerSelectedGui.setVisible(false);

        this.getChildren().add(hoverImage);
        this.getChildren().add(hoverCircle);
        this.getChildren().add(towerSelectedGui);

        hoverImage.setOpacity(0.5);
        hoverImage.setFitHeight(100);
        hoverImage.setFitWidth(100);
        hoverImage.setVisible(false);

        hoverCircle.setOpacity(0.2);
        hoverCircle.setVisible(false);



        // Finalizing the placement of a tower
        this.setOnDragDropped(new EventHandler<DragEvent>() {
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

        // Placing a tower
        this.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString() && dragboard.getString() == "basicTower") {
                    hoverImage.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
                    // range cirkle
                    hoverCircle.setRadius((width * 300) / 1920);
                } else if (dragboard.hasString() && dragboard.getString() == "aoeTower") {
                    hoverImage.setImage(new Image("dk/dtu/mtd/assets/AOEtower.png"));
                    // range cirkle
                    hoverCircle.setRadius((width * 200) / 1920);
                }
            }

        });

        // 
        this.setOnDragOver(new EventHandler<DragEvent>() {
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

        // Make hover images invisible
        this.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                hoverImage.setVisible(false);
                hoverCircle.setVisible(false);
            }
        });

        // I don't even know
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (lastSelected != -1) {
                    TowerGui lastTower = (TowerGui) GameGui.towerLayer.lookup("#" + lastSelected);
                    lastTower.setCircleVisible(false);
                    towerSelectedGui.setVisible(false);
                    lastSelected = -1;
                }
                event.consume();
            }
        });
    }

    public void towerClicked(int towerId, int playerId) {
        if (playerId != Controller.getPlayerId()) {
            return;
        }
        TowerGui tower = (TowerGui) GameGui.towerLayer.lookup("#" + towerId);
        if (lastSelected == towerId) {
            towerSelectedGui.setVisible(false);
            tower.setCircleVisible(false);
            lastSelected = -1;
        } else if (lastSelected == -1) {
            lastSelected = towerId;
            towerSelectedGui.updateGui(tower.name, tower.stats,tower.upgradePrice, tower.sellPrice, tower.damageUpgrade, towerId);
            towerSelectedGui.setVisible(true);
            //upgradeButton.relocate(tower.x - (upgradeButton.getWidth()/2), tower.y + 50);
            towerSelectedGui.relocate(tower.x + 50, tower.y - 50);

            tower.setCircleVisible(true);
        } else {
            TowerGui lastTower = (TowerGui) GameGui.towerLayer.lookup("#" + lastSelected);
            lastTower.setCircleVisible(false);
            lastSelected = towerId;
            towerSelectedGui.updateGui(tower.name, tower.stats,  tower.upgradePrice, tower.sellPrice, tower.damageUpgrade, towerId);
            
            tower.setCircleVisible(true);
            
            //upgradeButton.relocate(tower.x - (upgradeButton.getWidth()/2), tower.y + 50);
            towerSelectedGui.relocate(tower.x + 50, tower.y - 50);
        }
    }
}

class TowerSelectedGui extends StackPane {
    static VBox layout;
    static Text name;
    static Text stats;
    static Text upgradePrice;
    static HBox upgradeArea;
    static int sellPrice;
    static int damageBonus;
    static Button upgradeButton;
    static Button sellButton;

    public void updateGui(String name, String stats, int upgradePrice, int sellPrice, int damageBonus, int towerSelected) {
        String nameString;
        switch (name) {
            case "basicTower":
                nameString = "Basic Tower";
                break;
            case "aoeTower":
                nameString = "AOE Tower";
                break;
            default:
                nameString = "No Name";
                break;
        }
        TowerSelectedGui.name.setText(nameString);
        TowerSelectedGui.sellPrice = sellPrice;
        TowerSelectedGui.damageBonus = damageBonus;
        TowerSelectedGui.upgradePrice.setText(String.valueOf(upgradePrice));
        TowerSelectedGui.stats.setText(stats);

        upgradeButton.setOnAction(e -> {
            Controller.upgradeTower(towerSelected);
        });
        sellButton.setOnAction(e -> {
            Controller.sellTower(towerSelected);
            this.setVisible(false);
        });
    }

    public TowerSelectedGui() {
        super();

        layout = new VBox(10);
        upgradeArea = new HBox(5);

        this.setMinSize(130 , 140);
        this.setBackground(background());

        sellButton = new Button("Sell Tower");
        stats = new Text();

        upgradeButton = new Button("Upgrade");
        upgradePrice = new Text();
        upgradePrice.setFont(new Font(25));
        upgradePrice.setStroke(Color.WHITE);

        upgradeArea.setAlignment(Pos.CENTER);
        upgradeArea.getChildren().addAll(upgradeButton, upgradePrice);

        name = new Text();
        name.setFont(new Font(25));
        name.setStroke(Color.WHITE);

        layout.setAlignment(Pos.CENTER);

        name.setTextAlignment(TextAlignment.CENTER);
        layout.getChildren().addAll(name, stats, upgradeArea, sellButton);
        this.getChildren().add(layout);
    }

    private Background background() {
        return new Background(new BackgroundFill(Color.valueOf("#ffffff"), new CornerRadii(5), new Insets(0)));
    }

    public Text getUpgradePrice() {
        return upgradePrice;
    }

    public void updateUpgradePrice(int newPrice){
        upgradePrice.setText(String.valueOf(newPrice));
    }

    public void updateStats(String newStats){
        stats.setText(newStats);
    }


}