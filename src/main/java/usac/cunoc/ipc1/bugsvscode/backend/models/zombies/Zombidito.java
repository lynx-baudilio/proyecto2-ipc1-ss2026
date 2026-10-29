package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: ZOMBIDITO.
 */
public final class Zombidito extends Zombie {

    private static final long serialVersionUID = 1L;

    public Zombidito(int row) {
        super("ZOMBIDITO", row, 50, 0, 15, 1.7);
    }
}