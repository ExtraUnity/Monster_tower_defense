package dk.dtu.mtd.model.game;

import java.util.List;

public abstract class Tower {
    int radius;
    int x;
    int y;
    int damage;
    int fireRate;
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

    public void shoot(List<Enemy> enemies) {
        if (lastShot <= 0) {
            lastShot = fireRate;
            for(int i = 0; i < enemies.size(); i++) {
                if(inRange(enemies.get(i))) {
                enemies.get(i).health -= damage;
                break;
                }
            }
        } else {
            lastShot--;
        }
    }



}
