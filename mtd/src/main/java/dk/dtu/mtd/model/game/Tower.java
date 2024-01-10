package dk.dtu.mtd.model.game;

import java.util.List;

public abstract class Tower {

    String type;

    int radius, x, y, size, damage, fireRate, playerID, id;
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

    public boolean legal() {
        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
    
}
