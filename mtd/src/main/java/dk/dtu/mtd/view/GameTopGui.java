package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameTopGui extends VBox {
    HBox layout;
    Text healthPlayer1;
    Text healthPlayer2;
    Text waveNumber;
    StackPane leftIndicator = new StackPane();
    StackPane rightIndicator = new StackPane();

    public GameTopGui(String health1, String health2, int wave, String isLeft) {
        layout = new HBox(25);
        layout.setAlignment(Pos.CENTER);

        setPadding(new Insets(5));
        setSpacing(10);
        layout.setMaxHeight(100);
        setMaxHeight(100);
        setAlignment(Pos.BOTTOM_CENTER);

        healthPlayer1 = new Text("" + health1);
        healthPlayer2 = new Text("" + health2);
        waveNumber = new Text("Wave " + wave);

        healthPlayer1.setFont(new Font(30));
        healthPlayer2.setFont(new Font(30));
        waveNumber.setFont(new Font(30));

        leftIndicator.setMinWidth(500);
        rightIndicator.setMinWidth(500);

        Button exitGameButton = new Button("Resign");
        exitGameButton.setOnAction(e -> {
            Controller.resign();
        });

        layout.getChildren().addAll(leftIndicator, healthPlayer1, waveNumber, healthPlayer2, rightIndicator);
        getChildren().addAll(layout, exitGameButton);
    }

    public void updateWaveNumber(int waveNumber2) {
        layout.getChildren().removeAll(leftIndicator, healthPlayer1, waveNumber, healthPlayer2, rightIndicator);
        waveNumber = new Text("Wave " + waveNumber2);
        waveNumber.setFont(new Font(30));
        layout.getChildren().addAll(leftIndicator, healthPlayer1, waveNumber, healthPlayer2, rightIndicator);
    }

    public void youOpponent(String left) {
        Text you = new Text("You");
        Text opponent = new Text("Opponent");
        you.setFont(new Font(30));
        opponent.setFont(new Font(30));
        if (left.equals("left")) {
            leftIndicator.getChildren().add(you);
            rightIndicator.getChildren().add(opponent);
        } else {
            rightIndicator.getChildren().add(you);
            leftIndicator.getChildren().add(opponent);;
        }
    }

    public void updateHealth(String newHealth1, String newHealth2) {
        layout.getChildren().removeAll(leftIndicator, healthPlayer1, waveNumber, healthPlayer2, rightIndicator);

        healthPlayer1 = new Text("" + newHealth1);
        healthPlayer2 = new Text("" + newHealth2);
        healthPlayer1.setFont(new Font(30));
        healthPlayer2.setFont(new Font(30));

        layout.getChildren().addAll(leftIndicator, healthPlayer1, waveNumber, healthPlayer2, rightIndicator);
    }
}
