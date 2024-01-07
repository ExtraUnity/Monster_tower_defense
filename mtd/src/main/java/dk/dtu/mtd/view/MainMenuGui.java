package dk.dtu.mtd.view;

import dk.dtu.mtd.controller.Controller;
import javafx.application.Platform;
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
            final Object param = this;
            Thread t = new Thread(new Runnable() {
                Object p = param;
                public void run() {
                    Controller.joinGame();
                    Platform.runLater(() -> {
                        Gui.root.getChildren().remove(p);
                        Gui.game = new GameGui();
                        Gui.root.getChildren().add(Gui.game);
                    });

                }
            });
            t.start();

        });

        menu.getChildren().add(joinButton);
        this.getChildren().add(menu);

    }

}
