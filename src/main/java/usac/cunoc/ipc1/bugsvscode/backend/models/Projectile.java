package usac.cunoc.ipc1.bugsvscode.backend.models;

import java.io.Serializable;

/**
 * Proyectil autónomo con daño, dirección y ralentización propios.
 */
public final class Projectile implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int row;
    private double x;
    private final int direction;
    private final int damage;
    private final boolean frozen;
    private boolean active = true;

    public Projectile(int row, double x) {
        this(row, x, -1, 20, false);
    }

    public Projectile(int row, double x, int direction, int damage, boolean frozen) {
        this.row = row;
        this.x = x;
        this.direction = direction;
        this.damage = damage;
        this.frozen = frozen;
    }

    public int getRow() {
        return row;
    }

    public double getX() {
        return x;
    }

    public int getDirection() {
        return direction;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isActive() {
        return active;
    }

    public void move(double delta) {
        x += direction * 3.0 * delta;
    }

    public void deactivate() {
        active = false;
    }
}