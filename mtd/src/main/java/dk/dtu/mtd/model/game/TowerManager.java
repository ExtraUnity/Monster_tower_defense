package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.FormalField;

public class TowerManager implements Runnable {
    public volatile List<Tower> towerList;

    public TowerManager() {
        this.towerList = new ArrayList<Tower>();
    }

    @Override
    public void run() {
        while(true) {
            //System.out.println(towerList.size());
            for(int i = 0; i < towerList.size(); i++) {
                towerList.get(i).shoot(Game.waveManager.waveLeft.enemies);
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
            Object[] towerInfo = Game.gameSpace.get(new ActualField("towerInfo"), new FormalField(String.class),
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
                Game.gameSpace.put("gui", "newTower", tower, Game.player1.id);
                Game.gameSpace.put("gui", "newTower", tower, Game.player2.id);
                System.out.println("Tower placed at " + towerList.get(towerList.size() - 1).x + " "
                            + towerList.get(towerList.size() - 1).y);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
