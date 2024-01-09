package dk.dtu.mtd.model.game;

import java.util.List;

public abstract class Tower {
    int radius, x, y, damage, fireRate, playerID;
    int lastShot = fireRate;

    int towerLevel = 0;

    public Tower() {}
    
    public void upgradeTower() {
        towerLevel++;
        damage++;
    }

    public Boolean inRange(Enemy enemy) {
        return radius * radius > (enemy.x - x) * (enemy.x - x) + (enemy.y - y) * (enemy.y - y);
    }

    public abstract void shoot(List<Enemy> enemies);

    
}
