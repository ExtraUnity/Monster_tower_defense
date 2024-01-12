package dk.dtu.mtd.model.game;

import java.util.List;

public abstract class Tower {

    String type;

    int radius, x, y, size, damage, upgradeCost, towerCost, fireRate, playerId, towerId;
    int lastShot;
    //GameTicker gameTicker;

    int towerLevel = 0;

    public Tower() {}
    
    public void upgradeTower() {
        towerLevel++;
        damage++;
        upgradeCost += 10;
        System.out.println("tower level is " + towerLevel);
    }

    public Boolean inRange(Enemy enemy) {
        return radius * radius > (enemy.x - x) * (enemy.x - x) + (enemy.y - y) * (enemy.y - y);
    }

    public abstract void shoot(List<Enemy> enemies, Game game);

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

    public int getRadius() {
        return radius;
    }

    public int getTowerCost() {
        return towerCost;
    }

    public int getTowerId() {
        return towerId;
    }

    public int getPlayerId() {
        return playerId;
    }
    
    public int getUpgradeCost() {
        return upgradeCost;
    }
}
