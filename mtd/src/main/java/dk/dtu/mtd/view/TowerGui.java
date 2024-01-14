package dk.dtu.mtd.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    Text upgradePrice;
    int x;
    int y;
    int radius;
    int size;
    

    public TowerGui(String type, int size, int radius, int towerId, int playerId, int x, int y) {
        this.size = size;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.relocate(x - radius, y - radius);

        this.setAlignment(Pos.CENTER);

        towerImage.setFitHeight(size);
        towerImage.setFitWidth(size);

        circle = new Circle(x, y, radius);
        circle.setOpacity(0.2);
        circle.setVisible(false);
        circle.setMouseTransparent(true);

        upgradePrice = new Text("50");
        upgradePrice.setFont(new Font(25));
        upgradePrice.setStroke(Color.WHITE);
        upgradePrice.setVisible(false);
        upgradePrice.setX(radius - (size/2));
        upgradePrice.setY(radius - (size/2));

        pane.getChildren().add(upgradePrice);

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

    public Text getUpgradePrice() {
        return upgradePrice;
    }

    public void updateUpgradePrice(String newPrice){
        pane.getChildren().remove(upgradePrice);
        upgradePrice = new Text(newPrice);
        upgradePrice.setFont(new Font(25));
        upgradePrice.setStroke(Color.WHITE);
        pane.getChildren().add(upgradePrice);
        upgradePrice.setX(radius - (size/2));
        upgradePrice.setY(radius - (size/2));
    }

    public void setCircleVisible(boolean visible) {
        upgradePrice.setVisible(visible);
        circle.setVisible(visible);
    }


}
