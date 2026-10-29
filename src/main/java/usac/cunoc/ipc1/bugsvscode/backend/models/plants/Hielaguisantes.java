package usac.cunoc.ipc1.bugsvscode.backend.models.plants;

/**
 * Tipo concreto de planta: HIELAGUISANTES.
 */
public final class Hielaguisantes extends Plant {

    private static final long serialVersionUID = 1L;

    public Hielaguisantes() {
        super("HIELAGUISANTES", 100);
    }

    @Override
    public double getCadenceSeconds() {
        return 1.5;
    }

    @Override
    public int getShotDamage() {
        return 15;
    }

    @Override
    public boolean freezesOnHit() {
        return true;
    }
}