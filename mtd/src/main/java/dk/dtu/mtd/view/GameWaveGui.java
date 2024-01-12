package dk.dtu.mtd.view;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class GameWaveGui extends StackPane {
    public ArrayList<EnemyImage> enemyArray;
    Pane wavePane;

    public final int waveGuiId;
    double gameAreaHeight;
    double gameAreaWidth;

    public GameWaveGui(int id, double gameAreaWidth, double gameAreaHeight) {
        wavePane = new Pane();
        wavePane.setMinSize(gameAreaWidth, gameAreaHeight);
        this.getChildren().add(wavePane);
        this.waveGuiId = id;
        this.gameAreaHeight = gameAreaHeight;
        this.gameAreaWidth = gameAreaWidth;
        System.out.println(this.gameAreaHeight + " " + this.gameAreaWidth);
    }

    public void initEnemies(String enemyTypes) {
        enemyArray = new ArrayList<>();

        System.out.println("Initializing Enemies for next wave");
        System.out.println(enemyTypes);

        // Splttting the input:
        String[] pairs = enemyTypes.split(",");
        System.out.println(Arrays.toString(pairs));
        if(pairs[0].equals("")){
            System.out.println("Recived empty wave");
            return;
        }
        for (String pair : pairs) { // For each pair
            String[] keyValue = pair.split(" ");
            System.out.println(pair);

            int numberOfEnemies = Integer.valueOf(keyValue[1]);
            String type = keyValue[0];
        
            for (int i = 0; i < numberOfEnemies; i++) {
                EnemyImage newEnemy = new EnemyImage(type);
                enemyArray.add(newEnemy);
                wavePane.getChildren().add(newEnemy);
            }
        }        
    }


    public void updateEnemies(LinkedList<String> coordinates) {
        try {
            for (int i = 0; i < enemyArray.size(); i++) {
                String[] coord = coordinates.get(i).split(" ");
                enemyArray.get(i).xCoord = (gameAreaWidth * ((Double.valueOf(coord[0])) / 1920)) - 50;
                enemyArray.get(i).yCoord = (gameAreaHeight * ((Double.valueOf(coord[1])) / 1080)) - 50;
            }
        } catch (Exception e) {
            System.out.println(coordinates.size() + " " + enemyArray.size());
        }

    }

    public void removeWave(){
        System.out.println("Removing the wave from gui!!");
        getChildren().remove(wavePane);
        GameGui.gameArea.getChildren().remove(this);
    }

}

// each enemy is represented by an enemy image (for now they are all skeletons)
class EnemyImage extends ImageView {
    public double xCoord = 900;
    public double yCoord = 900;
    Image image;

    EnemyImage(String type) { //Pass in the image path, width and height

        if (type.equals("Skeleton")) {
            this.image = new Image("dk/dtu/mtd/assets/skelly.gif", 150, 0, true, false);
        } else if (type.equals("FatSkeleton")) {
            this.image = new Image("dk/dtu/mtd/assets/SuperMonkey.png", 100, 0, true, false);
        } else if (type.equals("tankEnemy")) {
            this.image = new Image("dk/dtu/mtd/assets/skelly.gif", 100, 0, true, false);
        } else if (type.equals("bossEnemy")) {
            this.image = new Image("dk/dtu/mtd/assets/skelly.gif", 100, 0, true, false);
        }
        
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
