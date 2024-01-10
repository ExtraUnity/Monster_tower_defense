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
            for(Tower tower : towerList) {
                tower.shoot(Game.waveManager.waveLeft.enemies);
            }
        }
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
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Boolean legalTowerPlacement(String towerType, int x, int y, int id) {
        return true;
    }


}
