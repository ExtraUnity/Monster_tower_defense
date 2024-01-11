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
    Game game;

    public TowerManager(Space gameSpace, Game game) {
        this.towerList = new ArrayList<Tower>();
        this.playing = true;
        this.gameSpace = gameSpace;
        this.game = game;
    }

    @Override
    public void run() {
        while (playing) {
            // System.out.println(towerList.size());
            for (int i = 0; i < towerList.size(); i++) {
                // System.out.println(towerList.get(i).playerId + " " + Game.player1.id + " " +
                // Game.player2.id);
                if (towerList.get(i).playerId == Game.player1.id) {
                    towerList.get(i).shoot(game.waveManager.waveLeft.enemies);
                } else {
                    // System.out.println("Shooting right side");
                    // TODO: this can throw exceptions (possibly when another game had been started)
                    towerList.get(i).shoot(game.waveManager.waveRight.enemies);
                }
            }
        }
        System.out.println("Towermanger " + game.id + " closing");
    }

    public Boolean legalTowerPlacement(Tower newTower, int playerId) {
        for (Tower tower : towerList) {
            if (Math.pow(tower.size, 2) + Math.pow(newTower.size, 2) >= Math.pow((tower.getX() - newTower.x), 2)
                    + Math.pow((tower.getY() - newTower.y), 2)) {
                return false;
            }
        }
        if (Game.player1.id == playerId && newTower.x > 960) {
            return false;
        } else if (Game.player2.id == playerId && newTower.x < 960) {
            return false;
        }

        if (Game.player1.id == playerId && Game.player1.getRewards() <= newTower.getTowerCost()) {
            return false;
        } else if (Game.player2.id == playerId && Game.player2.getRewards() <= newTower.getTowerCost()) {
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
                tower = new BasicTower((int) towerInfo[2], (int) towerInfo[3], towerList.size(), playerId);
            } else if (towerInfo[1].equals("superTower")) {
                tower = new BasicTower((int) towerInfo[2], (int) towerInfo[3], towerList.size(), playerId);
            } else {
                tower = new BasicTower(0, 0,towerList.size(), playerId);
            }

            if (legalTowerPlacement(tower, playerId)) {
                towerList.add(tower);
                game.gameSpace.put("gui", "newTower", tower, Game.player1.id);
                game.gameSpace.put("gui", "newTower", tower, Game.player2.id);
                if(Game.player1.id == playerId) {
                    Game.player1.spendRewards(tower.getTowerCost());
                } else {
                    Game.player2.spendRewards(tower.getTowerCost());
                }
                System.out.println("Tower placed at " + towerList.get(towerList.size() - 1).x + " "
                        + towerList.get(towerList.size() - 1).y);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void upgradeTower(int playerId) {
        try {
            int towerId = (int) game.gameSpace.get(new ActualField("towerId"), new FormalField(Integer.class))[1];
            System.out.println("tried to upgrade tower");
            for (Tower tower : towerList) {
                if(tower.getTowerId() == towerId) {
                    if(playerId == Game.player1.id && tower.getUpgradeCost() <= Game.player1.getRewards()) {
                        Game.player1.spendRewards(tower.getUpgradeCost());
                        tower.upgradeTower();
                    } else if (playerId == Game.player2.id && tower.getUpgradeCost() <= Game.player2.getRewards()) {
                        Game.player2.spendRewards(tower.getUpgradeCost());
                        tower.upgradeTower();
                    }
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
