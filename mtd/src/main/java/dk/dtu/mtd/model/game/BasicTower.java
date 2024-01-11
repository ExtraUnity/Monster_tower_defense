package dk.dtu.mtd.model.game;

import java.util.List;

public class BasicTower extends Tower {
    public BasicTower(int x, int y, int playerId) {
        this.x = x;
        this.y = y;
        this.playerId = playerId;
        this.type = "basicTower";
        this.lastShot = Game.gameTicker.gameTick;
        radius = 300;
        damage = 10;
        size = 100;
        fireRate = 100;
        towerCost = 15;
    }

    public void shoot(List<Enemy> enemies) {
        int deltaTick = Game.gameTicker.gameTick - lastShot;

        if (deltaTick > fireRate) {
            for (int i = 0; i < enemies.size(); i++) {
                if (inRange(enemies.get(i)) && !enemies.get(i).isDead()) {
                    System.out.println("Enemy hit!!!!!");
                    enemies.get(i).takeDamage(damage);
                    lastShot = Game.gameTicker.gameTick;
                    deltaTick = 0;
                    break;
                }
            }
        }
    }
}
