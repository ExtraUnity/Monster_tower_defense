package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TowerGui extends ImageView {

    public TowerGui(String towerType, int size, int x, int y) {
        this.setTranslateX(x - size/2);
        this.setTranslateY(y - size/2);
        this.setFitHeight(size);
        this.setFitWidth(size);

        if (towerType.equals("basicTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/dartMonkey.png"));
        } else if (towerType.equals("superTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/SuperMonkey.png"));
        } else {
            this.setImage(new Image("dk/dtu/mtd/assets/skelly.gif"));
        }

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                event.consume();
            }
       });

    }

}
