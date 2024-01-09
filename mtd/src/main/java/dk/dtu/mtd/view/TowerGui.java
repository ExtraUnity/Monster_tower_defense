package dk.dtu.mtd.view;

import java.util.ResourceBundle.Control;

import dk.dtu.mtd.controller.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TowerGui extends ImageView {

    int size = 100;

    public TowerGui(String towerType, int x, int y) {
        this.setTranslateX(x - size/2);
        this.setTranslateY(y - size/2);
        this.setFitHeight(size);
        this.setFitWidth(size);

        if (towerType.equals( "basicTower")) {
            this.setImage(new Image("dk/dtu/mtd/assets/dartMonkey.png"));
        } else {
            this.setImage(new Image("dk/dtu/mtd/assets/SuperMonkey.png"));
        }

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Tower pressed ");
                Controller.placeTower("basicTower", 300, 200);
                event.consume();
            }
       });

    }

}
