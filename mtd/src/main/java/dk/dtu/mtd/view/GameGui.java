package dk.dtu.mtd.view;

import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameGui extends StackPane {
    static VBox game;
    static BorderPane layout;
    static Text hp;
    static GameChat gameChat;
    public static GameWaveGui gameWaveGui;


    public GameGui(int health) {
        layout = new BorderPane();
        //this.setBackground(backgound());
        game = new VBox();
        hp = new Text("" + health);
        gameChat = new GameChat();


        
        game.setAlignment(Pos.CENTER);

        gameWaveGui = new GameWaveGui();

        Button counter = new Button("-10 for opponent");
        counter.setOnAction(e -> {
            Controller.damageEnemyToPlayer(10);
        });


        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });

        game.getChildren().addAll(hp,counter,exitGameButton);

        layout.setCenter(game);

        ImageView chatButton = new ImageView(new Image("dk/dtu/mtd/assets/chatButton.jpg",50,50,true,false));
        chatButton.setOnMouseClicked(e -> {
            if(game.getChildren().contains(gameChat)) {
                game.getChildren().remove(gameChat);
            } else {
                game.getChildren().add(gameChat);
            }
        });
        BorderPane bottom = new BorderPane();
        bottom.setCenter(new GameShop());
        bottom.setRight(chatButton);
        BorderPane.setAlignment(chatButton, Pos.BOTTOM_RIGHT);
        layout.setBottom(bottom);

        this.setBackground(background());
        this.getChildren().add(gameWaveGui);
        this.getChildren().add(layout);
    }

    public static void updateGameGui(int newHealth) {
        game.getChildren().remove(hp);
        hp = new Text("" + newHealth);
        game.getChildren().add(0, hp);
    }

    public Background background() {
        Image background = new Image("dk/dtu/mtd/assets/gameBackground.png",  Gui.stage.getWidth(),  Gui.stage.getHeight(), true, false);
        Background backgoundView = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(Gui.stage.getHeight(), Gui.stage.getWidth(), false, false, false, true)));
        return backgoundView;
    }

    public static void updateGameGui(LinkedList<String> newChat) {
        System.out.println("Got new chat list!");
        gameChat.chatList = newChat;
        gameChat.displayChat();
    }

}
