package usac.cunoc.ipc1.bugsvscode.backend.config;

import java.io.File;

/**
 * Rutas persistentes fuera del JAR; centralizadas en un solo lugar.
 */
public final class AppPaths {

    private AppPaths() {
    }

    public static File playersDirectory() {
        return new File(new File(System.getProperty("user.home"), ".bugsvscode"), "players");
    }
}