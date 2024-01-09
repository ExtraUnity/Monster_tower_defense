package dk.dtu.mtd.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameWaveGui extends Pane{

    public GameWaveGui(){

    }



    ImageView enemy(){
        Image image = new Image("/dk/dtu/mtd/skelly.gif", 100, 0, true, false);
        ImageView imageView = new ImageView(image);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(imageView.imageProperty() , 1))
        );


        return imageView;
    }
    
}
