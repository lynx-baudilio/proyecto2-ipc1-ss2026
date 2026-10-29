package usac.cunoc.ipc1.bugsvscode.frontend.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.FontLoader;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageButtonFactory;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageLoader;

/**
 * Fondo, encabezado y botones compartidos por los diálogos de cuentas.
 */
public class AccountDialogPanel extends ImageBackgroundPanel {

    public AccountDialogPanel(Window owner, String title, String titleImagePath,
            JPanel centralPanel, JButton actionButton,
            ImageLoader imageLoader, FontLoader fontLoader) {
        super(imageLoader.load("/assets/images/dialogs/background.png").getImage());
        setLayout(new BorderLayout(0, 12));

        add(new WindowHeader(owner, title, titleImagePath,
                imageLoader, fontLoader), BorderLayout.NORTH);
        add(centralPanel, BorderLayout.CENTER);
        add(createButtonPanel(owner, actionButton, imageLoader), BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel(Window owner, JButton actionButton,
            ImageLoader imageLoader) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 16, 12));

        ImageButtonFactory buttonFactory = new ImageButtonFactory(imageLoader);
        JButton cancelButton = buttonFactory.create(
                "/assets/images/ui/cancel.png",
                "/assets/images/ui/cancel-hover.png", 175, 90);
        cancelButton.addActionListener(event -> owner.dispose());

        buttonPanel.add(actionButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }
}