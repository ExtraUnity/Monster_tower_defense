package dk.dtu.mtd.view;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;

public class GameWaveGui extends StackPane {
    ArrayList<EnemyImage> enemyArray;
    Pane wavePane;

    public GameWaveGui() {
        wavePane = new Pane();
        wavePane.setMinSize(500, 500);
        this.getChildren().add(wavePane);

        enemyArray = new ArrayList<>();
        for (int i = 0; i < enemyArray.size(); i++) {
            EnemyImage newEnemy = new EnemyImage();
            enemyArray.add(newEnemy);
            wavePane.getChildren().add(newEnemy);
        }

    }


    public void updateEnemies(int newXCoord, int newYCoord){
        for (int i = 0; i < enemyArray.size(); i++) {
            enemyArray.get(i).xCoord = newXCoord;
            enemyArray.get(i).yCoord = newYCoord;
        }
    }

}

class EnemyImage extends ImageView {

    public double xCoord = 0;
    public double yCoord = 0;

    EnemyImage (){
        Image image = new Image("dk/dtu/mtd/assets/skelly.gif", 100, 0, true, false);
        setImage(image);

        ImageView param = this;
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                param.setX(xCoord);
                param.setY(yCoord);
            }

        };
        timer.start();
    }
}
