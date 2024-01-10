package dk.dtu.mtd.model.game;

import java.util.List;

public class TowerManager implements Runnable {
    public List<Tower> towers;

    public TowerManager(List<Tower> towers) {
        this.towers = towers;
    }

    @Override
    public void run() {
        while(true) {
            for(Tower tower : towers) {
                tower.shoot(Game.waveManager.waveLeft.enemies);
            }
        }
    }


}
