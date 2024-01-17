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
        this.setSpacing(15);
        setMaxHeight(100);
        setMinWidth(300);

        final ImageView skellyEnemy = new ImageView(new Image("dk/dtu/mtd/assets/skellyIcon.png", 70, 0, true, true));
        
        skellyEnemy.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Requesting enemies");
                Controller.sendEnemies(EnemyType.SKELETON);

                event.consume();
            }
        });

        final ImageView fatSkellyEnemy = new ImageView(new Image("dk/dtu/mtd/assets/slimeIcon.png", 70, 0, true, true));
        
        fatSkellyEnemy.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Requesting enemies");
                Controller.sendEnemies(EnemyType.FAT_SKELETON);

                event.consume();
            }
        });


        final ImageView deerSkull = new ImageView(new Image("dk/dtu/mtd/assets/DeerSkullIcon.png", 70, 0, true, true));
        deerSkull.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Requesting enemies");
                Controller.sendEnemies(EnemyType.DEER_SKULL);

                event.consume();
            }
        });



        this.getChildren().addAll(skellyEnemy, fatSkellyEnemy, deerSkull);
        setAlignment(Pos.CENTER);
    }
}
