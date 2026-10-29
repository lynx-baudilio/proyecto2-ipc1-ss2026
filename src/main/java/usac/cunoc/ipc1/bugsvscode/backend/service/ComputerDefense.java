package usac.cunoc.ipc1.bugsvscode.backend.service;

import java.util.Random;
import usac.cunoc.ipc1.bugsvscode.backend.models.PlantFactory;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Plant;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombie;

/**
 * Estrategias de siembra de la computadora, independientes del tiempo y de
 * Swing. Por cada evaluación se ejecuta como máximo una siembra.
 */
final class ComputerDefense {

    private static final String[] SEEDS = {"LANZAGUISANTES", "PATATAPUM", "HIELAGUISANTES",
        "NUEZ", "CARRONIVORA", "PINCHOHIERBA", "BIPETIDORA"};

    private ComputerDefense() {
    }

    static void medium(Plant[][] plants, Random random) {
        // En orden desde la casa; dentro de una columna, de arriba hacia abajo.
        for (int col = plants[0].length - 1; col >= 0; col--) {
            for (int row = 0; row < plants.length; row++) {
                if (plants[row][col] == null) {
                    plants[row][col] = PlantFactory.create(SEEDS[random.nextInt(SEEDS.length)]);
                    return;
                }
            }
        }
    }

    static void hard(Plant[][] plants, Zombie[] zombies, int count) {
        for (int i = 0; i < count; i++) {
            Zombie z = zombies[i];
            if (z.isAlive() && plants[0].length - z.getCurrentColumn() <= 4
                    && (placeAhead(plants, z, "NUEZ") || placeAhead(plants, z, "CARRONIVORA"))) {
                return;
            }
        }
        for (int i = 0; i < count; i++) {
            Zombie z = zombies[i];
            if (z.isAlive() && passedPlant(plants, z) && placeBehind(plants, z, "BIPETIDORA")) {
                return;
            }
        }
        for (int i = 0; i < count; i++) {
            Zombie z = zombies[i];
            if (!z.isAlive()) {
                continue;
            }
            if (z.isArmoredThreat()
                    && (placeAhead(plants, z, "PATATAPUM") || placeAhead(plants, z, "CARRONIVORA"))) {
                return;
            }
            if (z.isAthlete() && placeAhead(plants, z, "HIELAGUISANTES")) {
                return;
            }
            if (z.jumpsOnce() && placeAhead(plants, z, "NUEZ")) {
                return;
            }
        }
        for (int row = 0; row < plants.length; row++) {
            int inLane = 0;
            for (int i = 0; i < count; i++) {
                if (zombies[i].isAlive() && zombies[i].getRow() == row) {
                    inLane++;
                }
            }
            if (inLane > 3 && placeNearHouse(plants, row, "PINCHOHIERBA")) {
                return;
            }
        }
        int least = Integer.MAX_VALUE;
        int bestRow = -1;
        for (int row = 0; row < plants.length; row++) {
            int defenses = 0;
            boolean available = false;
            for (Plant p : plants[row]) {
                if (p == null) {
                    available = true;
                } else {
                    defenses++;
                }
            }
            if (available && defenses < least) {
                least = defenses;
                bestRow = row;
            }
        }
        if (bestRow >= 0) {
            placeNearHouse(plants, bestRow, "LANZAGUISANTES");
        }
    }

    private static boolean passedPlant(Plant[][] plants, Zombie z) {
        for (int col = 0; col < z.getCurrentColumn() && col < plants[0].length; col++) {
            if (plants[z.getRow()][col] != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean placeAhead(Plant[][] plants, Zombie z, String type) {
        for (int col = Math.max(0, z.getCurrentColumn() + 1); col < plants[0].length; col++) {
            if (plants[z.getRow()][col] == null) {
                plants[z.getRow()][col] = PlantFactory.create(type);
                return true;
            }
        }
        return false;
    }

    private static boolean placeBehind(Plant[][] plants, Zombie z, String type) {
        for (int col = Math.min(plants[0].length - 1, z.getCurrentColumn() - 1); col >= 0; col--) {
            if (plants[z.getRow()][col] == null) {
                plants[z.getRow()][col] = PlantFactory.create(type);
                return true;
            }
        }
        return false;
    }

    private static boolean placeNearHouse(Plant[][] plants, int row, String type) {
        for (int col = plants[row].length - 1; col >= 0; col--) {
            if (plants[row][col] == null) {
                plants[row][col] = PlantFactory.create(type);
                return true;
            }
        }
        return false;
    }
}