package usac.cunoc.ipc1.bugsvscode.frontend.utils;

import javax.swing.SwingUtilities;
import usac.cunoc.ipc1.bugsvscode.backend.service.GameEngine;

/**
 * Un hilo sencillo para cada generador. Cada hilo duerme y avisa a Swing. El
 * estado del juego se modifica únicamente en el hilo de Swing (EDT). Todos los
 * cambios al juego ocurren en el hilo de Swing.
 */
public final class GeneratorWorkers {

    private final GameEngine engine;
    // Solo se consulta o reemplaza desde los eventos de Swing (EDT).
    private Thread[] workers;

    public GeneratorWorkers(GameEngine engine) {
        this.engine = engine;
    }

    public void start() {
        if (workers != null) {
            return;
        }
        workers = new Thread[engine.getGeneratorCount()];
        for (int i = 0; i < workers.length; i++) {
            final int id = i;
            Thread worker = new Thread(() -> runGenerator(id), "brain-generator-" + id);
            worker.setDaemon(true);
            workers[i] = worker;
            worker.start();
        }
    }

    private void runGenerator(int id) {
        // Identidad del hilo para descartar eventos pendientes de partidas pausadas.
        Thread thisWorker = Thread.currentThread();
        long previous = System.nanoTime();
        while (!thisWorker.isInterrupted()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                break;
            }
            long now = System.nanoTime();
            double delta = Math.min(.1, (now - previous) / 1_000_000_000.0);
            previous = now;
            SwingUtilities.invokeLater(() -> {
                if (workers != null && id < workers.length
                        && workers[id] == thisWorker && !engine.isFinished()) {
                    engine.generatorTick(id, delta);
                }
            });
        }
    }

    public void stop() {
        Thread[] old = workers;
        workers = null;
        if (old != null) {
            for (Thread worker : old) {
                worker.interrupt();
            }
        }
    }
}