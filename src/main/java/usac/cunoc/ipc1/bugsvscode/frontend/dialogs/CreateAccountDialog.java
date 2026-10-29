package usac.cunoc.ipc1.bugsvscode.frontend.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import usac.cunoc.ipc1.bugsvscode.backend.models.Account;
import usac.cunoc.ipc1.bugsvscode.backend.service.AccountService;
import usac.cunoc.ipc1.bugsvscode.frontend.components.AccountDialogPanel;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.FontLoader;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageButtonFactory;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageLoader;

public class CreateAccountDialog extends JDialog {

    private final ImageLoader imageLoader = new ImageLoader();
    private final FontLoader fontLoader = new FontLoader();
    private final JTextField usernameField = new JTextField(18);
    private final AccountService accountService;
    private final Consumer<String> onCreated;

    public CreateAccountDialog(JFrame parent, AccountService accountService,
            Consumer<String> onCreated) {
        super(parent, "Crear cuenta", true);
        this.accountService = accountService;
        this.onCreated = onCreated;

        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(createContentPanel());
        setSize(540, 420);
        setLocationRelativeTo(parent);
    }

    private JPanel createContentPanel() {
        ImageButtonFactory buttonFactory = new ImageButtonFactory(imageLoader);
        JButton createButton = buttonFactory.create(
                "/assets/images/dialogs/create.png",
                "/assets/images/dialogs/create-hover.png", 175, 90);
        createButton.addActionListener(event -> createAccount());
        getRootPane().setDefaultButton(createButton);

        return new AccountDialogPanel(this, "CREAR CUENTA",
                "/assets/images/dialogs/create-account-title.png",
                createInputPanel(), createButton, imageLoader, fontLoader);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        JPanel inputGroup = new JPanel(new BorderLayout(0, 8));
        inputGroup.setOpaque(false);

        JLabel nicknameLabel = new JLabel("NICKNAME", SwingConstants.CENTER);
        nicknameLabel.setFont(fontLoader.getFont(22));
        nicknameLabel.setForeground(Color.WHITE);

        usernameField.setPreferredSize(new Dimension(270, 42));
        usernameField.setFont(fontLoader.getFont(23));
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setBorder(BorderFactory.createLineBorder(
                new Color(73, 107, 54), 2));

        inputGroup.add(nicknameLabel, BorderLayout.NORTH);
        inputGroup.add(usernameField, BorderLayout.CENTER);
        inputPanel.add(inputGroup);
        return inputPanel;
    }

    private void createAccount() {
        try {
            Account account = accountService.createAccount(usernameField.getText());
            dispose();
            onCreated.accept(account.getNickname());
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Nickname inválido", JOptionPane.WARNING_MESSAGE);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la cuenta: " + exception.getMessage(),
                    "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}