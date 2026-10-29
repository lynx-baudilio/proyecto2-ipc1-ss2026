package usac.cunoc.ipc1.bugsvscode.backend.models;

import java.util.Random;

/**
 * Generador independiente con su propio ciclo de espera. Se actualiza desde el
 * motor, sin modificar Swing ni compartir hilos.
 */
public final class BrainGenerator implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private double secondsUntilSpawn;
    private boolean hasActivePickup;

    public BrainGenerator(Random random) {
        resetWaiting(random);
    }

    public boolean advance(double delta) {
        if (hasActivePickup) {
            return false;
        }
        secondsUntilSpawn -= delta;
        if (secondsUntilSpawn <= 0) {
            hasActivePickup = true;
            return true;
        }
        return false;
    }

    /**
     * Empieza otro ciclo únicamente cuando se recoge o expira el cerebro.
     */
    public void finishCycle(Random random) {
        hasActivePickup = false;
        resetWaiting(random);
    }

    private void resetWaiting(Random random) {
        secondsUntilSpawn = 5.0 + random.nextDouble() * 10.0;
    }
}