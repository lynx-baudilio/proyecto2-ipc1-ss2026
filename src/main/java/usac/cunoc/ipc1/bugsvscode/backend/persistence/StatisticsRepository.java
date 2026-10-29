package usac.cunoc.ipc1.bugsvscode.backend.persistence;

import java.io.*;
import java.nio.charset.StandardCharsets;
import usac.cunoc.ipc1.bugsvscode.backend.models.PlayerStats;
import usac.cunoc.ipc1.bugsvscode.backend.service.GameEngine;

/**
 * Un único txt UTF-8 por cuenta; top cinco a partir de arreglos nativos.
 */
public final class StatisticsRepository {

    private final AccountRepository accounts;

    public StatisticsRepository(AccountRepository accounts) {
        this.accounts = accounts;
    }

    private File file(String nickname) {
        return new File(accounts.getAccountFolder(nickname), "statistics.txt");
    }

    public PlayerStats load(String nickname) throws IOException {
        PlayerStats stats = new PlayerStats(nickname);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file(nickname)), StandardCharsets.UTF_8))) {
            String line = in.readLine();
            if (!(("nickname=" + nickname).equals(line))) {
                throw new IOException("Cuenta dañada.");
            }
            while ((line = in.readLine()) != null) {
                int pos = line.indexOf('=');
                if (pos < 1) {
                    throw new IOException("Estadística inválida.");
                }
                String key = line.substring(0, pos);
                int n;
                try {
                    n = Integer.parseInt(line.substring(pos + 1));
                } catch (NumberFormatException ex) {
                    throw new IOException("Valor no numérico", ex);
                }
                if (n < 0 && !("bestVictoryTimeSeconds".equals(key) && n == -1)) {
                    throw new IOException("Valor negativo inválido");
                }
                try {
                    stats.setValue(key, n);
                } catch (IllegalArgumentException ex) {
                    throw new IOException("Estadística desconocida: " + key, ex);
                }
            }
        }
        return stats;
    }

    public void save(PlayerStats s) throws IOException {
        File target = file(s.getNickname());
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), StandardCharsets.UTF_8))) {
            write(out, "nickname", s.getNickname());
            write(out, "gamesPlayed", s.getGamesPlayed());
            write(out, "gamesWon", s.getGamesWon());
            write(out, "gamesLost", s.getGamesLost());
            write(out, "plantsDestroyed", s.getPlantsDestroyed());
            write(out, "zombiesSummoned", s.getZombiesSummoned());
            write(out, "brainsSpent", s.getBrainsSpent());
            write(out, "bestVictoryTimeSeconds", s.getBestVictoryTimeSeconds());
            write(out, "lawnmowersActivated", s.getLawnmowersActivated());
        }
    }

    private void write(BufferedWriter out, String key, Object value) throws IOException {
        out.write(key + "=" + value);
        out.newLine();
    }

    public void recordCompleted(String nickname, GameEngine engine) throws IOException {
        if (!engine.isFinished() || engine.isRegisteredResult()) {
            return;
        }
        PlayerStats s = load(nickname);
        s.addGame(engine.isVictory(), engine.getPlantsDestroyed(), engine.getZombiesInvoked(), engine.getBrainsSpent(),
                engine.getMowersTriggered(), (int) Math.ceil(engine.getElapsedSeconds()));
        save(s);
        engine.markRegistered();
    }

    public PlayerStats[] loadAll() throws IOException {
        String[] names = accounts.findNicknames();
        PlayerStats[] all = new PlayerStats[names.length];
        for (int i = 0; i < names.length; i++) {
            all[i] = load(names[i]);
        }
        return all;
    }
}