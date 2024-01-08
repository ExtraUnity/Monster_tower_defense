package dk.dtu.mtd.view;

import java.io.InputStream;
import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameGui extends StackPane {
    static VBox game;
    static Text hp;
    static LinkedList<String> chat;

    public GameGui(int health) {
        game = new VBox();
        game.setAlignment(Pos.CENTER);

        hp = new Text("" + health);
        chat = new LinkedList<String>();

        Image skel = new Image("dk/dtu/mtd/assets/skelly.gif", 100 , 0, true, true);
        ImageView skelly = new ImageView(skel);

        Button counter = new Button("-10 for opponent");
        counter.setOnAction( e -> {
            Controller.damage();
        });

        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });

        game.getChildren().add(new Text("Game joined"));
        game.getChildren().addAll(skelly, hp ,counter,exitGameButton);
        this.getChildren().add(game);
    }

    public static void updateGameGui(int newHealth){
       game.getChildren().remove(hp);
       hp = new Text("" + newHealth);
       game.getChildren().add(2 , hp);
    }

    public static void updateGameGui(LinkedList<String> newChat) {
        chat = newChat;
    }

}
