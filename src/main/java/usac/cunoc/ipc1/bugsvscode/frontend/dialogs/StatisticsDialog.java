package usac.cunoc.ipc1.bugsvscode.frontend.dialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import javax.swing.*;
import usac.cunoc.ipc1.bugsvscode.backend.models.PlayerStats;
import usac.cunoc.ipc1.bugsvscode.backend.persistence.StatisticsRepository;

/**
 * Dos pestañas con estadísticas personales y ranking Top 5.
 */
public final class StatisticsDialog extends JDialog {

    public StatisticsDialog(JFrame owner, String nickname, StatisticsRepository repository) throws IOException {
        super(owner, "Sala de la fama", true);
        JTabbedPane tabs = new JTabbedPane();
        PlayerStats current = repository.load(nickname);
        tabs.addTab("Mi cuenta", scroll(statsText(current)));
        PlayerStats[] all = repository.loadAll();
        tabs.addTab("Ranking global", scroll(rankingText(all)));
        add(tabs, BorderLayout.CENTER);
        setSize(520, 430);
        setLocationRelativeTo(owner);
    }

    private JScrollPane scroll(String contents) {
        JTextArea text = new JTextArea(contents);
        text.setEditable(false);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        text.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return new JScrollPane(text);
    }

    private String statsText(PlayerStats s) {
        return "Jugador: " + s.getNickname() + "\n\nPartidas jugadas: " + s.getGamesPlayed()
                + "\nPartidas ganadas: " + s.getGamesWon() + "\nPartidas perdidas: " + s.getGamesLost()
                + "\nPlantas destruidas: " + s.getPlantsDestroyed() + "\nZombis invocados: " + s.getZombiesSummoned()
                + "\nCerebros gastados: " + s.getBrainsSpent() + "\nMejor tiempo (s): " + s.getBestVictoryTimeSeconds()
                + "\nPodadoras activadas: " + s.getLawnmowersActivated()
                + String.format("\nPorcentaje de victorias: %.1f%%\nPromedio plantas/partida: %.2f", s.getWinRate(), s.getAveragePlants());
    }

    private String rankingText(PlayerStats[] all) {
        StringBuilder text = new StringBuilder();
        String[] categories = {"Top 5 invasores más rápidos", "Top 5 mayores destructores", "Top 5 jugadores más exitosos"};
        for (int mode = 0; mode < 3; mode++) {
            text.append(categories[mode]).append("\n");
            PlayerStats[] sorted = all.clone();
            for (int i = 0; i < sorted.length; i++) {
                for (int j = i + 1; j < sorted.length; j++) {
                    if (better(sorted[j], sorted[i], mode)) {
                        PlayerStats t = sorted[i];
                        sorted[i] = sorted[j];
                        sorted[j] = t;
                    }
                }
            }
            int shown = 0;
            for (PlayerStats s : sorted) {
                int metric = mode == 0 ? s.getBestVictoryTimeSeconds() : mode == 1 ? s.getPlantsDestroyed() : s.getGamesWon();
                if (mode == 0 && metric < 0) {
                    continue;
                }
                text.append("  ").append(++shown).append(". ").append(s.getNickname()).append(" — ").append(metric)
                        .append(mode == 0 ? " s" : "").append("\n");
                if (shown == 5) {
                    break;
                }
            }
            if (shown == 0) {
                text.append("  Sin resultados todavía\n");
            }
            text.append("\n");
        }
        return text.toString();
    }

    private boolean better(PlayerStats a, PlayerStats b, int mode) {
        int x = mode == 0 ? a.getBestVictoryTimeSeconds() : mode == 1 ? a.getPlantsDestroyed() : a.getGamesWon();
        int y = mode == 0 ? b.getBestVictoryTimeSeconds() : mode == 1 ? b.getPlantsDestroyed() : b.getGamesWon();
        if (mode == 0) {
            if (x < 0) {
                return false;
            }
            if (y < 0) {
                return true;
            }
            return x != y ? x < y : a.getNickname().compareTo(b.getNickname()) < 0;
        }
        return x != y ? x > y : a.getNickname().compareTo(b.getNickname()) < 0;
    }
}