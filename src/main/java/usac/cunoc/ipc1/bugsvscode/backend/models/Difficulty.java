package usac.cunoc.ipc1.bugsvscode.backend.models;

import java.io.Serializable;

/**
 * Dificultad de la partida. Se modela con atributos y constructores sencillos.
 * Cada opción solo contiene su nombre y la cantidad de generadores.
 */
public final class Difficulty implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final Difficulty EASY = new Difficulty("Fácil", 6);
    public static final Difficulty MEDIUM = new Difficulty("Medio", 5);
    public static final Difficulty HARD = new Difficulty("Difícil", 4);

    private final String name;
    private final int generatorCount;

    private Difficulty(String name, int generatorCount) {
        this.name = name;
        this.generatorCount = generatorCount;
    }

    public int getGeneratorCount() {
        return generatorCount;
    }

    public boolean isEasy() {
        return "Fácil".equals(name);
    }

    public boolean isMedium() {
        return "Medio".equals(name);
    }

    public static Difficulty fromLabel(String label) {
        if ("Fácil".equalsIgnoreCase(label) || "Facil".equalsIgnoreCase(label)) {
            return EASY;
        }
        if ("Medio".equalsIgnoreCase(label)) {
            return MEDIUM;
        }
        if ("Difícil".equalsIgnoreCase(label) || "Dificil".equalsIgnoreCase(label)) {
            return HARD;
        }
        throw new IllegalArgumentException("Dificultad inválida: " + label);
    }

    @Override
    public String toString() {
        return name;
    }
}