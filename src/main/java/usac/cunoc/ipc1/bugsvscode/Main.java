package usac.cunoc.ipc1.bugsvscode;

import javax.swing.SwingUtilities;
import usac.cunoc.ipc1.bugsvscode.frontend.MainFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame window = new MainFrame();
            window.setVisible(true);
        });
    }
}