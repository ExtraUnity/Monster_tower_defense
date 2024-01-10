package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

public class TowerManager implements Runnable {
    public volatile List<Tower> towerList;
    public volatile boolean playing;

    Space gameSpace;

    public TowerManager(Space gameSpace) {
        this.towerList = new ArrayList<Tower>();
        this.playing = true;
        this.gameSpace = gameSpace;
    }

    @Override
    public void run() {
        while (playing) {
            // System.out.println(towerList.size());
            for (int i = 0; i < towerList.size(); i++) {
                //System.out.println(towerList.get(i).playerId + " " + Game.player1.id + " " + Game.player2.id);
                if (towerList.get(i).playerId == Game.player1.id) {
                    towerList.get(i).shoot(Game.waveManager.waveLeft.enemies);
                } else {
                    //System.out.println("Shooting right side");
                    towerList.get(i).shoot(Game.waveManager.waveRight.enemies);
                }
            }
        }
    }

    public Boolean legalTowerPlacement(Tower newTower, int playerId) {
        for (Tower tower : towerList) {
            if(Math.pow(tower.size,2) + Math.pow(newTower.size,2) >= Math.pow((tower.getX() - newTower.x), 2) + Math.pow((tower.getY() - newTower.y), 2)) {
                return false;
            }
        }
        if (Game.player1.id == playerId && newTower.x > 960) {
            return false;
        } else if (Game.player2.id == playerId && newTower.x < 960) {
            return false;
        }
        return true;
    }

    public void placeTower(int playerId) {
        try {
            Tower tower;
            Object[] towerInfo = gameSpace.get(new ActualField("towerInfo"), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(Integer.class));
            if(towerInfo[1].equals("basicTower")) {
                tower = new BasicTower((int) towerInfo[2], (int) towerInfo[3], playerId);
            } else if (towerInfo[1].equals("superTower")) {
                tower = new BasicTower((int) towerInfo[2], (int) towerInfo[3], playerId);
            } else {
                tower = new BasicTower(0, 0, playerId);
            }

            if (legalTowerPlacement(tower, playerId)) {
                towerList.add(tower);
                gameSpace.put("gui", "newTower", tower, Game.player1.id);
                gameSpace.put("gui", "newTower", tower, Game.player2.id);
                System.out.println("Tower placed at " + towerList.get(towerList.size() - 1).x + " "
                            + towerList.get(towerList.size() - 1).y);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
