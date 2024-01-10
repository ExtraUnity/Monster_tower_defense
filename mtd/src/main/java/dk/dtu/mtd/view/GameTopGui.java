package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class GameTopGui extends HBox {
    HBox layout;
    Text healthPlayer1;
    Text healthPlayer2;
    Text waveNumber;

    public GameTopGui(String health1, String health2, int wave) {
        layout = new HBox(10);
        layout.setAlignment(Pos.CENTER);

        setPadding(new Insets(5));
        setSpacing(50);
        layout.setMaxHeight(100);
        setMaxHeight(100);
        setAlignment(Pos.TOP_CENTER);

        healthPlayer1 = new Text("" + health1);
        healthPlayer2 = new Text("" + health2);
        waveNumber = new Text("Wave " + wave);

        // temporary
        Button counter = new Button("-10 for opponent");
        counter.setOnAction(e -> {
            Controller.damageEnemyToPlayer(10);
        });

        Button exitGameButton = new Button("exit");
        exitGameButton.setOnAction(e -> {
            Controller.exitGame();
        });

        layout.getChildren().addAll(healthPlayer1, waveNumber, healthPlayer2);
        getChildren().addAll(counter, layout, exitGameButton);
    }
}
