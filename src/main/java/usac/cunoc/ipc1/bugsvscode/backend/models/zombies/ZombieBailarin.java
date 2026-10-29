package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: BAILARIN.
 */
public final class ZombieBailarin extends Zombie {

    private static final long serialVersionUID = 1L;

    public ZombieBailarin(int row) {
        super("BAILARIN", row, 100, 0, 15, 1);
    }
}