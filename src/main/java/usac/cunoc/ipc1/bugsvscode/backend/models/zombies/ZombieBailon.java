package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: BAILON.
 */
public final class ZombieBailon extends Zombie {

    private static final long serialVersionUID = 1L;

    public ZombieBailon(int row) {
        super("BAILON", row, 150, 0, 15, 0.8);
    }

    @Override
    public boolean summonsDancers() {
        return true;
    }

    @Override
    public boolean canStartDance() {
        return !isDancing() && getDanceClock() >= 5;
    }
}