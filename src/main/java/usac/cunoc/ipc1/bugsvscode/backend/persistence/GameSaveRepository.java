package usac.cunoc.ipc1.bugsvscode.backend.persistence;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import usac.cunoc.ipc1.bugsvscode.backend.service.GameEngine;

/**
 * Archivos binarios separados por usuario. Nunca guarda en el directorio src.
 */
public final class GameSaveRepository {

    private final File playersRoot;

    public GameSaveRepository() {
        this(usac.cunoc.ipc1.bugsvscode.backend.config.AppPaths.playersDirectory());
    }

    public GameSaveRepository(File playersRoot) {
        this.playersRoot = playersRoot;
    }

    public File getSaveFolder(String nickname) {
        return new File(new File(playersRoot, AccountRepository.folderName(nickname)), "saved-games");
    }

    public File save(String nickname, GameEngine engine) throws IOException {
        File folder = getSaveFolder(nickname);
        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new IOException("No se pudo crear carpeta de partidas.");
        }
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
        File target = new File(folder, "partida_" + timestamp + ".dat");
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(target)))) {
            out.writeObject(engine);
        }
        return target;
    }

    public String[] list(String nickname) {
        File[] files = getSaveFolder(nickname).listFiles((dir, name) -> name.startsWith("partida_") && name.endsWith(".dat"));
        if (files == null) {
            return new String[0];
        }
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }
        for (int i = 1; i < names.length; i++) {
            String cur = names[i];
            int j = i - 1;
            while (j >= 0 && names[j].compareTo(cur) < 0) {
                names[j + 1] = names[j];
                j--;
            }
            names[j + 1] = cur;
        }
        return names;
    }

    public GameEngine load(String nickname, String fileName) throws IOException {
        if (fileName == null || !fileName.matches("partida_[0-9_-]+\\.dat")) {
            throw new IOException("Nombre de partida inválido.");
        }
        File folder = getSaveFolder(nickname).getCanonicalFile();
        File file = new File(folder, fileName).getCanonicalFile();
        if (!file.getParentFile().equals(folder)) {
            throw new IOException("Ruta de partida no permitida.");
        }
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            Object value = in.readObject();
            if (!(value instanceof GameEngine)) {
                throw new IOException("Archivo de partida inválido.");
            }
            return (GameEngine) value;
        } catch (ClassNotFoundException | ClassCastException exception) {
            throw new IOException("Versión de partida incompatible.", exception);
        }
    }
}