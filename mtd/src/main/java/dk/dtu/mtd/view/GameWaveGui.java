package dk.dtu.mtd.view;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GameWaveGui extends StackPane {
    ImageView[] enemyArray;
    int numberOfEnemys;
    Pane wavePane;

    public GameWaveGui(int numberOfEnemys) {
        this.numberOfEnemys = numberOfEnemys;
        wavePane = new Pane();
        wavePane.setMinSize(500, 500);
        this.getChildren().add(wavePane);

        enemyArray = new ImageView[numberOfEnemys];
        for (int i = 0; i < numberOfEnemys; i++) {
            ImageView newEnemy = enemy();
            enemyArray[i] = newEnemy;
            wavePane.getChildren().add(newEnemy);
        }

    }

    ImageView enemy() {
        Image image = new Image("dk/dtu/mtd/assets/skelly.gif", 100, 0, true, false);
        ImageView imageView = new ImageView(image);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                imageView.setX(imageView.getX() + 2.0);
            }

        };
        timer.start();

        return imageView;
    }
}
