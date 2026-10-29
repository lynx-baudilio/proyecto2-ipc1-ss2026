package usac.cunoc.ipc1.bugsvscode.backend.models.zombies;

/**
 * Tipo especializado de zombi: CARACONO.
 */
public final class ZombieCaracono extends Zombie {

    private static final long serialVersionUID = 1L;

    public ZombieCaracono(int row) {
        super("CARACONO", row, 100, 100, 20, 1);
    }

    @Override
    public double getAttackPerSecond() {
        return getShield() > 0 ? 20 : 15;
    }

    @Override
    public boolean isArmoredThreat() {
        return true;
    }
}