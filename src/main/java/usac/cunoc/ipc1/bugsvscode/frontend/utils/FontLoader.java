package usac.cunoc.ipc1.bugsvscode.frontend.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    private Font baseFont;

    public Font getFont(float size) {
        if (baseFont == null) {
            baseFont = loadFont();
        }
        return baseFont.deriveFont(size);
    }

    private Font loadFont() {
        String path = "/assets/fonts/zombie.ttf";
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                return new Font(Font.SANS_SERIF, Font.BOLD, 16);
            }
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (FontFormatException | IOException exception) {
            System.err.println("No se pudo cargar " + path + ": " + exception.getMessage());
            return new Font(Font.SANS_SERIF, Font.BOLD, 16);
        }
    }
}