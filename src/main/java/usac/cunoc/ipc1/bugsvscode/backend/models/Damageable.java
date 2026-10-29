package usac.cunoc.ipc1.bugsvscode.backend.models;

/**
 * Contrato común para recibir daño sin conocer el tipo de entidad.
 */
public interface Damageable {

    double getHealth();

    void takeDamage(double amount);
}