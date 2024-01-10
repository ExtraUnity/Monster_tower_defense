package dk.dtu.mtd.model.game;

import java.util.List;

public class BasicTower extends Tower {
    public BasicTower(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.type = "basicTower";
        this.lastShot = Game.gameTicker.gameTick;
        radius = 36;
        damage = 10;
        fireRate = 100;
    }    

    public void shoot(List<Enemy> enemies) {
        int deltaTick = Game.gameTicker.gameTick - lastShot;
        if (deltaTick > fireRate) {
            lastShot = Game.gameTicker.gameTick;
            deltaTick = 0;
            for(int i = 0; i < enemies.size(); i++) {
                if(inRange(enemies.get(i))) {
                    enemies.get(i).health -= damage;
                    System.out.println("I shot an enemy!!");
                    break;
                }
            }
        }
    }
}
