package dk.dtu.mtd.view;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class EnemyShop extends HBox {

    public EnemyShop() {
        setMaxHeight(100);
        ArrayList<ImageView> items = new ArrayList<ImageView>();

        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 70, 0, true, true)));
        this.getChildren().addAll(items);
        setAlignment(Pos.CENTER_LEFT);
    }
}
