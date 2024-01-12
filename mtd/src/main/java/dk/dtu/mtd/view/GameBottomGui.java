package dk.dtu.mtd.view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

public class GameBottomGui extends BorderPane {

    GameShop shop;

    public GameBottomGui() {
        this.shop = new GameShop();
        ImageView chatButton = chatButton();

        BorderPane.setAlignment(chatButton, Pos.CENTER_RIGHT);
        setMinWidth(Screen.getPrimary().getBounds().getWidth());
        setMaxHeight(100);
        setCenter(shop);
        setRight(chatButton);
    }

    public void updateGameBottomGui(String newReward) {
        shop.updateRewardBox(newReward);
    }

    // TODO: create better placement for chat window!!!
    public ImageView chatButton() {
        ImageView chatButton = new ImageView(new Image("dk/dtu/mtd/assets/chatButton.jpg", 50, 50, true, false));
        chatButton.setOnMouseClicked(e -> {
            if (GameGui.gameTop.getChildren().contains(GameGui.gameChat)) {
                GameGui.gameTop.getChildren().remove(GameGui.gameChat);
            } else {
                GameGui.gameTop.getChildren().add(GameGui.gameChat);
            }
        });

        return chatButton;
    }
}
