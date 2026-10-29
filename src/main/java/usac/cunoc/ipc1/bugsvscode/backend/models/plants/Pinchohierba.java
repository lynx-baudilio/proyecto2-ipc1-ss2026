package usac.cunoc.ipc1.bugsvscode.backend.models.plants;

/**
 * Tipo concreto de planta: PINCHOHIERBA.
 */
public final class Pinchohierba extends Plant {

    private static final long serialVersionUID = 1L;

    public Pinchohierba() {
        super("PINCHOHIERBA", 0);
    }

    @Override
    public boolean isSpike() {
        return true;
    }

    @Override
    public boolean canBeEaten() {
        return false;
    }

    @Override
    public int getContactDamage() {
        return 10;
    }

    @Override
    public double getContactInterval() {
        return 0.5;
    }
}