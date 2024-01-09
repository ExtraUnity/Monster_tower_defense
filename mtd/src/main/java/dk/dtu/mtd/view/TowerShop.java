package dk.dtu.mtd.view;

import java.util.ArrayList;

import dk.dtu.mtd.controller.Controller;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TowerShop extends HBox {

    public TowerShop() {
        ArrayList<ImageView> items = new ArrayList<ImageView>();
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));

        this.getChildren().addAll(items);
        setAlignment(Pos.CENTER_RIGHT);

        final ImageView source = new ImageView(new Image("dk/dtu/mtd/assets/SuperMonkey.png", 100, 0, true, true));
        this.getChildren().add(source);

        source.addEventHandler(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("basicTower");
                db.setContent(content);
                
                event.consume();
            }
       });
    }
}
