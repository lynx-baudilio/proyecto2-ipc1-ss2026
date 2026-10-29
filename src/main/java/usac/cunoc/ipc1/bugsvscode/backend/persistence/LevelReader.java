package usac.cunoc.ipc1.bugsvscode.backend.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import usac.cunoc.ipc1.bugsvscode.backend.models.LevelConfig;

/**
 * Intérprete del formato JSON fijo requerido por el Proyecto 2. No usa
 * bibliotecas externas ni estructuras del Collections Framework.
 */
public class LevelReader {

    public LevelConfig read(File file) throws IOException {
        if (file == null || !file.isFile()) {
            throw new IOException("El archivo seleccionado no existe.");
        }

        StringBuilder contents = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contents.append(line).append('\n');
            }
        }
        return new Parser(contents.toString()).parseLevel();
    }

    private static class Parser {

        private final String source;
        private int pos;

        private Parser(String source) {
            this.source = source;
        }

        private LevelConfig parseLevel() throws IOException {
            expect('{');
            expectKey("duracionSegundos");
            expect(':');
            int duration = readInt();
            expect(',');
            expectKey("cerebrosIniciales");
            expect(':');
            int brains = readInt();
            expect(',');
            expectKey("jardin");
            expect(':');
            String[][] garden = readGarden();
            expect('}');
            skipSpaces();
            if (pos != source.length()) {
                throw error("Hay contenido después del objeto JSON.");
            }
            if (duration <= 0) {
                throw error("duracionSegundos debe ser mayor que cero.");
            }
            if (brains < 0) {
                throw error("cerebrosIniciales no puede ser negativo.");
            }
            return new LevelConfig(duration, brains, garden);
        }

        private String[][] readGarden() throws IOException {
            expect('[');
            String[][] rows = new String[4][];
            int count = 0;
            int expectedColumns = -1;
            skipSpaces();
            if (currentIs(']')) {
                throw error("El jardín debe contener al menos un carril.");
            }

            while (true) {
                String[] row = readRow();
                if (expectedColumns == -1) {
                    expectedColumns = row.length;
                } else if (row.length != expectedColumns) {
                    throw error("Todos los carriles deben tener igual número de columnas.");
                }
                if (count == rows.length) {
                    String[][] bigger = new String[rows.length * 2][];
                    System.arraycopy(rows, 0, bigger, 0, rows.length);
                    rows = bigger;
                }
                rows[count++] = row;
                skipSpaces();
                if (currentIs(',')) {
                    pos++;
                } else {
                    break;
                }
            }
            expect(']');
            String[][] result = new String[count][];
            System.arraycopy(rows, 0, result, 0, count);
            return result;
        }

        private String[] readRow() throws IOException {
            expect('[');
            String[] plants = new String[4];
            int count = 0;
            skipSpaces();
            if (currentIs(']')) {
                throw error("Cada carril debe tener al menos una casilla.");
            }
            while (true) {
                String plant = readString();
                if (!validPlant(plant)) {
                    throw error("Identificador de planta desconocido: " + plant);
                }
                if (count == plants.length) {
                    String[] bigger = new String[plants.length * 2];
                    System.arraycopy(plants, 0, bigger, 0, plants.length);
                    plants = bigger;
                }
                plants[count++] = plant;
                skipSpaces();
                if (currentIs(',')) {
                    pos++;
                } else {
                    break;
                }
            }
            expect(']');
            String[] result = new String[count];
            System.arraycopy(plants, 0, result, 0, count);
            return result;
        }

        private boolean validPlant(String plant) {
            switch (plant) {
                case "VACIO":
                case "LANZAGUISANTES":
                case "PATATAPUM":
                case "HIELAGUISANTES":
                case "NUEZ":
                case "CARRONIVORA":
                case "PINCHOHIERBA":
                case "BIPETIDORA":
                    return true;
                default:
                    return false;
            }
        }

        private void expectKey(String key) throws IOException {
            String found = readString();
            if (!key.equals(found)) {
                throw error("Se esperaba la propiedad \"" + key + "\".");
            }
        }

        private String readString() throws IOException {
            expect('"');
            StringBuilder value = new StringBuilder();
            while (pos < source.length() && source.charAt(pos) != '"') {
                char character = source.charAt(pos++);
                // El formato fijo exige únicamente claves e identificadores simples.
                if (character == '\\' || character < ' ') {
                    throw error("Cadena JSON no válida para este formato fijo.");
                }
                value.append(character);
            }
            if (pos >= source.length()) {
                throw error("Faltan las comillas de cierre.");
            }
            pos++;
            return value.toString();
        }

        private int readInt() throws IOException {
            skipSpaces();
            int start = pos;
            if (currentIs('-')) {
                pos++;
            }
            while (pos < source.length() && source.charAt(pos) >= '0'
                    && source.charAt(pos) <= '9') {
                pos++;
            }
            if (start == pos || (source.charAt(start) == '-' && pos == start + 1)) {
                throw error("Se esperaba un número entero.");
            }
            try {
                return Integer.parseInt(source.substring(start, pos));
            } catch (NumberFormatException ex) {
                throw error("Número entero fuera de rango.");
            }
        }

        private void expect(char character) throws IOException {
            skipSpaces();
            if (pos >= source.length() || source.charAt(pos) != character) {
                throw error("Se esperaba '" + character + "'.");
            }
            pos++;
        }

        private boolean currentIs(char character) {
            return pos < source.length() && source.charAt(pos) == character;
        }

        private void skipSpaces() {
            while (pos < source.length()
                    && (Character.isWhitespace(source.charAt(pos))
                    || source.charAt(pos) == '\uFEFF')) {
                pos++;
            }
        }

        private IOException error(String message) {
            return new IOException(message + " (posición " + pos + ")");
        }
    }
}