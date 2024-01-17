package dk.dtu.mtd.view;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class TowerGui extends StackPane {
    ImageView towerImage = new ImageView();
    ImageView towerAttack = new ImageView();
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
    String stats;

    public TowerGui(String type, int size, int radius, int towerId, int playerId, int x, int y) {
        this.size = size;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.name = type;
        this.relocate(x - radius, y - radius);
        this.setAlignment(Pos.CENTER);
        switch (type) {
            case "basicTower":
                this.upgradePrice = 50;
                this.stats = "Damage: 5 \nAttackSpeed: 0.5 shots/sec\n SellPrice: 50";
                this.towerAttack.setImage(new Image("dk/dtu/mtd/assets/basicTowerShoot.gif"));
                this.towerAttack.setFitHeight(150);
                this.towerAttack.setFitWidth(150);
                break;
            case "aoeTower":
                this.upgradePrice = 50;
                this.stats = "Damage: 1 \nAttackSpeed: " + Math.round(5 / 7.0 * 100D) / 100D
                        + " hit/sec\n SellPrice: 50";
                this.towerAttack.setImage(new Image("dk/dtu/mtd/assets/AOEtower.gif"));
                this.towerAttack.setFitHeight(200);
                this.towerAttack.setFitWidth(200);
                break;
            case "moneyTower":
                this.upgradePrice = 50;
                this.stats = "Damage: 0 \nAttackSpeed: " + Math.round(5 / 10.0 * 100D) / 100D
                        + " coins/sec\n SellPrice: 50";
                this.towerAttack.setImage(new Image("dk/dtu/mtd/assets/MoneyTowerShoot.gif"));
                this.towerAttack.setFitHeight(100);
                this.towerAttack.setFitWidth(100);
                break;
        }

        towerImage.setFitHeight(size);
        towerImage.setFitWidth(size);

        circle = new Circle(x, y, radius);
        circle.setOpacity(0.2);
        circle.setVisible(false);
        circle.setMouseTransparent(true);

        this.getChildren().add(circle);

        if (type.equals("basicTower")) {
            towerImage.setImage(new Image("dk/dtu/mtd/assets/BasicTower.png"));
        } else if (type.equals("aoeTower")) {
            towerImage.setImage(new Image("dk/dtu/mtd/assets/AOEtower.png"));
        } else if (type.equals("moneyTower")) {
            towerImage.setImage(new Image("dk/dtu/mtd/assets/MoneyTower.png"));
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
        circle.setVisible(visible);
    }

    public void updateUpgradePrice(int price) {
        this.upgradePrice = price;
    }

    public void updateStatsAsString(String newStats) {
        this.stats = newStats;
    }

    public void shoot() {
        switch (name) {
            case "basicTower":
                this.towerAttack.setImage(new Image("dk/dtu/mtd/assets/basicTowerShoot.gif"));
                break;
            case "aoeTower":
                this.towerAttack.setImage(new Image("dk/dtu/mtd/assets/AOEtower.gif"));
                break;
            case "moneyTower":
                this.towerAttack.setImage(new Image("dk/dtu/mtd/assets/MoneyTowerShoot.gif"));
                break;
        }
        towerAttack.setX(x - (towerAttack.getFitWidth() / 2));
        towerAttack.setY(y - (towerAttack.getFitHeight() / 2));

        this.getChildren().add(towerAttack);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> this.getChildren().remove(towerAttack));
        pause.play();
    }

}
