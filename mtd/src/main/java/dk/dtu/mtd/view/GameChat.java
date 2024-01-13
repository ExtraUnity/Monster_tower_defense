package dk.dtu.mtd.view;

import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GameChat extends StackPane {
    VBox layout;
    TextField chatWriter;
    LinkedList<String> chatList;
    TextArea textArea;

    public GameChat() {
        this.relocate(GameGui.gameAreaWidth / 2 - 160, GameGui.gameAreaHeight - 250);
        layout = new VBox(15);
        chatList = new LinkedList<String>();
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setMaxSize(300, 300);
        textArea.setWrapText(true);

        layout.setMinHeight(250);
        layout.setMinWidth(320);

        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        layout.setBorder(new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
        chatWriter = new TextField("");
        chatWriter.setMaxWidth(300);

        Button submitMessage = new Button("Send");
        submitMessage.setOnAction(e -> {
            submitMessage();
        });

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(chatWriter, submitMessage);

        layout.getChildren().addAll(textArea, hbox);
        this.getChildren().add(layout);

    }

    public void submitMessage() {
        String msg = chatWriter.getText().trim();
        Controller.sendMessage(msg);
    }

    public void displayChat() {
        textArea.setText("");
        textArea.setScrollTop(Double.MIN_VALUE);
        for (String s : chatList) {
            textArea.appendText(s + "\n");
        }

    }

}
