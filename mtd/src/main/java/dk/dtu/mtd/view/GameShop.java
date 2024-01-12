package dk.dtu.mtd.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class GameShop extends StackPane {
    HBox shop;
    HBox towerShop;
    HBox enemyShop;
    StackPane stackPane;

    public GameShop() {
        stackPane = new StackPane();
        shop = new HBox();
        towerShop = new TowerShop();
        enemyShop = new EnemyShop();
        setMaxHeight(100);
        rewardBox();

        shop.getChildren().addAll(towerShop, stackPane, enemyShop);
        shop.setAlignment(Pos.CENTER);
        Image shopImage = new Image("dk/dtu/mtd/assets/shop.png", 0, 100, true, false);
        ImageView shopView = new ImageView(shopImage);
        this.getChildren().addAll(shopView, shop);

    }

    void rewardBox() {
        Circle coin1 = new Circle(35, Color.GOLD);
        Circle coin2 = new Circle(30, Color.BROWN);
        Circle coin3 = new Circle(25, Color.GOLD);
        Text reward = new Text("100");
        stackPane.getChildren().addAll(coin1, coin2, coin3, reward);
        stackPane.setAlignment(Pos.CENTER);
    }

    void updateRewardBox(String newReward) {
        stackPane.getChildren().remove(1);
        stackPane.getChildren().add(new Text(newReward));
    }
}
