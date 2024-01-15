package dk.dtu.mtd.model.game;

import java.util.LinkedList;

public class Path {
    
    LinkedList<String> player1Path;
    LinkedList<String> player2Path;


    public Path() {
        player1Path = new LinkedList<String>();
        player2Path = new LinkedList<String>();

        greenPath();
    }

    public void greenPath() {
        // Player 1's path
        player1Path.add("610 0 160 650"); // First section
        player1Path.add("390 440 350 220"); // Second section
        player1Path.add("130 260 370 220"); // Third section
        player1Path.add("130 310 150 770"); // Fourth section

        // Player 2's path
        player2Path.add("1150 0 160 630"); // First section
        player2Path.add("1160 440 380 220"); // Second section
        player2Path.add("1400 260 400 220"); // Third section
        player2Path.add("1630 310 170 770"); // Fourth section
    }

    public boolean isOnPath(int x, int y) {

        for (String section:player1Path) {
            String[] cordinates = section.trim().split("\\s+");
            if (Integer.valueOf(cordinates[0]) < x && Integer.valueOf(cordinates[1]) < y && Integer.valueOf(cordinates[0]) + Integer.valueOf(cordinates[2]) > x && Integer.valueOf(cordinates[1]) + Integer.valueOf(cordinates[3]) > y) {
                return true;
            }
        }

        for (String section:player2Path) {
            String[] cordinates = section.trim().split("\\s+");
            if (Integer.valueOf(cordinates[0]) < x && Integer.valueOf(cordinates[1]) < y && Integer.valueOf(cordinates[0]) + Integer.valueOf(cordinates[2]) > x && Integer.valueOf(cordinates[1]) + Integer.valueOf(cordinates[3]) > y) {
                return true;
            }
        }
        return false;
    }


}



