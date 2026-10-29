package usac.cunoc.ipc1.bugsvscode.frontend.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import usac.cunoc.ipc1.bugsvscode.backend.models.Account;
import usac.cunoc.ipc1.bugsvscode.backend.service.AccountService;
import usac.cunoc.ipc1.bugsvscode.frontend.components.AccountDialogPanel;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.FontLoader;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageButtonFactory;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageLoader;

public class SelectAccountDialog extends JDialog {

    private final AccountService accountService;
    private final Consumer<String> onSelected;
    private final ImageLoader imageLoader = new ImageLoader();
    private final FontLoader fontLoader = new FontLoader();
    private final JList<String> accountsList = new JList<>();

    public SelectAccountDialog(JFrame parent, AccountService accountService,
            Consumer<String> onSelected) {
        super(parent, "Seleccionar cuenta", true);
        this.accountService = accountService;
        this.onSelected = onSelected;

        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(createContentPanel());
        setSize(540, 420);
        setLocationRelativeTo(parent);
        refreshAccounts();
    }

    private JPanel createContentPanel() {
        ImageButtonFactory buttonFactory = new ImageButtonFactory(imageLoader);
        JButton selectButton = buttonFactory.create(
                "/assets/images/dialogs/select.png",
                "/assets/images/dialogs/select-hover.png", 175, 90);
        selectButton.addActionListener(event -> selectAccount());
        getRootPane().setDefaultButton(selectButton);

        return new AccountDialogPanel(this, "SELECCIONAR CUENTA",
                "/assets/images/dialogs/select-account-title.png",
                createAccountsPanel(), selectButton, imageLoader, fontLoader);
    }

    private JPanel createAccountsPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(6, 85, 6, 85));

        accountsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsList.setFont(fontLoader.getFont(21));
        accountsList.setBackground(new Color(244, 239, 222));
        JScrollPane scrollPane = new JScrollPane(accountsList);
        scrollPane.setPreferredSize(new Dimension(310, 130));
        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private void refreshAccounts() {
        try {
            String[] nicknames = accountService.listAccounts();
            accountsList.setListData(nicknames);
            if (nicknames.length > 0) {
                accountsList.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Todavía no hay cuentas. Creá una desde el login.");
            }
        } catch (IOException exception) {
            accountsList.setListData(new String[0]);
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Error al leer cuentas", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectAccount() {
        String nickname = accountsList.getSelectedValue();
        if (nickname == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná una cuenta.");
            return;
        }
        try {
            Account account = accountService.selectAccount(nickname);
            dispose();
            onSelected.accept(account.getNickname());
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Error al abrir la cuenta", JOptionPane.ERROR_MESSAGE);
            refreshAccounts();
        }
    }
}