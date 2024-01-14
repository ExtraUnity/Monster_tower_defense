package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import dk.dtu.mtd.shared.EnemyType;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class EnemyShop extends HBox {

    public EnemyShop() {
        setMaxHeight(100);
        setMinWidth(300);

        final ImageView skellyEnemy = new ImageView(new Image("dk/dtu/mtd/assets/skellyIcon.png", 70, 0, true, true));
        this.getChildren().add(skellyEnemy);
        skellyEnemy.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Requesting enemies");
                Controller.sendEnemies(EnemyType.SKELETON);

                event.consume();
            }
        });
        setAlignment(Pos.CENTER);
    }
}
