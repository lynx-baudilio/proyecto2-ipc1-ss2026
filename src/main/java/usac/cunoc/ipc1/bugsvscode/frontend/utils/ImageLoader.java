package usac.cunoc.ipc1.bugsvscode.frontend.utils;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public final class ImageLoader {

    public ImageIcon load(String path) {
        ImageIcon icon = loadOptional(path);

        if (icon == null) {
            throw new IllegalArgumentException(
                    "Imagen no encontrada en: " + path
            );
        }

        return icon;
    }

    public ImageIcon loadOptional(String path) {
        URL resource = getClass().getResource(path);

        if (resource == null) {
            return null;
        }

        return new ImageIcon(resource);
    }

    public ImageIcon loadScaled(String path, int width, int height) {
        Image originalImage = load(path).getImage();

        Image scaledImage = originalImage.getScaledInstance(
                width,
                height,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(scaledImage);
    }
}