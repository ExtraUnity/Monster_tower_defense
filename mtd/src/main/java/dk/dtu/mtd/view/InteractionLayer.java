package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class InteractionLayer extends Pane {
    private ImageView hoverImage;
    private Circle hoverCircle;
    static Button upgradeButton;
    static int lastSelected;

    public InteractionLayer(double width, double height) {
        setMaxSize(width, height);
        hoverImage = new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif"));
        hoverCircle = new Circle(0, 0, ((300 * width) / 1920));
        upgradeButton = new Button();
        lastSelected = -1;

        this.getChildren().add(hoverImage);
        this.getChildren().add(hoverCircle);
        this.getChildren().add(upgradeButton);

        hoverImage.setOpacity(0.5);
        hoverImage.setFitHeight(100);
        hoverImage.setFitWidth(100);
        hoverImage.setVisible(false);

        hoverCircle.setOpacity(0.2);
        hoverCircle.setVisible(false);

        upgradeButton.setVisible(false);
        upgradeButton.setOnAction(e -> {
            Controller.upgradeTower(lastSelected);
        });

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

        this.setOnDragEntered(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString() && dragboard.getString() == "basicTower") {
                    hoverImage.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
                    hoverCircle.setRadius((width * 300) / 1920);
                } else if (dragboard.hasString() && dragboard.getString() == "aoeTower") {
                    hoverImage.setImage(new Image("dk/dtu/mtd/assets/AOEtower.png"));
                    hoverCircle.setRadius((width * 200) / 1920);
                }
            }

        });

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

        this.setOnDragExited(new EventHandler<DragEvent>() {
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
                    // TODO: this might be incorrect
                    TowerGui lastTower = (TowerGui) GameGui.interactionLayer.lookup("#" + lastSelected);
                    lastTower.setCircleVisible(false);
                    upgradeButton.setVisible(false);
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
            upgradeButton.setVisible(false);
            tower.setCircleVisible(false);
            lastSelected = -1;
        } else if (lastSelected == -1) {
            lastSelected = towerId;
            upgradeButton.setVisible(true);
            tower.setCircleVisible(true);
        } else {
            TowerGui lastTower = (TowerGui) GameGui.towerLayer.lookup("#" + lastSelected);
            lastTower.setCircleVisible(false);
            lastSelected = towerId;
            tower.setCircleVisible(true);
        }
    }
}
