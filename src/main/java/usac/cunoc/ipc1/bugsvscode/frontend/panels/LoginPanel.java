package usac.cunoc.ipc1.bugsvscode.frontend.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import usac.cunoc.ipc1.bugsvscode.backend.service.AccountService;
import usac.cunoc.ipc1.bugsvscode.frontend.dialogs.CreateAccountDialog;
import usac.cunoc.ipc1.bugsvscode.frontend.dialogs.SelectAccountDialog;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageButtonFactory;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageLoader;

public class LoginPanel extends JPanel {

    private final ImageLoader imageLoader = new ImageLoader();
    private final ImageButtonFactory buttonFactory = new ImageButtonFactory(imageLoader);
    private final AccountService accountService;
    private final Consumer<String> onLogin;

    public LoginPanel(AccountService accountService, Consumer<String> onLogin) {
        this.accountService = accountService;
        this.onLogin = onLogin;
        setLayout(new GridBagLayout());
        setOpaque(false);
        initializeComponents();
    }

    private void initializeComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets.top = 12;
        add(createContentPanel(), constraints);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.add(createTitleLabel(), BorderLayout.NORTH);
        panel.add(createButtonPanel(), BorderLayout.CENTER);
        return panel;
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel(imageLoader.loadScaled(
                "/assets/images/login/title.png", 440, 215));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 18, 0));
        panel.setOpaque(false);
        panel.add(createLoginChoice("/assets/images/login/gravestone.png",
                "/assets/images/login/create-account.png",
                "/assets/images/login/create-account-hover.png", this::openCreateAccountDialog));
        panel.add(createLoginChoice("/assets/images/login/zombie.png",
                "/assets/images/login/login.png",
                "/assets/images/login/login-hover.png", this::openSelectAccountDialog));
        return panel;
    }

    private JPanel createLoginChoice(String image, String button, String hover, Runnable action) {
        JPanel column = new JPanel(new BorderLayout(0, 5));
        column.setOpaque(false);
        JLabel decoration = new JLabel(imageLoader.loadScaled(image, 200, 190));
        decoration.setHorizontalAlignment(JLabel.CENTER);
        column.add(decoration, BorderLayout.CENTER);
        JButton select = buttonFactory.create(button, hover, 208, 104);
        select.addActionListener(event -> action.run());
        column.add(select, BorderLayout.SOUTH);
        return column;
    }

    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }

    private void openCreateAccountDialog() {
        CreateAccountDialog dialog = new CreateAccountDialog(
                getParentFrame(), accountService, onLogin);
        dialog.setVisible(true);
    }

    private void openSelectAccountDialog() {
        SelectAccountDialog dialog = new SelectAccountDialog(
                getParentFrame(), accountService, onLogin);
        dialog.setVisible(true);
    }
}