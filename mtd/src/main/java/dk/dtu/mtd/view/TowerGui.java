package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class TowerGui extends ImageView {

    Circle circle;

    public TowerGui(String towerType, int size, int radius, int x, int y) {
        this.setTranslateX(x - size/2);
        this.setTranslateY(y - size/2);
        this.setFitHeight(size);
        this.setFitWidth(size);

        circle = new Circle(x, y, radius);
        circle.setOpacity(0.2);
        circle.setVisible(false);

        if (towerType.equals("basicTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/dartMonkey.png"));
        } else if (towerType.equals("superTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/SuperMonkey.png"));
        } else {
            this.setImage(new Image("dk/dtu/mtd/assets/skelly.gif"));
        }

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                circle.setVisible(true);
                event.consume();
            }
       });

       this.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                circle.setVisible(false);
                event.consume();
            }
       });
    }

    public Circle getCircle() {
        return circle;
    }
}
