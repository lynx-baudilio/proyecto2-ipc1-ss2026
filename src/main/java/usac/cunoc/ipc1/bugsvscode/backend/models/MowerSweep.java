package usac.cunoc.ipc1.bugsvscode.backend.models;

import java.io.Serializable;

/**
 * Efecto visual de una podadora después de limpiar su carril.
 */
public final class MowerSweep implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int row;
    private double x;

    public MowerSweep(int row, double startingX) {
        this.row = row;
        this.x = startingX;
    }

    public int getRow() {
        return row;
    }

    public double getX() {
        return x;
    }

    public void advance(double dt) {
        x -= dt * 4.5;
    }

    public boolean isActive() {
        return x >= -1.0;
    }
}