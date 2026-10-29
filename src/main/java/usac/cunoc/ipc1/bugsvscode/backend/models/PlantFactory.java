package usac.cunoc.ipc1.bugsvscode.backend.models;

import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Bipetidora;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Carronivora;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Hielaguisantes;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Lanzaguisantes;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Nuez;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Patatapum;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Pinchohierba;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Plant;

/**
 * Centraliza qué clase concreta corresponde al identificador JSON.
 */
public final class PlantFactory {

    private PlantFactory() {
    }

    public static Plant create(String type) {
        switch (type) {
            case "LANZAGUISANTES":
                return new Lanzaguisantes();
            case "PATATAPUM":
                return new Patatapum();
            case "HIELAGUISANTES":
                return new Hielaguisantes();
            case "NUEZ":
                return new Nuez();
            case "CARRONIVORA":
                return new Carronivora();
            case "PINCHOHIERBA":
                return new Pinchohierba();
            case "BIPETIDORA":
                return new Bipetidora();
            default:
                throw new IllegalArgumentException("Planta desconocida: " + type);
        }
    }
}