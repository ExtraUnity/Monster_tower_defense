package dk.dtu.mtd.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TowerShop extends HBox {

    public TowerShop() {
        setMaxHeight(100);
        setMinWidth(350);
        setAlignment(Pos.CENTER);
        
        final ImageView basicTower = new ImageView(new Image("dk/dtu/mtd/assets/BasicTower.png", 70, 0, true, true));

        VBox basicTowerLayout = new VBox();
        Text basicTowerPrice = new Text("100");
        basicTowerPrice.setStroke(Color.WHITE);
        basicTowerLayout.setAlignment(Pos.CENTER);
        basicTowerLayout.getChildren().addAll(basicTowerPrice, basicTower);
        this.getChildren().add(basicTowerLayout);

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
        final ImageView superTower = new ImageView(new Image("dk/dtu/mtd/assets/AOEtower.png", 70, 0, true, true));

        VBox superTowerLayout = new VBox();
        Text superTowerPrice = new Text("350");
        superTowerPrice.setStroke(Color.WHITE);
        superTowerLayout.setAlignment(Pos.CENTER);
        superTowerLayout.getChildren().addAll(superTowerPrice, superTower);
        this.getChildren().add(superTowerLayout);

        superTower.addEventHandler(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = superTower.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("aoeTower");
                db.setContent(content);

                event.consume();
            }
        });

        final ImageView moneyTree = new ImageView(new Image("dk/dtu/mtd/assets/MoneyTower.png", 70, 0, true, true));

        VBox moneyTreeLayout = new VBox();
        Text moneyTreePrice = new Text("250");
        moneyTreePrice.setStroke(Color.WHITE);
        moneyTreeLayout.setAlignment(Pos.CENTER);
        moneyTreeLayout.getChildren().addAll(moneyTreePrice, moneyTree);
        this.getChildren().add(moneyTreeLayout);

        moneyTree.addEventHandler(MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = moneyTree.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                content.putString("moneyTower");
                db.setContent(content);

                event.consume();
            }
        });
    }
}
