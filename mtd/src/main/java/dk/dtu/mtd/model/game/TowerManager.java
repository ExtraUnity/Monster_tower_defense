package dk.dtu.mtd.model.game;

import java.util.ArrayList;
import java.util.List;

import org.jspace.ActualField;
import org.jspace.FormalField;

public class TowerManager implements Runnable {
    public volatile List<Tower> towerList;
    public volatile boolean playing;

    Game game;

    public TowerManager(Game game) {
        this.towerList = new ArrayList<Tower>();
        this.playing = true;
        this.game = game;
    }

    @Override
    public void run() {
        while (playing) {

            for (int i = 0; i < towerList.size(); i++) {
                if (towerList.get(i).playerId == game.player1.id) {
                    towerList.get(i).shoot(game.waveManager.leftEnemies);
                } else {
                    towerList.get(i).shoot(game.waveManager.rightEnemies);
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
        if (game.player1.id == playerId && newTower.x > 960) {
            return false;
        } else if (game.player2.id == playerId && newTower.x < 960) {
            return false;
        }

        if (game.player1.id == playerId && game.player1.getRewards() <= newTower.getTowerCost()) {
            return false;
        } else if (game.player2.id == playerId && game.player2.getRewards() <= newTower.getTowerCost()) {
            return false;
        }
        return true;
    }

    public void placeTower(int playerId) {
        try {
            Tower tower;
            Object[] towerInfo = game.gameSpace.get(new ActualField("towerInfo"), new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(Integer.class));
            if (towerInfo[1].equals("basicTower")) {
                tower = new BasicTower((int) towerInfo[2], (int) towerInfo[3], towerList.size(), playerId,
                        game.gameTicker);
            } else if (towerInfo[1].equals("superTower")) {
                tower = new BasicTower((int) towerInfo[2], (int) towerInfo[3], towerList.size(), playerId,
                        game.gameTicker);
            } else {
                tower = new BasicTower(0, 0, towerList.size(), playerId, game.gameTicker);
            }

            if (legalTowerPlacement(tower, playerId)) {
                towerList.add(tower);
                // String type, int size, int radius, int towerId, int playerId, int x, int y
                String guiTowerInfoString = tower.getType() + " " + tower.getSize() + " "
                        + tower.getRadius() + " " + tower.getTowerId() + " "
                        + tower.getPlayerId() + " " + tower.getX() + " " + tower.getY();
                game.gameSpace.put("gui", "newTower", guiTowerInfoString, game.player1.id);
                game.gameSpace.put("gui", "newTower", guiTowerInfoString, game.player2.id);
                if (game.player1.id == playerId) {
                    game.player1.spendRewards(tower.getTowerCost());
                } else {
                    game.player2.spendRewards(tower.getTowerCost());
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
                if (tower.getTowerId() == towerId) {
                    if (playerId == game.player1.id && tower.getUpgradeCost() <= game.player1.getRewards()) {
                        game.player1.spendRewards(tower.getUpgradeCost());
                        tower.upgradeTower();
                    } else if (playerId == game.player2.id && tower.getUpgradeCost() <= game.player2.getRewards()) {
                        game.player2.spendRewards(tower.getUpgradeCost());
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
