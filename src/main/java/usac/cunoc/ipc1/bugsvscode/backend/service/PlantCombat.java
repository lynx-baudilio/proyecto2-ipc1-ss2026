package usac.cunoc.ipc1.bugsvscode.backend.service;

import usac.cunoc.ipc1.bugsvscode.backend.models.*;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Plant;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombie;

/**
 * Ataques de plantas y colisiones de proyectiles. Las reglas comunes están en
 * un lugar; cada planta entrega sus propios valores.
 */
final class PlantCombat {

    private PlantCombat() {
    }

    static void advancePlants(GameEngine engine, double delta) {
        for (int row = 0; row < engine.getRows(); row++) {
            for (int col = 0; col < engine.getCols(); col++) {
                Plant p = engine.getPlant(row, col);
                if (p == null) {
                    continue;
                }
                p.advanceClock(delta);
                if (p.getCadenceSeconds() > 0 && p.ready(p.getCadenceSeconds())) {
                    boolean fired = false;
                    if (zombieInDirection(engine, row, col, -1)) {
                        engine.addProjectile(new Projectile(row, col - .2, -1, p.getShotDamage(), p.freezesOnHit()));
                        fired = true;
                    }
                    if (p.firesBackwardTwice() && zombieInDirection(engine, row, col, 1)) {
                        engine.addProjectile(new Projectile(row, col + .2, 1, p.getShotDamage(), false));
                        engine.addProjectile(new Projectile(row, col + .2, 1, p.getShotDamage(), false));
                        fired = true;
                    }
                    if (fired) {
                        p.resetClock();
                    }
                } else if (p.isMine() && p.isArmed()) {
                    boolean found = false;
                    for (int z = 0; z < engine.getZombieCount(); z++) {
                        if (engine.getZombie(z).isAlive() && engine.getZombie(z).getRow() == row
                                && engine.getZombie(z).getCurrentColumn() == col) {
                            engine.getZombie(z).takeDamage(p.getContactDamage());
                            found = true;
                        }
                    }
                    if (found) {
                        engine.destroyPlant(row, col);
                    }
                } else if (p.isSpike() && p.ready(p.getContactInterval())) {
                    for (int z = 0; z < engine.getZombieCount(); z++) {
                        if (engine.getZombie(z).isAlive() && engine.getZombie(z).getRow() == row
                                && engine.getZombie(z).getCurrentColumn() == col) {
                            engine.getZombie(z).takeDamage(p.getContactDamage());
                        }
                    }
                    p.resetClock();
                } else if (p.isCarnivore() && p.getDigestRemaining() <= 0) {
                    for (int z = 0; z < engine.getZombieCount(); z++) {
                        Zombie enemy = engine.getZombie(z);
                        if (enemy.isAlive() && enemy.getRow() == row
                                && (enemy.getCurrentColumn() == col || enemy.getCurrentColumn() == col - 1)) {
                            if (enemy.crushesPlants()) {
                                enemy.takeDamage(p.getContactDamage());
                                p.startDigestion();
                            } else {
                                enemy.kill();
                                p.startDigestion();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean zombieInDirection(GameEngine engine, int row, int col, int direction) {
        for (int i = 0; i < engine.getZombieCount(); i++) {
            Zombie z = engine.getZombie(i);
            if (z.isAlive() && z.getRow() == row && (direction < 0 ? z.getX() < col : z.getX() > col)) {
                return true;
            }
        }
        return false;
    }

    static void advanceProjectiles(GameEngine engine, double delta) {
        for (int i = 0; i < engine.getProjectileCount(); i++) {
            Projectile p = engine.getProjectile(i);
            if (!p.isActive()) {
                continue;
            }
            double previous = p.getX();
            p.move(delta);
            Zombie target = null;
            double best = Double.MAX_VALUE;
            for (int j = 0; j < engine.getZombieCount(); j++) {
                Zombie z = engine.getZombie(j);
                if (!z.isAlive() || z.getRow() != p.getRow()) {
                    continue;
                }
                double distance = Math.abs(previous - z.getX());
                boolean hit = p.getDirection() < 0 ? z.getX() <= previous + .12 && z.getX() >= p.getX() - .12
                        : z.getX() >= previous - .12 && z.getX() <= p.getX() + .12;
                if (hit && distance < best) {
                    best = distance;
                    target = z;
                }
            }
            if (target != null) {
                target.takeDamage(p.getDamage());
                if (p.isFrozen()) {
                    target.freeze();
                }
                p.deactivate();
            } else if (p.getX() < -.6 || p.getX() > engine.getCols() + .5) {
                p.deactivate();
            }
        }
    }
}