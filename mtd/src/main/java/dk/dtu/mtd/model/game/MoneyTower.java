package dk.dtu.mtd.model.game;

import java.util.List;

public class MoneyTower extends Tower{
    int payOut;

    public MoneyTower(int x, int y, int towerId, int playerId, GameTicker gameTicker) {
        this.x = x;
        this.y = y;
        this.playerId = playerId;
        this.type = "moneyTower";
        this.lastShot = -1;
        this.towerId = towerId;
        radius = 100;
        damage = 0;
        size = 100;
        fireRate = 800;
        towerCost = 250;
        upgradeCost = 50;
        sellPrice = 50;
        payOut = 10; 
    }

    @Override
    public void upgradeTower() {
        super.upgradeTower();
        payOut += 5;
    }

    @Override
    public void shoot(List<Enemy> enemies, Game game) {
        if(this.lastShot == -1) {
            this.lastShot = game.gameTicker.gameTick;
        }
        int deltaTick = game.gameTicker.gameTick - lastShot;

        if (deltaTick > fireRate) {
            if(game.player1.id == playerId){
                //then player one owns the tower and gets rewarded
                game.player1.addReward(payOut);
            } else {
                game.player2.addReward(payOut);
            }
            game.updateReward(playerId);
            hasShot(game);

            lastShot = game.gameTicker.gameTick;
            deltaTick = 0;
        }
    }

    @Override
    public String getStatsAsString() {
        return "payOut: " + payOut + " \nGrowthSpeed: " + Math.round((50D / (double)fireRate * 100D)*payOut) / 100D + " coins/sec\nSellPrice: "
                + sellPrice;
    }
    
}
