package dk.dtu.mtd.view;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

public class TowerShop extends HBox {

    public TowerShop() {
        setAlignment(Pos.CENTER_RIGHT);

        final ImageView basicTower = new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true));
        this.getChildren().add(basicTower);

        basicTower.addEventHandler(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = basicTower.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("basicTower");
                db.setContent(content);
                
                event.consume();
            }
       });
       final ImageView superTower = new ImageView(new Image("dk/dtu/mtd/assets/SuperMonkey.png", 100, 0, true, true));
        this.getChildren().add(superTower);

        superTower.addEventHandler(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = superTower.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("superTower");
                db.setContent(content);
                
                event.consume();
            }
       });
    }
}
