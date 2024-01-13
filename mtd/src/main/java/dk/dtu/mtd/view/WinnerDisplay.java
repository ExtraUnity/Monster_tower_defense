package dk.dtu.mtd.view;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WinnerDisplay extends Text {
    
    public WinnerDisplay(String text) {
        super(600, 600, text);
        setFont(new Font(200));
        setStroke(Color.WHITE);
        setStrokeWidth(5);
    }
}
