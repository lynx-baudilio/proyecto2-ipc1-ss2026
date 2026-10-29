package usac.cunoc.ipc1.bugsvscode.backend.models;

/**
 * Recurso coleccionable, en coordenadas de casillas (sin píxeles ni Swing). Un
 * cerebro vive como máximo cinco segundos y concede 25 al recogerlo.
 */
public final class BrainPickup implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static final int VALUE = 25;
    public static final double LIFETIME_SECONDS = 5.0;

    private final double x;
    private final double y;
    private final int generatorId; // -1 indica que fue liberado por una planta
    private double ageSeconds;

    public BrainPickup(double x, double y, int generatorId) {
        this.x = x;
        this.y = y;
        this.generatorId = generatorId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getGeneratorId() {
        return generatorId;
    }

    public double getRemainingSeconds() {
        return Math.max(0, LIFETIME_SECONDS - ageSeconds);
    }

    public void advance(double delta) {
        ageSeconds += delta;
    }

    public boolean isExpired() {
        return ageSeconds >= LIFETIME_SECONDS;
    }
}