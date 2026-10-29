package usac.cunoc.ipc1.bugsvscode.frontend.panels;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import usac.cunoc.ipc1.bugsvscode.frontend.components.ImageBackgroundPanel;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.FontLoader;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageButtonFactory;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageLoader;

/**
 * Coloca los botones sobre las cinco tablas dibujadas en menu/background.png.
 * Las posiciones son relativas a la imagen, no a la resolución del monitor.
 */
public class MainMenuPanel extends JPanel {

    private final ImageLoader imageLoader = new ImageLoader();
    private final ImageButtonFactory buttonFactory
            = new ImageButtonFactory(imageLoader);
    private final FontLoader fontLoader = new FontLoader();
    private final String[] buttonImages = {
        "new-game", "upload-game", "stats", "change-player", "exit"
    };
    private final int[] boardCentersY = {405, 647, 883, 1130, 1374};
    private final JButton[] buttons = new JButton[5];
    private final JLabel playerLabel;
    private int previousButtonWidth;
    private int previousButtonHeight;

    public MainMenuPanel(String nickname, Runnable onNewGame, Runnable onResume, Runnable onStats, Runnable onLogout, Runnable onExit) {
        setLayout(null);
        setOpaque(false);

        playerLabel = new JLabel("JUGADOR: " + nickname, SwingConstants.CENTER);
        playerLabel.setForeground(new Color(25, 19, 12));
        add(playerLabel);

        createButtons(onNewGame, onResume, onStats, onLogout, onExit);
    }

    private void createButtons(Runnable onNewGame, Runnable onResume, Runnable onStats, Runnable onLogout, Runnable onExit) {
        for (int index = 0; index < buttons.length; index++) {
            String path = "/assets/images/menu/" + buttonImages[index];
            JButton button = buttonFactory.create(
                    path + ".png", path + "-hover.png", 190, 98);
            buttons[index] = button;
            add(button);
        }

        buttons[0].addActionListener(event -> onNewGame.run());
        buttons[1].addActionListener(event -> onResume.run());
        buttons[2].addActionListener(event -> onStats.run());
        buttons[3].addActionListener(event -> onLogout.run());
        buttons[4].addActionListener(event -> onExit.run());
    }

    @Override
    public void doLayout() {
        if (!(getParent() instanceof ImageBackgroundPanel)) {
            return;
        }

        ImageBackgroundPanel background = (ImageBackgroundPanel) getParent();
        double scale = background.getBackgroundScale();

        int buttonWidth = Math.max(1, (int) Math.round(455 * scale));
        int buttonHeight = Math.max(1, (int) Math.round(220 * scale));
        updateButtonImages(buttonWidth, buttonHeight);

        for (int index = 0; index < buttons.length; index++) {
            int x = background.imageXToPanel(1700) - buttonWidth / 2;
            int y = background.imageYToPanel(boardCentersY[index])
                    - getY() - buttonHeight / 2;
            buttons[index].setBounds(x, y, buttonWidth, buttonHeight);
        }

        int labelWidth = (int) Math.round(720 * scale);
        int labelHeight = (int) Math.round(100 * scale);
        int labelX = background.imageXToPanel(500) - labelWidth / 2;
        int labelY = background.imageYToPanel(470)
                - getY() - labelHeight / 2;
        playerLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        Font font = fontLoader.getFont(Math.max(12, (int) Math.round(50 * scale)));
        playerLabel.setFont(font);
    }

    private void updateButtonImages(int width, int height) {
        if (width == previousButtonWidth && height == previousButtonHeight) {
            return;
        }
        previousButtonWidth = width;
        previousButtonHeight = height;

        for (int index = 0; index < buttons.length; index++) {
            String path = "/assets/images/menu/" + buttonImages[index];
            buttons[index].setIcon(imageLoader.loadScaled(path + ".png", width, height));
            buttons[index].setRolloverIcon(imageLoader.loadScaled(
                    path + "-hover.png", width, height));
        }
    }
}