package usac.cunoc.ipc1.bugsvscode.frontend.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Sprites opcionales empaquetados en resources; las figuras siguen siendo
 * fallback.
 */
public final class SpriteCatalog {

    private static final int MAX_SPRITES = 32;
    private final String[] paths = new String[MAX_SPRITES];
    private final BufferedImage[] cached = new BufferedImage[MAX_SPRITES];
    private final boolean[] attempted = new boolean[MAX_SPRITES];
    private int count;

    public SpriteCatalog() {
        // Los nombres son convenciones, no requieren configuración por entidad.
        for (String type : new String[]{"lanzaguisantes", "patatapum", "hielaguisantes", "nuez",
            "carronivora", "pinchohierba", "bipetidora"}) {
            add("plants/" + type + ".png");
        }
        for (String type : new String[]{"normal", "caracono", "garrocha", "deportista",
            "bailon", "zombistein", "zombidito", "bailarin"}) {
            add("zombies/" + type + ".png");
        }
        for (String type : new String[]{"nuez-agrietada", "nuez-danada", "patatapum-inactiva",
            "patatapum-activa", "carronivora-digiriendo"}) {
            add("plants/" + type + ".png");
        }
        for (String type : new String[]{"normal-sin-brazo", "caracono-sin-cono", "garrocha-sin-garrocha",
            "deportista-sin-casco", "zombistein-sin-zombidito", "bailon-bailando"}) {
            add("zombies/" + type + ".png");
        }
        for (String type : new String[]{"brain", "pea", "ice-pea", "mower", "house"}) {
            add("effects/" + type + ".png");
        }
    }

    private void add(String path) {
        if (count >= paths.length) {
            throw new IllegalStateException("Catálogo de sprites sin espacio.");
        }
        paths[count++] = "/assets/images/game/" + path;
    }

    public boolean draw(Graphics2D graphics, String relative, int x, int y, int width, int height) {
        String full = "/assets/images/game/" + relative;
        for (int i = 0; i < count; i++) {
            if (paths[i].equals(full)) {
                if (!attempted[i]) {
                    attempted[i] = true;
                    try (java.io.InputStream input = getClass().getResourceAsStream(full)) {
                        if (input != null) {
                            cached[i] = ImageIO.read(input);
                        }
                    } catch (IOException ignore) {
                        cached[i] = null;
                    }
                }
                if (cached[i] == null) {
                    break;
                }
                graphics.drawImage(cached[i], x, y, width, height, null);
                return true;
            }
        }
        int dash = relative.indexOf('-');
        if (dash > 0 && relative.endsWith(".png")) {
            String prefix = relative.substring(0, dash) + ".png";
            // Si falta una variante se muestra el sprite base, nunca una imagen rota.
            return draw(graphics, prefix, x, y, width, height);
        }
        return false;
    }
}