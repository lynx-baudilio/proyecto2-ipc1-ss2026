package usac.cunoc.ipc1.bugsvscode.backend.models;

import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombidito;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombie;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.ZombieBailarin;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.ZombieBailon;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.ZombieCaracono;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.ZombieComun;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.ZombieDeportista;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.ZombieGarrocha;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombistein;

/**
 * Punto único de creación. Zombidito y bailarines son invocaciones especiales.
 */
public final class ZombieFactory {

    private ZombieFactory() {
    }

    public static Zombie create(String type, int row) {
        switch (type) {
            case "NORMAL":
                return new ZombieComun(row);
            case "CARACONO":
                return new ZombieCaracono(row);
            case "GARROCHA":
                return new ZombieGarrocha(row);
            case "DEPORTISTA":
                return new ZombieDeportista(row);
            case "BAILON":
                return new ZombieBailon(row);
            case "ZOMBISTEIN":
                return new Zombistein(row);
            case "ZOMBIDITO":
                return new Zombidito(row);
            case "BAILARIN":
                return new ZombieBailarin(row);
            default:
                throw new IllegalArgumentException("Zombi desconocido: " + type);
        }
    }

    public static int baseCost(String type) {
        switch (type) {
            case "NORMAL":
                return 50;
            case "CARACONO":
            case "GARROCHA":
                return 100;
            case "DEPORTISTA":
            case "BAILON":
                return 150;
            case "ZOMBISTEIN":
                return 350;
            default:
                return -1;
        }
    }

    public static String[] purchasableTypes() {
        return new String[]{"NORMAL", "CARACONO", "GARROCHA", "DEPORTISTA", "BAILON", "ZOMBISTEIN"};
    }
}