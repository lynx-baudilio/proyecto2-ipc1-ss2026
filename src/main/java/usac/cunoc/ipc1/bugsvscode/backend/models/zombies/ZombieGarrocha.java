package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: GARROCHA.
 */
public final class ZombieGarrocha extends Zombie {

    private static final long serialVersionUID = 1L;

    public ZombieGarrocha(int row) {
        super("GARROCHA", row, 120, 0, 15, 1.7);
    }

    @Override
    public boolean jumpsOnce() {
        return true;
    }

    @Override
    public boolean hasPole() {
        return !isPoleUsed();
    }

    @Override
    protected double getMovementSpeed() {
        return hasPole() ? 1.7 : 1;
    }
}