package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

import java.io.Serializable;
import usac.cunoc.ipc1.bugsvscode.backend.models.Damageable;

/**
 * Entidad móvil. Posición y casilla lógica medidas en unidades de columnas.
 */
public class Zombie implements Serializable, Damageable {

    private static final long serialVersionUID = 1L;
    private final String type;
    private final int row;
    private final double maxHealth;
    private final double attackPerSecond;
    private final double baseSpeed;
    private double x = -0.35;
    private double health;
    private double shield;
    private boolean alive = true;
    private boolean poleUsed;
    private boolean babyThrown;
    private double frozenFor;
    private double jumpClock;
    private double jumpStart;
    private double jumpDestination;
    private boolean jumping;
    private double danceClock;
    private double dancingFor;

    protected Zombie(String type, int row, double health, double shield, double attack, double speed) {
        this.type = type;
        this.row = row;
        this.maxHealth = health;
        this.health = health;
        this.shield = shield;
        this.attackPerSecond = attack;
        this.baseSpeed = speed;
    }

    public String getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getCurrentColumn() {
        return Math.max(0, (int) Math.floor(x + .5));
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getShield() {
        return shield;
    }

    public double getAttackPerSecond() {
        return attackPerSecond;
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * Capacidad redefinida por cada clase especializada.
     */
    public boolean crushesPlants() {
        return false;
    }

    /**
     * La computadora usa estas capacidades, no el nombre del zombi.
     */
    public boolean isArmoredThreat() {
        return false;
    }

    public boolean isAthlete() {
        return false;
    }

    public boolean summonsDancers() {
        return false;
    }

    public boolean throwsBaby() {
        return false;
    }

    public boolean jumpsOnce() {
        return false;
    }

    public void startJump(double destination) {
        jumping = true;
        jumpClock = 0;
        jumpStart = x;
        jumpDestination = destination;
    }

    public boolean isJumping() {
        return jumping;
    }

    public double getJumpProgress() {
        return jumping ? Math.min(1, jumpClock / .55) : 0;
    }

    public void advanceJump(double delta) {
        if (!jumping) {
            return;
        }
        jumpClock += delta;
        double t = Math.min(1, jumpClock / .55);
        x = jumpStart + (jumpDestination - jumpStart) * t;
        if (t >= 1) {
            jumping = false;
        }
    }

    public boolean hasPole() {
        return false;
    }

    protected boolean isPoleUsed() {
        return poleUsed;
    }

    public void usePole() {
        poleUsed = true;
    }

    public boolean isBabyThrown() {
        return babyThrown;
    }

    public void markBabyThrown() {
        babyThrown = true;
    }

    public boolean isDancing() {
        return dancingFor > 0;
    }

    public boolean canStartDance() {
        return false;
    }

    protected double getDanceClock() {
        return danceClock;
    }

    public void startDance() {
        danceClock = 0;
        dancingFor = 1;
    }

    public void updateTime(double delta) {
        frozenFor = Math.max(0, frozenFor - delta);
        if (dancingFor > 0) {
            dancingFor = Math.max(0, dancingFor - delta);
        } else {
            danceClock += delta;
        }
    }

    public void freeze() {
        frozenFor = 4;
    }

    public boolean isFrozen() {
        return frozenFor > 0;
    }

    protected double getMovementSpeed() {
        return baseSpeed;
    }

    public void advance(double delta) {
        x += 0.42 * getMovementSpeed() * (isFrozen() ? .6 : 1) * delta;
    }

    public void kill() {
        alive = false;
        health = 0;
    }

    public void takeDamage(double damage) {
        double blocked = Math.min(shield, damage);
        shield -= blocked;
        health = Math.max(0, health - (damage - blocked));
        if (health <= 0) {
            alive = false;
        }
    }
}