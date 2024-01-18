package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.shared.EnemyType;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class EnemyShop extends HBox {

    public EnemyShop() {
        this.setSpacing(5);
        setMaxHeight(100);
        setMinWidth(350);
        this.setPadding(new Insets(5));

        ShopElement skeleton = new ShopElement("dk/dtu/mtd/assets/skellyIcon.png", "50", EnemyType.SKELETON);
        ShopElement slime = new ShopElement("dk/dtu/mtd/assets/slimeIcon.png", "100", EnemyType.FAT_SKELETON);
        ShopElement devil = new ShopElement("dk/dtu/mtd/assets/DevilIcon.png", "250", EnemyType.DEVIL);
        ShopElement deerSkull = new ShopElement("dk/dtu/mtd/assets/DeerSkullIcon.png", "500", EnemyType.DEER_SKULL);

        this.getChildren().addAll(skeleton, slime, devil, deerSkull);
        setAlignment(Pos.CENTER);
    }

}

class ShopElement extends VBox {
    public ShopElement(String imageURL, String price, EnemyType type) {
        final ImageView element = new ImageView(new Image(imageURL, 70, 0, true, true));
        element.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Requesting enemies");
                Controller.sendEnemies(type);

                event.consume();
            }
        });
        Text priceText = new Text(price);
        priceText.setStroke(Color.WHITE);
        setAlignment(Pos.CENTER);
        getChildren().addAll(priceText, element);
    }

}