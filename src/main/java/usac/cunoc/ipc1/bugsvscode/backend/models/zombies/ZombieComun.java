package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: NORMAL.
 */
public final class ZombieComun extends Zombie {

    private static final long serialVersionUID = 1L;

    public ZombieComun(int row) {
        super("NORMAL", row, 100, 0, 15, 1);
    }
}