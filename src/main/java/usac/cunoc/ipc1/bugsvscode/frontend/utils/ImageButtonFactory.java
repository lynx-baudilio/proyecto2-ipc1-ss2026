package usac.cunoc.ipc1.bugsvscode.frontend.utils;

import javax.swing.JButton;

public class ImageButtonFactory {

    private final ImageLoader imageLoader;

    public ImageButtonFactory(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public JButton create(
            String normalPath,
            String hoverPath,
            int width,
            int height
    ) {
        JButton button = new JButton();

        button.setIcon(imageLoader.loadScaled(
                normalPath, width, height
        ));

        button.setRolloverIcon(imageLoader.loadScaled(
                hoverPath, width, height
        ));

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }
}