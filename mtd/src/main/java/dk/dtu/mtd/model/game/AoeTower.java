package dk.dtu.mtd.model.game;

import java.util.List;

public class AoeTower extends Tower {

    public AoeTower(int x, int y, int towerId, int playerId, GameTicker gameTicker) {
        this.x = x;
        this.y = y;
        this.playerId = playerId;
        this.type = "aoeTower";
        //this.gameTicker = gameTicker;
        this.lastShot = -1;
        this.towerId = towerId;
        radius = 200;
        damage = 1;
        size = 100;
        fireRate = 70;
        towerCost = 350;
        upgradeCost = 50;
        sellPrice = 175;
    }

    @Override
    public void upgradeTower() {
        super.upgradeTower();   
        if (fireRate > 10){
            fireRate--;
        } else {
            damage++;
        }
    }

    @Override
    public void shoot(List<Enemy> enemies, Game game) {
        if(this.lastShot == -1) {
            this.lastShot = game.gameTicker.gameTick;
        }
        int deltaTick = game.gameTicker.gameTick - lastShot;

        if (deltaTick > fireRate) {
            for (int i = 0; i < enemies.size(); i++) {
                if (inRange(enemies.get(i)) && !enemies.get(i).isDead()) {
                    enemies.get(i).takeDamage(damage, playerId, game);
                    lastShot = game.gameTicker.gameTick;
                    deltaTick = 0;
                }
            }
        }
    }
    

}
