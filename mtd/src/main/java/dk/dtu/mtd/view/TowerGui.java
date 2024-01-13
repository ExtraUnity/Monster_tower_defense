package dk.dtu.mtd.view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TowerGui extends ImageView {

    Circle circle;

    public TowerGui(String type, int size, int radius, int towerId, int playerId, int x, int y) {
        this.setX(x - size/2);
        this.setY(y - size/2);
        this.setFitHeight(size);
        this.setFitWidth(size);
        

        circle = new Circle(x, y, radius);
        circle.setOpacity(0.2);
        circle.setVisible(false);
        circle.setMouseTransparent(true);

        if (type.equals("basicTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
        } else if (type.equals("aoeTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/AOEtower.png"));
        } else {
            this.setImage(new Image("dk/dtu/mtd/assets/skelly.gif"));
        }        
        
        this.setId("" + towerId);


    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircleVisible(boolean visible) {
        circle.setVisible(visible);
    }
}
