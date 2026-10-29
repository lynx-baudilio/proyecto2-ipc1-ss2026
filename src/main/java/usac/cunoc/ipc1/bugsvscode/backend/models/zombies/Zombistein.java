package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: ZOMBISTEIN.
 */
public final class Zombistein extends Zombie {

    private static final long serialVersionUID = 1L;

    public Zombistein(int row) {
        super("ZOMBISTEIN", row, 300, 0, 0, 0.7);
    }

    @Override
    public boolean crushesPlants() {
        return true;
    }

    @Override
    public boolean throwsBaby() {
        return true;
    }

    @Override
    public boolean isArmoredThreat() {
        return true;
    }
}