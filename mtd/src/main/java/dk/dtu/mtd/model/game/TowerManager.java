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

    public Boolean legalTowerPlacement(String towerType, int x, int y, int id) {
        for (Tower tower : towerList) {
            if(70 * 70 > Math.pow((tower.getX() - x), 2) + Math.pow((tower.getY() - y), 2)) {
                return false;
            }
        }
        if (Game.player1.id == id && x > 960) {
            return false;
        } else if (Game.player2.id == id && x < 960) {
            return false;
        }
        return true;
    }

    public void placeTower(int playerId) {
        try {
            Object[] towerInfo = Game.gameSpace.get(new ActualField("towerInfo"), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(Integer.class));
            if (towerInfo[1].equals("basicTower")) {
                if (legalTowerPlacement((String) towerInfo[1], (int) towerInfo[2], (int) towerInfo[3], playerId)) {
                    towerList.add(new BasicTower((int) towerInfo[2], (int) towerInfo[3], playerId));
                    Game.gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), Game.player1.id);
                    Game.gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), Game.player2.id);
                    System.out.println("Tower placed at " + towerList.get(towerList.size() - 1).x + " "
                            + towerList.get(towerList.size() - 1).y);
                }
            } else if (towerInfo[1].equals("superTower")) {
                if (legalTowerPlacement((String) towerInfo[1], (int) towerInfo[2], (int) towerInfo[3], playerId)) {
                    towerList.add(new BasicTower((int) towerInfo[2], (int) towerInfo[3], playerId));
                    Game.gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), Game.player1.id);
                    Game.gameSpace.put("gui", "newTower", towerList.get(towerList.size() - 1), Game.player2.id);
                    System.out.println("Tower placed at " + towerList.get(towerList.size() - 1).x + " "
                            + towerList.get(towerList.size() - 1).y);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
