package usac.cunoc.ipc1.bugsvscode.backend.models;

import java.io.Serializable;

/**
 * Efecto temporal de aplastamiento, independiente del dibujo.
 */
public final class CrushMark implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int row, col;
    private double remaining = .7;

    public CrushMark(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public double getRemaining() {
        return remaining;
    }

    public void advance(double dt) {
        remaining -= dt;
    }
}