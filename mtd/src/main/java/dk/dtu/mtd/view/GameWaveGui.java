package dk.dtu.mtd.view;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
            ImageView newEnemy = new EnemyImage();
            enemyArray[i] = newEnemy;
            wavePane.getChildren().add(newEnemy);
        }

    }

}

class EnemyImage extends ImageView {

    double speedX = 0;
    double speedY = 0;

    EnemyImage (){
        Image image = new Image("dk/dtu/mtd/assets/skelly.gif", 100, 0, true, false);
        setImage(image);

        
        setOnMousePressed(e -> {
            speedX = 1.0;
        });
        setOnMouseReleased(e -> {
            speedX = 0;
        });
        ImageView param = this;
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                param.setX(param.getX() + speedX);
                param.setY(param.getY() + speedY);
            }

        };
        timer.start();
    }
}
