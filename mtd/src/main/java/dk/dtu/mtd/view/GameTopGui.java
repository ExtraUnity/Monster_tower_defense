package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class GameTopGui extends StackPane{
        VBox layout;
        Text hp;
    
    public GameTopGui(int health){
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        hp = new Text("" + health);

        Button counter = new Button("-10 for opponent");
        counter.setOnAction(e -> {
            Controller.damageEnemyToPlayer(10);
        });

        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });

        layout.getChildren().addAll(hp, counter, exitGameButton);
        getChildren().add(layout);
    }
}
