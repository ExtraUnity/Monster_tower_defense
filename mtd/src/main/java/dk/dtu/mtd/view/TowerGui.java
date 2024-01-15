package dk.dtu.mtd.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TowerGui extends StackPane {
    ImageView towerImage = new ImageView();
    Circle circle;
    Pane pane = new Pane();
    int upgradePrice;
    int sellPrice;
    int damageUpgrade;
    int x;
    int y;
    int radius;
    int size;
    String name;
    

    public TowerGui(String type, int size, int radius, int towerId, int playerId, int x, int y) {
        this.size = size;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.name = type;
        this.relocate(x - radius, y - radius);
        this.setAlignment(Pos.CENTER);
        switch(type) {
            case "basicTower":
                this.upgradePrice = 50;
                break;
            case "aoeTower":
                this.upgradePrice = 50;
                break;
        }

        towerImage.setFitHeight(size);
        towerImage.setFitWidth(size);

        circle = new Circle(x, y, radius);
        circle.setOpacity(0.2);
        circle.setVisible(false);
        circle.setMouseTransparent(true);

        

        //pane.getChildren().add(upgradePrice);

        this.getChildren().add(circle);

        if (type.equals("basicTower")) {
            towerImage.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
        } else if (type.equals("aoeTower")) {
            towerImage.setImage(new Image("dk/dtu/mtd/assets/AOEtower.png"));
        } else {
            towerImage.setImage(new Image("dk/dtu/mtd/assets/skelly.gif"));
        }      

        this.getChildren().add(towerImage);  
        this.getChildren().add(pane);
        
        this.setId("" + towerId);
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircleVisible(boolean visible) {
        //upgradePrice.setVisible(visible);
        circle.setVisible(visible);
    }

    public void updateUpgradePrice(int price) {
        this.upgradePrice = price;
    }


}
