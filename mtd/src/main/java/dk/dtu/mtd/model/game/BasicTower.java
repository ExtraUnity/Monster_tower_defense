package dk.dtu.mtd.model.game;

import java.util.List;

public class BasicTower extends Tower {
    public BasicTower(int x, int y, int towerId, int playerId, GameTicker gameTicker) {
        this.x = x;
        this.y = y;
        this.playerId = playerId;
        this.type = "basicTower";
        //this.gameTicker = gameTicker;
        this.lastShot = -1;
        this.towerId = towerId;
        radius = 300;
        damage = 5;
        size = 100;
        fireRate = 100;
        towerCost = 100;
        upgradeCost = 50;
        sellPrice = 50;
    }

    public void shoot(List<Enemy> enemies, Game game) {
        if(this.lastShot == -1) {
            this.lastShot = game.gameTicker.gameTick;
        }
        int deltaTick = game.gameTicker.gameTick - lastShot;

        if (deltaTick > fireRate) {
            for (int i = 0; i < enemies.size(); i++) {
                if (inRange(enemies.get(i)) && !enemies.get(i).isDead()) {
                    System.out.println("Player " + playerId + " killed enemy at: " + enemies.get(i).getX() + " " + enemies.get(i).getY() + " with tower at: " + x + " " + y);
                    enemies.get(i).takeDamage(damage, playerId, game);
                    lastShot = game.gameTicker.gameTick;
                    deltaTick = 0;
                    break;
                }
            }
        }
    }
}
