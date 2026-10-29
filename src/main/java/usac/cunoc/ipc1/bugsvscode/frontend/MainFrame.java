package usac.cunoc.ipc1.bugsvscode.frontend;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import usac.cunoc.ipc1.bugsvscode.backend.models.LevelConfig;
import usac.cunoc.ipc1.bugsvscode.backend.persistence.LevelReader;
import usac.cunoc.ipc1.bugsvscode.backend.persistence.AccountRepository;
import usac.cunoc.ipc1.bugsvscode.backend.persistence.GameSaveRepository;
import usac.cunoc.ipc1.bugsvscode.backend.persistence.StatisticsRepository;
import usac.cunoc.ipc1.bugsvscode.backend.service.GameEngine;
import usac.cunoc.ipc1.bugsvscode.frontend.dialogs.StatisticsDialog;
import usac.cunoc.ipc1.bugsvscode.backend.service.AccountService;
import usac.cunoc.ipc1.bugsvscode.frontend.components.ImageBackgroundPanel;
import usac.cunoc.ipc1.bugsvscode.frontend.components.WindowHeader;
import usac.cunoc.ipc1.bugsvscode.frontend.panels.GamePanel;
import usac.cunoc.ipc1.bugsvscode.frontend.panels.LoginPanel;
import usac.cunoc.ipc1.bugsvscode.frontend.panels.MainMenuPanel;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.FontLoader;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.ImageLoader;

public class MainFrame extends JFrame {

    private final AccountService accountService
            = new AccountService(new AccountRepository());
    private final ImageLoader imageLoader = new ImageLoader();
    private final FontLoader fontLoader = new FontLoader();

    public MainFrame() {
        setTitle("Bugs vs Code");
        setUndecorated(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        configureWindowSize();
        configureCloseConfirmation();
        showLogin();
    }

    private void configureWindowSize() {
        Rectangle screen = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = (int) (screen.width * 0.80);
        int height = (int) (screen.height * 0.80);
        setSize(width, height);
        setLocation(screen.x + (screen.width - width) / 2,
                screen.y + (screen.height - height) / 2);
    }

    private void configureCloseConfirmation() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                confirmExit();
            }
        });
    }

    private void confirmExit() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que querés salir?",
                "Salir del juego",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private void showLogin() {
        showScreen(new LoginPanel(accountService, this::showMainMenu),
                "/assets/images/login/background.png");
    }

    private void showMainMenu(String nickname) {
        showScreen(new MainMenuPanel(nickname,
                () -> startNewGame(nickname), () -> resumeSavedGame(nickname),
                () -> showStatistics(nickname), this::showLogin, this::confirmExit),
                "/assets/images/menu/background.png");
    }

    private void resumeSavedGame(String nickname) {
        GameSaveRepository saves = new GameSaveRepository();
        String[] names = saves.list(nickname);
        if (names.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay partidas guardadas.");
            return;
        }
        String selected = (String) JOptionPane.showInputDialog(this, "Seleccioná una partida", "Reanudar",
                JOptionPane.QUESTION_MESSAGE, null, names, names[0]);
        if (selected == null) {
            return;
        }
        try {
            GameEngine engine = saves.load(nickname, selected);
            showGame(new GamePanel(engine, nickname, () -> showMainMenu(nickname), true));
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Error al cargar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStatistics(String nickname) {
        try {
            new StatisticsDialog(this, nickname, new StatisticsRepository(new AccountRepository())).setVisible(true);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Escoger el nivel y la dificultad sin poner la lógica en MainMenuPanel.
     */
    private void startNewGame(String nickname) {
        File suggested = new File("src/docs/niveles");
        JFileChooser chooser = suggested.isDirectory() ? new JFileChooser(suggested) : new JFileChooser();
        chooser.setDialogTitle("Elegir nivel JSON");
        chooser.setFileFilter(new FileNameExtensionFilter("Niveles JSON (*.json)", "json"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File levelFile = chooser.getSelectedFile();

        String[] difficulties = {"Fácil", "Medio", "Difícil"};
        String difficulty = (String) JOptionPane.showInputDialog(this,
                "Seleccioná la dificultad:", "Nueva partida",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);
        if (difficulty == null) {
            return;
        }

        try {
            LevelConfig config = new LevelReader().read(levelFile);
            showGame(new GamePanel(config, difficulty, nickname,
                    () -> showMainMenu(nickname)));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Nivel JSON inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showGame(JPanel gamePanel) {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new java.awt.Color(22, 38, 31));
        content.add(new WindowHeader(this, "", null,
                imageLoader, fontLoader), BorderLayout.NORTH);
        content.add(gamePanel, BorderLayout.CENTER);
        setContentPane(content);
        revalidate();
        repaint();
    }

    private void showScreen(JPanel screen, String backgroundPath) {
        ImageBackgroundPanel windowContent = new ImageBackgroundPanel(
                imageLoader.load(backgroundPath).getImage());
        windowContent.setLayout(new BorderLayout());
        windowContent.add(new WindowHeader(this, "", null,
                imageLoader, fontLoader), BorderLayout.NORTH);
        windowContent.add(screen, BorderLayout.CENTER);
        setContentPane(windowContent);
        revalidate();
        repaint();
    }
}