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
        towerShop = new HBox();
        enemyShop = new HBox();
        towerShop.setAlignment(Pos.CENTER_RIGHT);
        enemyShop.setAlignment(Pos.CENTER_LEFT);

        towerShop.getChildren().add(new ImageView(new Image("dk/dtu/mtd/assets/skelly.gif", 100,0,true,true)));
        enemyShop.getChildren().add(new ImageView(new Image("dk/dtu/mtd/assets/SuperMonkey.png", 100,0,true,true)));
        shop.getChildren().addAll(towerShop,enemyShop);
        shop.setAlignment(Pos.CENTER);
        Image shopImage = new Image("dk/dtu/mtd/assets/shop.png");
        ImageView shopView = new ImageView(shopImage);
        this.getChildren().addAll(shopView,shop);
        
    }    
}
