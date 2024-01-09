package dk.dtu.mtd.view;

import java.util.LinkedList;

import dk.dtu.mtd.controller.Controller;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameChat extends StackPane {
    VBox layout;
    TextField chatWriter;
    LinkedList<String> chatList;
    TextArea textArea;

    public GameChat() {
        layout = new VBox(15);
        chatList = new LinkedList<String>();
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setMaxSize(300, 500);
        textArea.setWrapText(true);

        chatWriter = new TextField("");
        chatWriter.setMaxWidth(300);

        Button submitMessage = new Button("Send");
        submitMessage.setOnAction(e -> {
            submitMessage();
        });

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(chatWriter, submitMessage);

        layout.getChildren().addAll(textArea, hbox);
        this.getChildren().add(layout);

    }

    public void submitMessage() {
        System.out.println("Submitting message");
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
