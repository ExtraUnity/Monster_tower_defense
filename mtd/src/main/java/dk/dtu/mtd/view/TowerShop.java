package dk.dtu.mtd.view;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TowerShop extends HBox {

    public TowerShop() {
        ArrayList<ImageView> items = new ArrayList<ImageView>();
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));
        items.add(new ImageView(new Image("dk/dtu/mtd/assets/dartMonkey.png", 100, 0, true, true)));

        this.getChildren().addAll(items);
        setAlignment(Pos.CENTER_RIGHT);

        
    }
}
