package dk.dtu.mtd.view;

import java.util.ArrayList;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.shared.EnemyType;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

public class EnemyShop extends HBox {

    public EnemyShop() {
        setMaxHeight(100);
        ArrayList<ImageView> items = new ArrayList<ImageView>();

        final ImageView skellyEnemy = new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true));
        this.getChildren().add(skellyEnemy);
        skellyEnemy.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                
                Controller.sendEnemies(EnemyType.SKELETON);

                event.consume();
            }
        });
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        this.getChildren().addAll(items);
        setAlignment(Pos.CENTER_LEFT);
    }
}
