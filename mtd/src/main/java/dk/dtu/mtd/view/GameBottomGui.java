package dk.dtu.mtd.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class GameBottomGui extends BorderPane {

    GameShop shop;

    public GameBottomGui() {
        this.shop = new GameShop();
        ImageView chatButton = chatButton();
        Rectangle square = new Rectangle(50,50);
        square.setFill(Color.TRANSPARENT);

        setCenter(shop);
        setRight(chatButton);
        setLeft(square);

        GameBottomGui.setAlignment(chatButton, Pos.CENTER_RIGHT);
        GameBottomGui.setAlignment(shop, Pos.CENTER);
        setMinWidth(Screen.getPrimary().getBounds().getWidth());
        setMaxHeight(100);

    }

    public void updateGameBottomGui(String newReward) {
        shop.updateRewardBox(newReward);
    }

    public ImageView chatButton() {
        ImageView chatButton = new ImageView(new Image("dk/dtu/mtd/assets/chatButton.jpg", 50, 50, true, false));
        chatButton.setOnMouseClicked(e -> {
            if (GameGui.interactionLayer.getChildren().contains(GameGui.gameChat)) {
                GameGui.interactionLayer.getChildren().remove(GameGui.gameChat);
            } else {
                GameGui.interactionLayer.getChildren().add(GameGui.gameChat);
            }
        });

        return chatButton;
    }
}
