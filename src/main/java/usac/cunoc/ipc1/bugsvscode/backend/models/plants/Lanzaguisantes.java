package usac.cunoc.ipc1.bugsvscode.backend.models.plants;

/**
 * Tipo concreto de planta: LANZAGUISANTES.
 */
public final class Lanzaguisantes extends Plant {

    private static final long serialVersionUID = 1L;

    public Lanzaguisantes() {
        super("LANZAGUISANTES", 100);
    }

    @Override
    public double getCadenceSeconds() {
        return 1;
    }

    @Override
    public int getShotDamage() {
        return 20;
    }
}