package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: DEPORTISTA.
 */
public final class ZombieDeportista extends Zombie {

    private static final long serialVersionUID = 1L;

    public ZombieDeportista(int row) {
        super("DEPORTISTA", row, 100, 150, 20, 2);
    }

    @Override
    public boolean isAthlete() {
        return true;
    }
}