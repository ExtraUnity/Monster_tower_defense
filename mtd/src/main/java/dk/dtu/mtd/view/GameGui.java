package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameGui extends StackPane {
    VBox game;

    public GameGui() {
        game = new VBox();
        game.setAlignment(Pos.CENTER);

        Button counter = new Button("+");
        counter.setOnAction( e -> {
            Controller.increaseScore();
        });

        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });
        game.getChildren().add(new Text("Game joined"));
        game.getChildren().add(counter);
        game.getChildren().add(exitGameButton);
        this.getChildren().add(game);
    }

}
