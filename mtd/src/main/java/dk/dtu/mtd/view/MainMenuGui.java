package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenuGui extends StackPane {
    VBox menu;

    public MainMenuGui() {
        menu = new VBox();
        menu.setAlignment(Pos.CENTER);

        Button joinButton = new Button();
        joinButton.setText("Join Game");
        joinButton.setOnAction(e -> {
            Gui.root.getChildren().remove(this);
            Gui.game = new GameGui(150); // TODO: plz fix
            Gui.root.getChildren().add(Gui.game);
            Controller.joinGame();
        });

        menu.getChildren().add(joinButton);
        this.getChildren().add(menu);

    }

}
