package usac.cunoc.ipc1.bugsvscode.backend.models.plants;

/**
 * Tipo concreto de planta: CARRONIVORA.
 */
public final class Carronivora extends Plant {

    private static final long serialVersionUID = 1L;

    public Carronivora() {
        super("CARRONIVORA", 100);
    }

    @Override
    public boolean isCarnivore() {
        return true;
    }

    @Override
    public int getContactDamage() {
        return 200;
    }
}