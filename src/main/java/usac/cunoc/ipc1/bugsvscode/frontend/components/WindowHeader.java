package usac.cunoc.ipc1.bugsvscode.frontend.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.FontLoader;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageButtonFactory;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageLoader;

public class WindowHeader extends JPanel {

    public WindowHeader(Window owner, String title, String titleImagePath,
            ImageLoader imageLoader, FontLoader fontLoader) {
        setLayout(new BorderLayout());
        setOpaque(false);

        ImageButtonFactory buttonFactory = new ImageButtonFactory(imageLoader);
        add(createControlPanel(owner, buttonFactory), BorderLayout.NORTH);

        if (titleImagePath != null || !title.isBlank()) {
            add(createTitle(title, titleImagePath, imageLoader, fontLoader),
                    BorderLayout.CENTER);
        }
    }

    private JPanel createControlPanel(Window owner,
            ImageButtonFactory buttonFactory) {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        controls.setOpaque(false);

        if (owner instanceof JFrame) {
            JFrame frame = (JFrame) owner;
            JButton minimizeButton = buttonFactory.create(
                    "/assets/images/ui/minimize.png",
                    "/assets/images/ui/minimize-hover.png", 40, 40);
            minimizeButton.setToolTipText("Minimizar");
            minimizeButton.addActionListener(event
                    -> frame.setState(Frame.ICONIFIED));
            controls.add(minimizeButton);
        }

        JButton closeButton = buttonFactory.create(
                "/assets/images/ui/close.png",
                "/assets/images/ui/close-hover.png", 40, 40);
        closeButton.setToolTipText("Cerrar");
        closeButton.addActionListener(event -> owner.dispatchEvent(
                new WindowEvent(owner, WindowEvent.WINDOW_CLOSING)));
        controls.add(closeButton);
        return controls;
    }

    private JLabel createTitle(String title, String titleImagePath,
            ImageLoader imageLoader, FontLoader fontLoader) {
        JLabel label;
        if (titleImagePath != null) {
            label = new JLabel(imageLoader.loadScaled(titleImagePath, 224, 124));
        } else {
            label = new JLabel(title);
            label.setFont(fontLoader.getFont(25));
            label.setForeground(Color.WHITE);
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}