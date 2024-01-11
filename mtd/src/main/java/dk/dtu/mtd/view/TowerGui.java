package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.model.game.Tower;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class TowerGui extends ImageView {

    Circle circle;

    public TowerGui(Tower tower, int x, int y) {
        this.setX(x - tower.getSize()/2);
        this.setY(y - tower.getSize()/2);
        this.setFitHeight(tower.getSize());
        this.setFitWidth(tower.getSize());

        circle = new Circle(x, y, tower.getRadius());
        circle.setOpacity(0.2);
        circle.setVisible(false);
        circle.setMouseTransparent(true);

        if (tower.getType().equals("basicTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
        } else if (tower.getType().equals("superTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/SuperMonkey.png"));
        } else {
            this.setImage(new Image("dk/dtu/mtd/assets/skelly.gif"));
        }

        this.setId("" + tower.getTowerId());

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                GameGui.towerClicked(tower.getTowerId(), tower.getPlayerId());
                event.consume();
            }
       });
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircleVisible(boolean visible) {
        circle.setVisible(visible);
    }
}
