package dk.dtu.mtd.model.game;

import java.util.LinkedList;

public class Path {
    
    LinkedList<int[]> player1Path;
    LinkedList<int[]> player2Path;


    public Path() {
        player1Path = new LinkedList<int[]>();
        player2Path = new LinkedList<int[]>();

        greenPath();
    }

    public void greenPath() {
        // Player 1's path
        int[] section1 = {610,0,770,440}; // First section
        player1Path.add(section1); 

        int[] section2 = {380,440,770,670}; // Second section
        player1Path.add(section2); 
    }

    public boolean isOnPath(int x, int y) {

        for (int[] section:player1Path) {
            if (section[0] < x && section[1] < y && section[2] > x && section[3] > y) {
                System.out.println("not here");
                return true;
            }
        }
        return false;
    }


}



