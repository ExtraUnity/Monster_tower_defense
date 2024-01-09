package dk.dtu.mtd.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TowerGui extends ImageView {

    int size = 70;

    public TowerGui(String towerImage, int x, int y) {
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setFitHeight(size);
        this.setFitWidth(size);

        this.setImage(new Image("dk/dtu/mtd/assets/" + towerImage));


        this.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Tower pressed ");
                
                event.consume();
            }
       });

    }

}
