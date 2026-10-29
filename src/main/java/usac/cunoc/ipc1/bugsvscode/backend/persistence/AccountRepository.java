package usac.cunoc.ipc1.bugsvscode.backend.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import usac.cunoc.ipc1.bugsvscode.backend.models.Account;
import usac.cunoc.ipc1.bugsvscode.backend.models.PlayerStats;

public class AccountRepository {

    private final File accountsFolder;

    public AccountRepository() {
        this(usac.cunoc.ipc1.bugsvscode.backend.config.AppPaths.playersDirectory());
    }

    // También permite indicar una carpeta temporal para las pruebas.
    public AccountRepository(File accountsFolder) {
        this.accountsFolder = accountsFolder;
    }

    public boolean exists(String nickname) {
        return getAccountFolder(nickname).exists();
    }

    public void save(Account account) throws IOException {
        File folder = getAccountFolder(account.getNickname());

        if (folder.exists()) {
            throw new IOException("Esta cuenta ya existe.");
        }
        if (!folder.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta del jugador.");
        }

        File savedGamesFolder = new File(folder, "saved-games");
        File statsFile = new File(folder, "statistics.txt");

        try {
            if (!savedGamesFolder.mkdir()) {
                throw new IOException("No se pudo crear la carpeta de partidas.");
            }
            // El formato TXT lo define un único repositorio.
            new StatisticsRepository(this).save(new PlayerStats(account.getNickname()));
        } catch (IOException exception) {
            // Si falla el registro, no dejamos una cuenta incompleta.
            statsFile.delete();
            savedGamesFolder.delete();
            folder.delete();
            throw exception;
        }
    }

    public Account load(String nickname) throws IOException {
        File file = new File(getAccountFolder(nickname), "statistics.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            String firstLine = reader.readLine();
            if (!("nickname=" + nickname).equals(firstLine)) {
                throw new IOException("El archivo de la cuenta está dañado: " + nickname);
            }
            readStatistic(reader, "gamesPlayed", false);
            readStatistic(reader, "gamesWon", false);
            readStatistic(reader, "gamesLost", false);
            readStatistic(reader, "plantsDestroyed", false);
            readStatistic(reader, "zombiesSummoned", false);
            readStatistic(reader, "brainsSpent", false);
            readStatistic(reader, "bestVictoryTimeSeconds", true);
            readStatistic(reader, "lawnmowersActivated", false);
            return new Account(nickname);
        }
    }

    private int readStatistic(BufferedReader reader, String key, boolean allowNegativeOne)
            throws IOException {
        String line = reader.readLine();
        if (line == null || !line.startsWith(key + "=")) {
            throw new IOException("Falta la estadística: " + key);
        }
        try {
            int value = Integer.parseInt(line.substring(key.length() + 1));
            if (value < 0 && !(allowNegativeOne && value == -1)) {
                throw new IOException("Estadística inválida: " + key);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IOException("Estadística no numérica: " + key, exception);
        }
    }

    public String[] findNicknames() throws IOException {
        if (!accountsFolder.exists()) {
            return new String[0];
        }

        File[] folders = accountsFolder.listFiles(File::isDirectory);
        if (folders == null) {
            throw new IOException("No se pudieron leer las cuentas guardadas.");
        }

        // Arreglos nativos: no usamos Java Collections Framework.
        String[] available = new String[folders.length];
        int count = 0;

        for (File folder : folders) {
            File file = new File(folder, "statistics.txt");
            if (!file.isFile()) {
                continue;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                if (line == null || !line.startsWith("nickname=")) {
                    continue;
                }
                String nickname = line.substring("nickname=".length());
                if (getAccountFolder(nickname).equals(folder)) {
                    available[count] = nickname;
                    count++;
                }
            }
        }

        String[] names = new String[count];
        for (int index = 0; index < count; index++) {
            names[index] = available[index];
        }
        sortAlphabetically(names);
        return names;
    }

    public File getAccountFolder(String nickname) {
        return new File(accountsFolder, folderName(nickname));
    }

    public static String folderName(String nickname) {
        StringBuilder name = new StringBuilder("player_");
        for (int index = 0; index < nickname.length(); index++) {
            int value = nickname.charAt(index);
            name.append(Character.forDigit(value / 16, 16));
            name.append(Character.forDigit(value % 16, 16));
        }
        return name.toString();
    }

    private void sortAlphabetically(String[] names) {
        for (int index = 1; index < names.length; index++) {
            String current = names[index];
            int previous = index - 1;
            while (previous >= 0 && names[previous].compareTo(current) > 0) {
                names[previous + 1] = names[previous];
                previous--;
            }
            names[previous + 1] = current;
        }
    }
}