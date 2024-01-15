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
        player1Path.add("610 0 160 440"); // First section

        //int[] section2 = {380,440,770,670}; // Second section
        //player1Path.add(section2); 



        player2Path.add("810 500 870 640");
    }

    public boolean isOnPath(int x, int y) {

        for (String section:player1Path) {
            String[] cordinates = section.trim().split("\\s+");
            if (Integer.valueOf(cordinates[0]) < x && Integer.valueOf(cordinates[1]) < y && Integer.valueOf(cordinates[2]) > x && Integer.valueOf(cordinates[3]) > y) {
                System.out.println("not here");
                return true;
            }
        }
        return false;
    }


}



