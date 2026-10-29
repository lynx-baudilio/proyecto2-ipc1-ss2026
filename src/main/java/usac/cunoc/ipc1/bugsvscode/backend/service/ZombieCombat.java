package usac.cunoc.ipc1.bugsvscode.backend.service;

import usac.cunoc.ipc1.bugsvscode.backend.models.*;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Plant;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombie;

/**
 * Movimiento de zombis, habilidades especiales y ataques cuerpo a cuerpo. El
 * motor administra el estado global y las podadoras.
 */
final class ZombieCombat {

    private ZombieCombat() {
    }

    static void advanceZombies(GameEngine engine, double delta) {
        int originalCount = engine.getZombieCount();
        for (int i = 0; i < originalCount && !engine.isFinished(); i++) {
            Zombie z = engine.getZombie(i);
            if (!z.isAlive()) {
                continue;
            }
            z.updateTime(delta);
            if (z.isJumping()) {
                z.advanceJump(delta);
                continue;
            }
            String type = z.getType();
            if (z.throwsBaby() && !z.isBabyThrown() && z.getHealth() <= z.getMaxHealth() * .5) {
                Zombie baby = ZombieFactory.create("ZOMBIDITO", z.getRow());
                baby.setX(Math.max(0, engine.getCols() - 2.45));
                engine.addZombie(baby);
                z.markBabyThrown();
            }
            if (z.canStartDance()) {
                z.startDance();
                summonDancers(engine, z);
                continue;
            }
            if (z.isDancing()) {
                continue;
            }
            int col = findPlantAhead(engine, z);
            if (col >= 0) {
                Plant p = engine.getPlant(z.getRow(), col);
                if (p == null) {
                    continue;
                }
                if (z.jumpsOnce() && z.hasPole()) {
                    z.usePole();
                    if (!(p instanceof usac.cunoc.ipc1.bugsvscode.backend.models.plants.Nuez)) {
                        z.startJump(col + 1.15);
                        continue;
                    }
                }
                if (z.crushesPlants()) {
                    engine.destroyPlant(z.getRow(), col);
                    engine.addCrushMark(z.getRow(), col);
                    continue;
                }
                if (p.canBeEaten()) {
                    p.takeDamage(z.getAttackPerSecond() * delta);
                    if (p.isDestroyed()) {
                        engine.destroyPlant(z.getRow(), col);
                    }
                    continue;
                }
            }
            z.advance(delta);
            if (z.getX() >= engine.getCols() - .05) {
                engine.reachHouse(z.getRow());
            }
        }
    }

    private static void summonDancers(GameEngine engine, Zombie dancer) {
        int row = dancer.getRow();
        int col = dancer.getCurrentColumn();
        int[] rows = {row - 1, row + 1, row, row};
        int[] cols = {col, col, col - 1, col + 1};
        for (int i = 0; i < 4; i++) {
            if (rows[i] >= 0 && rows[i] < engine.getRows() && cols[i] >= 0 && cols[i] < engine.getCols()) {
                if (engine.getPlant(rows[i], cols[i]) != null) {
                    continue;
                }
                boolean occupied = false;
                for (int j = 0; j < engine.getZombieCount(); j++) {
                    if (engine.getZombie(j).isAlive() && engine.getZombie(j).getRow() == rows[i]
                            && engine.getZombie(j).getCurrentColumn() == cols[i]) {
                        occupied = true;
                        break;
                    }
                }
                if (occupied) {
                    continue;
                }
                Zombie summoned = ZombieFactory.create("BAILARIN", rows[i]);
                summoned.setX(cols[i]);
                engine.addZombie(summoned);
            }
        }
    }

    private static int findPlantAhead(GameEngine engine, Zombie z) {
        for (int col = 0; col < engine.getCols(); col++) {
            Plant p = engine.getPlant(z.getRow(), col);
            if (p != null && z.getX() >= col - .48 && z.getX() <= col + .05) {
                if (p.canBeEaten() || z.crushesPlants() || p instanceof usac.cunoc.ipc1.bugsvscode.backend.models.plants.Nuez) {
                    return col;
                }
            }
        }
        return -1;
    }
}