package dk.dtu.mtd.view;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GameWaveGui extends StackPane {
    ImageView[] enemyArray;
    int numberOfEnemys;
    StackPane wavePane;

    public GameWaveGui(int numberOfEnemys) {
        this.numberOfEnemys = numberOfEnemys;
        wavePane = new StackPane();
        this.getChildren().add(wavePane);

        enemyArray = new ImageView[numberOfEnemys];
        for (int i = 0; i < numberOfEnemys; i++) {
            ImageView newEnemy = enemy();
            enemyArray[i] = newEnemy;
            System.out.println("enemy position" + newEnemy.xProperty());
            newEnemy.relocate(0, 0);
            wavePane.getChildren().add(newEnemy);
            animation(100,100, newEnemy);
            System.out.println("enemy position" + newEnemy.xProperty());
        }

    
    }

    ImageView enemy() {
        Image image = new Image("dk/dtu/mtd/assets/skelly.gif", 100, 0, true, false);
        ImageView imageView = new ImageView(image);

        return imageView;
    }

    void animation(int newX, int newY, ImageView enemy) {
        System.out.println("moving enemy to " + newX + " " + newY);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                enemy.setX(newX);
                enemy.setY(newY);
            }

        };
        timer.start();
    }

    void gameWaveGuiUpdate() {
        for (int i = 0; i < numberOfEnemys; i++) {
            animation(i, i, enemyArray[i]);
        }
    }
}
