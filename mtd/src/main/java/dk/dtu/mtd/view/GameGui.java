package dk.dtu.mtd.view;

import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameGui extends BorderPane {
    static VBox game;
    static BorderPane shop;
    static Text hp;
    static LinkedList<String> chat;
    static TextField chatWriter;

    public GameGui(int health) {
        //shop = new BorderPane();
        game = new VBox();
        hp = new Text("" + health);
        chat = new LinkedList<String>();
        chatWriter = new TextField("");

        this.setBackground(backgound());
        game.setAlignment(Pos.CENTER);

        Button counter = new Button("-10 for opponent");
        counter.setOnAction(e -> {
            Controller.damage();
        });

        Button submitMessage = new Button("Send Message");
        submitMessage.setOnAction( e -> {
            submitMessage();
        });

        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });

        game.getChildren().add(new Text("Game joined"));
        game.getChildren().addAll(hp,counter,chatWriter,submitMessage,exitGameButton);

        this.setCenter(game);
        this.setBottom(new GameShop());
    }

    public static void updateGameGui(int newHealth) {
        game.getChildren().remove(hp);
        hp = new Text("" + newHealth);
        game.getChildren().add(2, hp);
    }

    public Background backgound() {
        Image background = new Image("dk/dtu/mtd/assets/gameBackground.png");
        Background backgoundView = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(Gui.stage.getHeight(), Gui.stage.getWidth(), false, false, false, true)));
        return backgoundView;
    }

    public static void updateGameGui(LinkedList<String> newChat) {
        System.out.println("Got new chat list!");
        chat = newChat;
        displayChat();
    }

    public static void displayChat() {
        for(String s : chat) {
            System.out.println(s);
        }
    }

    private static void submitMessage() {
        System.out.println("Submitting message");
        String msg = chatWriter.getText().trim();
        Controller.sendMessage(msg);
    }

}
