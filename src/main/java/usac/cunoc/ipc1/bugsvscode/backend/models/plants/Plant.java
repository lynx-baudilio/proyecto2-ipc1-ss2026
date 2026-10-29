package usac.cunoc.ipc1.bugsvscode.backend.models.plants;

import java.io.Serializable;
import usac.cunoc.ipc1.bugsvscode.backend.models.Damageable;

/**
 * Estado y comportamiento compartido. La capa gráfica lee, nunca altera, estos
 * datos.
 */
public class Plant implements Serializable, Damageable {

    private static final long serialVersionUID = 1L;
    private final String type;
    private final int maxHealth;
    private double health;
    private double age;
    private double cooldown;
    private double digestRemaining;

    protected Plant(String type, int maxHealth) {
        this.type = type;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public String getType() {
        return type;
    }

    public double getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public double getAge() {
        return age;
    }

    public boolean isArmed() {
        return false;
    }

    protected boolean hasPreparedFor(double seconds) {
        return age >= seconds;
    }

    public double getDigestRemaining() {
        return digestRemaining;
    }

    public void startDigestion() {
        digestRemaining = 8;
    }

    public void advanceClock(double delta) {
        age += delta;
        cooldown += delta;
        digestRemaining = Math.max(0, digestRemaining - delta);
    }

    public boolean ready(double seconds) {
        return cooldown >= seconds;
    }

    public void resetClock() {
        cooldown = 0;
    }

    // Facilita comprobar la cadencia sin que el modelo dependa de la GUI.
    public boolean canShoot() {
        return ready(1);
    }

    public void resetShootClock() {
        resetClock();
    }

    /**
     * Los subtipos redefinen los parámetros de su disparo.
     */
    public double getCadenceSeconds() {
        return -1;
    }

    public int getShotDamage() {
        return 0;
    }

    public boolean freezesOnHit() {
        return false;
    }

    public boolean firesBackwardTwice() {
        return false;
    }

    public boolean canBeEaten() {
        return true;
    }

    public boolean isMine() {
        return false;
    }

    public boolean isSpike() {
        return false;
    }

    public boolean isCarnivore() {
        return false;
    }

    public int getContactDamage() {
        return 0;
    }

    public double getContactInterval() {
        return -1;
    }

    public void takeDamage(double damage) {
        if (canBeEaten()) {
            health = Math.max(0, health - damage);
        }
    }

    public boolean isDestroyed() {
        return canBeEaten() && health <= 0;
    }
}