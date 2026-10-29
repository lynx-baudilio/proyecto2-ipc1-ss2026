package usac.cunoc.ipc1.bugsvscode.backend.models.plants;

/**
 * Tipo concreto de planta: BIPETIDORA.
 */
public final class Bipetidora extends Plant {

    private static final long serialVersionUID = 1L;

    public Bipetidora() {
        super("BIPETIDORA", 100);
    }

    @Override
    public double getCadenceSeconds() {
        return 1;
    }

    @Override
    public int getShotDamage() {
        return 15;
    }

    @Override
    public boolean firesBackwardTwice() {
        return true;
    }
}