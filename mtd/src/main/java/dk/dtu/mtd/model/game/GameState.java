package dk.dtu.mtd.model.game;

import java.util.LinkedList;
import java.util.Queue;

import org.jspace.Space;

public class GameState implements Runnable{
    
    Space space;
    private Queue<Enemy> enemyList; 
    private Queue<Tower> towerList;

    public GameState(Space space) {
        this.enemyList = new LinkedList<Enemy>();
        this.towerList = new LinkedList<Tower>();
        this.space = space;
    }

    public void addEnemy(Enemy enemy) {
        enemyList.add(enemy);
    }

    public void addTower(Tower tower) {
        towerList.add(tower);
    }

    public void removeEnemy(Enemy enemy) {
        enemyList.remove(enemy);
    }

    public void removeTower(Tower tower) {
        towerList.remove(tower);
    }

    @Override
    public void run() {
        while (true) {
            
        }
    }
    
}
