package usac.cunoc.ipc1.bugsvscode.backend.models;

/**
 * Datos de una partida obtenidos de un archivo JSON, sin depender de Swing.
 */
public class LevelConfig {

    private final int duracionSegundos;
    private final int cerebrosIniciales;
    private final String[][] jardin;

    public LevelConfig(int duracionSegundos, int cerebrosIniciales, String[][] jardin) {
        if (duracionSegundos <= 0 || cerebrosIniciales < 0 || jardin == null
                || jardin.length == 0 || jardin[0] == null || jardin[0].length == 0) {
            throw new IllegalArgumentException("La configuración del nivel no es válida.");
        }
        int columns = jardin[0].length;
        for (String[] row : jardin) {
            if (row == null || row.length != columns) {
                throw new IllegalArgumentException("Todos los carriles deben tener igual tamaño.");
            }
            for (String plant : row) {
                if (plant == null) {
                    throw new IllegalArgumentException("No puede haber plantas nulas.");
                }
            }
        }
        this.duracionSegundos = duracionSegundos;
        this.cerebrosIniciales = cerebrosIniciales;
        this.jardin = new String[jardin.length][];
        for (int fila = 0; fila < jardin.length; fila++) {
            this.jardin[fila] = jardin[fila].clone();
        }
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public int getCerebrosIniciales() {
        return cerebrosIniciales;
    }

    public int getFilas() {
        return jardin.length;
    }

    public int getColumnas() {
        return jardin[0].length;
    }

    public String getPlanta(int fila, int columna) {
        return jardin[fila][columna];
    }
}