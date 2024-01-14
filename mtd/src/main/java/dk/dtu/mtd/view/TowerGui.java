package dk.dtu.mtd.view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TowerGui extends ImageView {

    Circle circle;
    Rectangle square;

    public TowerGui(String type, int size, int radius, int towerId, int playerId, int x, int y) {
        this.setX(x - size/2);
        this.setY(y - size/2);
        this.setFitHeight(size);
        this.setFitWidth(size);

        circle = new Circle(x, y, radius);
        circle.setOpacity(0.2);
        circle.setVisible(false);
        circle.setMouseTransparent(true);

        square = new Rectangle(x - size / 2, y - size / 2, size, size);
        square.setOpacity(0);;


        if (type.equals("basicTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
        } else if (type.equals("superTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/SuperMonkey.png"));
        } else {
            this.setImage(new Image("dk/dtu/mtd/assets/skelly.gif"));
        }

        this.setId("" + towerId);

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GameGui.towerClicked(towerId, playerId);
                event.consume();
            }
       });

       this.setOnDragEntered(new EventHandler<DragEvent>() {
        @Override
        public void handle(DragEvent event) {
            GameGui.legalPlacmentHover(false);
        }
        });

        this.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                GameGui.legalPlacmentHover(true);
            }
        });

        square.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                GameGui.legalPlacmentHover(false);
            }
        });

        square.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                 GameGui.legalPlacmentHover(true);
            }
        });

    }

    public Circle getCircle() {
        return circle;
    }

    public Rectangle getSquare() {
        return square;
    }

    public void setCircleVisible(boolean visible) {
        circle.setVisible(visible);
    }
}
