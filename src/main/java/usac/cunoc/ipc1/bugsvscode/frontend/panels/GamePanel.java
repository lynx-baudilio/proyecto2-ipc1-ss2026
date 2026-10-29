package usac.cunoc.ipc1.bugsvscode.frontend.panels;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import usac.cunoc.ipc1.bugsvscode.backend.models.*;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Plant;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombie;
import usac.cunoc.ipc1.bugsvscode.backend.persistence.*;
import usac.cunoc.ipc1.bugsvscode.backend.service.GameEngine;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.SpriteCatalog;
import usac.cunoc.ipc1.bugsvscode.frontend.components.GardenCanvas;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.GeneratorWorkers;

/**
 * Interfaz de juego: presentación y eventos, sin reglas de combate.
 */
public final class GamePanel extends JPanel {

    private final GameEngine engine;
    private final Runnable onBack;
    private final String nickname;
    private final Timer timer;
    private final JLabel info = new JLabel();
    private final JLabel notice = new JLabel("Seleccioná un zombi y luego un carril", SwingConstants.CENTER);
    private final GardenCanvas garden;
    private final SpriteCatalog sprites = new SpriteCatalog();
    private final GeneratorWorkers workers;
    private final GameSaveRepository saves = new GameSaveRepository();
    private final StatisticsRepository statistics = new StatisticsRepository(new AccountRepository());
    private final JButton[] typeButtons = new JButton[6];
    private String selectedType = "NORMAL";
    private long lastTick;

    public GamePanel(LevelConfig config, String difficulty, String nickname, Runnable onBack) {
        this(new GameEngine(config, Difficulty.fromLabel(difficulty)), nickname, onBack, false);
    }

    public GamePanel(GameEngine engine, String nickname, Runnable onBack, boolean startPaused) {
        this.engine = engine;
        garden = new GardenCanvas(engine, sprites);
        this.nickname = nickname;
        this.onBack = onBack;
        engine.enableThreadedGenerators();
        workers = new GeneratorWorkers(engine);
        setLayout(new BorderLayout(8, 8));
        setBackground(new Color(22, 38, 31));
        setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JPanel toolbar = new JPanel(new BorderLayout(8, 0));
        toolbar.setOpaque(false);
        info.setForeground(Color.WHITE);
        info.setFont(new Font("SansSerif", Font.BOLD, 13));
        toolbar.add(info, BorderLayout.CENTER);
        JPanel actions = new JPanel(new GridLayout(1, 2, 5, 0));
        actions.setOpaque(false);
        JButton pause = new JButton("Pausar");
        pause.addActionListener(e -> pauseGame());
        JButton menu = new JButton("Menú");
        menu.addActionListener(e -> leaveGame());
        actions.add(pause);
        actions.add(menu);
        toolbar.add(actions, BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);
        JPanel catalog = new JPanel(new GridLayout(7, 1, 3, 3));
        catalog.setOpaque(false);
        JLabel label = new JLabel("ZOMBIS", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        catalog.add(label);
        String[] types = ZombieFactory.purchasableTypes();
        for (int i = 0; i < types.length; i++) {
            final String type = types[i];
            JButton button = new JButton();
            button.setMargin(new Insets(1, 3, 1, 3));
            button.setFont(new Font("SansSerif", Font.BOLD, 11));
            button.addActionListener(e -> choose(type));
            typeButtons[i] = button;
            catalog.add(button);
        }
        add(catalog, BorderLayout.WEST);
        notice.setForeground(new Color(244, 225, 155));
        notice.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(notice, BorderLayout.SOUTH);
        add(garden, BorderLayout.CENTER);
        garden.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickGarden(e.getX(), e.getY());
            }
        });
        choose("NORMAL");
        refresh();
        timer = new Timer(50, e -> {
            long now = System.nanoTime();
            double delta = (now - lastTick) / 1_000_000_000.0;
            lastTick = now;
            engine.tick(delta);
            refresh();
            garden.repaint();
            if (engine.isFinished()) {
                endGame();
            }
        });
        lastTick = System.nanoTime();
        if (startPaused) {
            SwingUtilities.invokeLater(this::pauseGame);
        } else {
            workers.start();
            timer.start();
        }
    }

    public void stop() {
        timer.stop();
        workers.stop();
    }

    @Override
    public void removeNotify() {
        stop();
        super.removeNotify();
    }

    private void choose(String type) {
        selectedType = type;
        refresh();
        notice.setText("Elegiste " + type + ". Hacé clic en un carril.");
    }

    private void refresh() {
        int remain = (int) Math.ceil(engine.getRemainingSeconds());
        String phase = engine.getBlock() == 4 ? " | HORDA -50%" : engine.getBlock() == 7 ? " | HORDA FINAL -75%" : "";
        info.setText(nickname + " | " + engine.getDifficulty() + " | Cerebros: " + engine.getBrains()
                + " | " + String.format("%02d:%02d", remain / 60, remain % 60) + phase);
        String[] types = ZombieFactory.purchasableTypes();
        for (int i = 0; i < typeButtons.length; i++) {
            int price = engine.getCost(types[i]);
            typeButtons[i].setText(types[i] + " (" + price + ")");
            typeButtons[i].setBackground(types[i].equals(selectedType) ? new Color(159, 215, 150) : null);
        }
    }

    private void clickGarden(int x, int y) {
        if (engine.isFinished()) {
            return;
        }
        if (garden.tryCollectBrain(x, y)) {
            notice.setText("¡+25 cerebros!");
            refresh();
            garden.repaint();
            return;
        }
        int row = garden.getClickedRow(x, y);
        if (row < 0) {
            return;
        }
        if (engine.invoke(selectedType, row)) {
            notice.setText("¡Zombi en el carril " + (row + 1) + "!");
        } else {
            notice.setText("Cerebros insuficientes");
            JOptionPane.showMessageDialog(this, "No alcanza para invocar este zombi.");
        }
        refresh();
        garden.repaint();
    }

    private void pauseGame() {
        if (engine.isFinished()) {
            return;
        }
        timer.stop();
        workers.stop();
        Object[] options = {"Reanudar", "Guardar y salir"};
        int choice = JOptionPane.showOptionDialog(this, "Partida pausada. ¿Qué querés hacer?", "Pausa",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) {
            try {
                saves.save(nickname, engine);
                stop();
                onBack.run();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
                resume();
            }
        } else {
            resume();
        }
    }

    private void resume() {
        lastTick = System.nanoTime();
        if (!engine.isFinished()) {
            workers.start();
            timer.start();
        }
    }

    private void leaveGame() {
        timer.stop();
        workers.stop();
        int option = JOptionPane.showConfirmDialog(this, "¿Volver al menú sin guardar?", "Salir", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            stop();
            onBack.run();
        } else {
            resume();
        }
    }

    private void endGame() {
        timer.stop();
        workers.stop();
        try {
            statistics.recordCompleted(nickname, engine);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron actualizar estadísticas: " + ex.getMessage());
        }
        String status = engine.isVictory() ? "¡VICTORIA!" : "DERROTA";
        JOptionPane.showMessageDialog(this, status + "\nTiempo transcurrido: " + (int) Math.ceil(engine.getElapsedSeconds())
                + " s\nPlantas destruidas: " + engine.getPlantsDestroyed() + "\nZombis invocados: " + engine.getZombiesInvoked()
                + "\nPodadoras activadas: " + engine.getMowersTriggered(), "Fin de partida", JOptionPane.INFORMATION_MESSAGE);
        onBack.run();
    }

}