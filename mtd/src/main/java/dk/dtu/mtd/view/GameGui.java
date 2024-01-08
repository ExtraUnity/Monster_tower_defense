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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameGui extends StackPane {
    static VBox game;
    static BorderPane layout;
    static Text hp;
    static LinkedList<String> chat;
    static TextField chatWriter;

    public GameGui(int health) {
        layout = new BorderPane();
        //this.setBackground(backgound());
        game = new VBox();
        hp = new Text("" + health);
        chat = new LinkedList<String>();
        chatWriter = new TextField("");
        chatWriter.setMaxWidth(300);

        
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

        game.getChildren().addAll(hp,counter,chatWriter,submitMessage,exitGameButton);

        layout.setCenter(game);
        layout.setBottom(new GameShop());

        this.getChildren().addAll(background(), layout);
    }

    public static void updateGameGui(int newHealth) {
        game.getChildren().remove(hp);
        hp = new Text("" + newHealth);
        game.getChildren().add(0, hp);
    }

    public ImageView background() {
        Image background = new Image("dk/dtu/mtd/assets/gameBackground.png",  Gui.stage.getWidth(),  Gui.stage.getHeight(), true, false);
        //Background backgoundView = new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,
        //        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        //        new BackgroundSize(Gui.stage.getHeight(), Gui.stage.getWidth(), false, false, false, true)));
        return new ImageView(background);
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
