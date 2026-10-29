package usac.cunoc.ipc1.bugsvscode.frontend.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class ImageBackgroundPanel extends JPanel {

    private final Image backgroundImage;

    public ImageBackgroundPanel(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        setBackground(new Color(49, 77, 48));
    }

    public double getBackgroundScale() {
        int imageWidth = backgroundImage.getWidth(this);
        int imageHeight = backgroundImage.getHeight(this);
        if (imageWidth <= 0 || imageHeight <= 0) {
            return 1.0;
        }
        return Math.max((double) getWidth() / imageWidth,
                (double) getHeight() / imageHeight);
    }

    public int imageXToPanel(int imageX) {
        double scale = getBackgroundScale();
        int scaledWidth = (int) Math.ceil(backgroundImage.getWidth(this) * scale);
        return (getWidth() - scaledWidth) / 2
                + (int) Math.round(imageX * scale);
    }

    public int imageYToPanel(int imageY) {
        double scale = getBackgroundScale();
        int scaledHeight = (int) Math.ceil(backgroundImage.getHeight(this) * scale);
        return (getHeight() - scaledHeight) / 2
                + (int) Math.round(imageY * scale);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int imageWidth = backgroundImage.getWidth(this);
        int imageHeight = backgroundImage.getHeight(this);
        if (imageWidth <= 0 || imageHeight <= 0) {
            return;
        }

        double scale = getBackgroundScale();
        int scaledWidth = (int) Math.ceil(imageWidth * scale);
        int scaledHeight = (int) Math.ceil(imageHeight * scale);
        int x = (getWidth() - scaledWidth) / 2;
        int y = (getHeight() - scaledHeight) / 2;

        graphics.drawImage(backgroundImage, x, y,
                scaledWidth, scaledHeight, this);
    }
}