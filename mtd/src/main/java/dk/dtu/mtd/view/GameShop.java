package dk.dtu.mtd.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class GameShop extends StackPane{
    HBox shop;
    HBox towerShop;
    HBox enemyShop;

    public GameShop(){
        shop = new HBox();
        towerShop = new TowerShop();
        enemyShop = new EnemyShop();

        shop.getChildren().addAll(towerShop,enemyShop);
        shop.setAlignment(Pos.CENTER);
        Image shopImage = new Image("dk/dtu/mtd/assets/shop.png");
        ImageView shopView = new ImageView(shopImage);
        this.getChildren().addAll(shopView,shop);
        
    }    
}
