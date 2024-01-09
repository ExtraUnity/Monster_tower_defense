package dk.dtu.mtd.model.game;

import java.util.List;

public class BasicTower extends Tower {
    public BasicTower(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;

        radius = 36;
        damage = 10;
        fireRate = 100;
    }    

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
