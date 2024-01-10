package dk.dtu.mtd.view;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;
import java.util.LinkedList;

public class GameWaveGui extends StackPane {
    public ArrayList<EnemyImage> enemyArray;
    Pane wavePane;

    public GameWaveGui() {
        wavePane = new Pane();
        wavePane.setMinSize(500, 500);
        this.getChildren().add(wavePane);

    }

    public void initEnemies(int num) {
        enemyArray = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            EnemyImage newEnemy = new EnemyImage();
            enemyArray.add(newEnemy);
            wavePane.getChildren().add(newEnemy);
        }
    }

    public void updateEnemies(LinkedList<String> coordinates) {
        for (int i = 0 ; i < enemyArray.size(); i++) {
            String[] coord = coordinates.get(i).split(" ");
            enemyArray.get(i).xCoord = Integer.valueOf(coord[0]) - 50;
            enemyArray.get(i).yCoord = Integer.valueOf(coord[1]) - 50;
        }
    }

}

// each enemy is represented by an enemy image (for now they are all skeletons)
class EnemyImage extends ImageView {
    public double xCoord = 900;
    public double yCoord = 900;

    EnemyImage() {
        Image image = new Image("dk/dtu/mtd/assets/skelly.gif", 100, 0, true, false);
        setImage(image);
        setX(xCoord);
        setY(yCoord);
        setFitHeight(100);
        setFitWidth(100);

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
