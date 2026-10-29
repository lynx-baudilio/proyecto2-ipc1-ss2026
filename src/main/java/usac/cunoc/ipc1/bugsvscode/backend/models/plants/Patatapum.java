package usac.cunoc.ipc1.bugsvscode.backend.models.plants;

/**
 * Tipo concreto de planta: PATATAPUM.
 */
public final class Patatapum extends Plant {

    private static final long serialVersionUID = 1L;

    public Patatapum() {
        super("PATATAPUM", 50);
    }

    @Override
    public boolean isArmed() {
        return hasPreparedFor(10);
    }

    @Override
    public boolean isMine() {
        return true;
    }

    @Override
    public int getContactDamage() {
        return 100;
    }
}