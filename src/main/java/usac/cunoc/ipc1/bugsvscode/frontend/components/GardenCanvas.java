package usac.cunoc.ipc1.bugsvscode.frontend.components;

import java.awt.*;
import javax.swing.JPanel;
import usac.cunoc.ipc1.bugsvscode.backend.models.*;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Plant;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombie;
import usac.cunoc.ipc1.bugsvscode.backend.service.GameEngine;
import usac.cunoc.ipc1.bugsvscode.frontend.utils.SpriteCatalog;

/**
 * Dibuja el tablero a partir del motor, sin modificar sus reglas.
 */
public final class GardenCanvas extends JPanel {

    private int cell;
    private int startX;
    private int startY;
    private int gridWidth;
    private int gridHeight;

    private final GameEngine engine;
    private final SpriteCatalog sprites;

    public GardenCanvas(GameEngine engine, SpriteCatalog sprites) {
        this.engine = engine;
        this.sprites = sprites;
        setOpaque(false);
    }

    /**
     * Convierte píxeles a coordenadas del modelo.
     */
    public boolean tryCollectBrain(int x, int y) {
        if (cell <= 0 || engine.isFinished()) {
            return false;
        }
        double modelX = (x - startX) / (double) cell;
        double modelY = (y - startY) / (double) cell;
        return engine.collectBrainAt(modelX, modelY);
    }

    public int getClickedRow(int x, int y) {
        if (cell <= 0 || x < startX - cell / 2 || x >= startX + gridWidth
                || y < startY || y >= startY + gridHeight) {
            return -1;
        }
        return (y - startY) / cell;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(35, 65, 42));
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            int rows = engine.getRows();
            int cols = engine.getCols();
            cell = Math.max(1, Math.min(Math.max(1, getWidth() - 95) / cols,
                    Math.max(1, getHeight() - 55) / rows));
            gridWidth = cell * cols;
            gridHeight = cell * rows;
            startX = Math.max(35, (getWidth() - gridWidth) / 2);
            startY = Math.max(18, (getHeight() - gridHeight) / 2);
            g.setFont(new Font("SansSerif", Font.BOLD, 11));
            g.setColor(Color.WHITE);
            g.drawString("ENTRADA", startX - 42, startY - 8);
            g.drawString("CASA", startX + gridWidth + 5, startY - 8);

            sprites.draw(g, "effects/house.png", startX + gridWidth + Math.max(8, cell / 2),
                    startY, Math.max(12, cell / 2), gridHeight);
            for (int row = 0; row < rows; row++) {
                int y = startY + row * cell;
                g.setColor(Color.WHITE);
                g.drawString("C" + (row + 1), startX - 28, y + cell / 2 + 5);
                for (int col = 0; col < cols; col++) {
                    int x = startX + col * cell;
                    g.setColor((row + col) % 2 == 0
                            ? new Color(100, 148, 78) : new Color(112, 160, 83));
                    g.fillRect(x, y, cell, cell);
                    g.setColor(new Color(44, 94, 53));
                    g.setStroke(new BasicStroke(1));
                    g.drawRect(x, y, cell, cell);
                    Plant plant = engine.getPlant(row, col);
                    if (plant != null) {
                        drawPlant(g, plant, x, y);
                    }
                }
                if (engine.hasMower(row) && sprites.draw(g, "effects/mower.png",
                        startX + gridWidth + 3, y + cell / 4, Math.max(10, cell / 2), Math.max(12, cell / 2))) {
                    continue;
                }
                g.setColor(engine.hasMower(row)
                        ? new Color(236, 191, 72) : new Color(75, 78, 70));
                g.fillRoundRect(startX + gridWidth + 5, y + cell / 4,
                        Math.max(8, cell / 3), Math.max(8, cell / 2), 5, 5);
                g.setColor(Color.BLACK);
                g.drawString("P", startX + gridWidth + 8, y + cell / 2 + 4);
            }
            // Podadoras activadas: avance visual a través de la fila.
            for (int row = 0; row < rows; row++) {
                MowerSweep sweep = engine.getMowerSweep(row);
                if (sweep == null) {
                    continue;
                }
                int mx = startX + (int) (sweep.getX() * cell);
                int my = startY + row * cell + cell / 4;
                int mw = Math.max(11, cell / 2);
                if (!sprites.draw(g, "effects/mower.png", mx, my, mw, mw)) {
                    g.setColor(new Color(236, 191, 72));
                    g.fillRoundRect(mx, my, mw, mw, 8, 8);
                    g.setColor(new Color(48, 60, 49));
                    g.drawRoundRect(mx, my, mw, mw, 8, 8);
                }
            }
            for (int i = 0; i < engine.getProjectileCount(); i++) {
                Projectile p = engine.getProjectile(i);
                int px = startX + (int) ((p.getX() + 0.5) * cell);
                int py = startY + p.getRow() * cell + cell / 2;
                if (!sprites.draw(g, p.isFrozen() ? "effects/ice-pea.png" : "effects/pea.png",
                        px - 7, py - 7, 14, 14)) {
                    g.setColor(p.isFrozen() ? new Color(85, 194, 241) : new Color(174, 234, 57));
                    g.fillOval(px - 5, py - 5, 10, 10);
                }
            }
            for (int i = 0; i < engine.getZombieCount(); i++) {
                Zombie z = engine.getZombie(i);
                int zx = startX + (int) ((z.getX() + 0.5) * cell);
                int zy = startY + z.getRow() * cell + cell / 2;
                if (z.isJumping()) {
                    zy -= (int) (Math.sin(z.getJumpProgress() * Math.PI) * cell * .36);
                }
                int size = Math.max(9, (int) (cell * 0.52));
                String state = z.getType().toLowerCase();
                if ("CARACONO".equals(z.getType()) && z.getShield() <= 0) {
                    state = "caracono-sin-cono";
                }
                if ("DEPORTISTA".equals(z.getType()) && z.getShield() <= 0) {
                    state = "deportista-sin-casco";
                }
                if ("GARROCHA".equals(z.getType()) && !z.hasPole()) {
                    state = "garrocha-sin-garrocha";
                }
                if ("ZOMBISTEIN".equals(z.getType()) && z.isBabyThrown()) {
                    state = "zombistein-sin-zombidito";
                }
                if ("BAILON".equals(z.getType()) && z.isDancing()) {
                    state = "bailon-bailando";
                }
                if ("NORMAL".equals(z.getType()) && z.getHealth() <= 50) {
                    state = "normal-sin-brazo";
                }
                if (sprites.draw(g, "zombies/" + state + ".png", zx - size / 2, zy - size / 2, size, size)) {
                    drawHealthBar(g, zx - size / 2, zy - size / 2 - 7, size, z.getHealth() / z.getMaxHealth());
                    continue;
                }
                g.setColor("CARACONO".equals(z.getType())
                        ? new Color(216, 126, 65) : new Color(110, 109, 151));
                g.fillOval(zx - size / 2, zy - size / 2, size, size);
                g.setColor(Color.BLACK);
                g.drawOval(zx - size / 2, zy - size / 2, size, size);
                if (z.isDancing()) {
                    g.setColor(new Color(255, 240, 108));
                    g.drawString("♫", zx, zy - size / 2);
                }
                if (z.getShield() > 0) {
                    g.setColor(new Color(255, 225, 81));
                    int[] xs = {zx - size / 3, zx + size / 3, zx};
                    int[] ys = {zy - size / 3, zy - size / 3, zy - size};
                    g.fillPolygon(xs, ys, 3);
                }
                // El zombi común (y el caracono sin cono) pierde el brazo al 50%.
                if (z.getHealth() > 50) {
                    g.setColor(new Color(208, 206, 169));
                    g.setStroke(new BasicStroke(3));
                    g.drawLine(zx + size / 4, zy, zx + size / 2, zy + size / 3);
                }
                drawHealthBar(g, zx - size / 2, zy - size / 2 - 7,
                        size, z.getHealth() / z.getMaxHealth());
            }
            // Animación breve de aplastamiento del Zombistein.
            for (int i = 0; i < engine.getCrushCount(); i++) {
                CrushMark mark = engine.getCrushMark(i);
                int px = startX + mark.getCol() * cell + cell / 2;
                int py = startY + mark.getRow() * cell + cell / 2;
                g.setColor(new Color(247, 79, 48));
                g.setFont(new Font("SansSerif", Font.BOLD, Math.max(10, cell / 5)));
                g.drawString("CRASH!", px - cell / 3, py);
            }
            // Última capa del jardín: los cerebros quedan por encima
            // de plantas, zombis y proyectiles, incluso si se superponen.
            for (int i = 0; i < engine.getPickupCount(); i++) {
                drawBrain(g, engine.getPickup(i));
            }
        } finally {
            g.dispose();
        }
    }

    /**
     * Ícono provisional: más adelante se cambia SOLO en el frontend.
     */
    private void drawBrain(Graphics2D g, BrainPickup brain) {
        int x = startX + (int) (brain.getX() * cell);
        int y = startY + (int) (brain.getY() * cell);
        int diameter = Math.max(12, (int) (cell * 0.42));
        int half = diameter / 2;
        if (sprites.draw(g, "effects/brain.png", x - half, y - half, diameter, diameter)) {
            return;
        }
        g.setColor(new Color(60, 29, 54, 190));
        g.fillOval(x - half - 2, y - half - 2, diameter + 4, diameter + 4);
        g.setColor(new Color(243, 131, 182));
        g.fillOval(x - half, y - half, diameter, diameter);
        g.setColor(new Color(170, 65, 118));
        g.setStroke(new BasicStroke(Math.max(1, cell / 38f)));
        g.drawArc(x - half + diameter / 6, y - half + diameter / 5,
                (int) (diameter * 0.65), (int) (diameter * 0.65), 45, 280);
        g.drawLine(x, y - half + 3, x, y + half - 3);
    }

    private void drawPlant(Graphics2D g, Plant plant, int x, int y) {
        String type = plant.getType();
        String state = type.toLowerCase();
        if ("NUEZ".equals(type)) {
            if (plant.getHealth() < plant.getMaxHealth() * .35) {
                state = "nuez-danada";
            } else if (plant.getHealth() <= plant.getMaxHealth() * .65) {
                state = "nuez-agrietada";
            }
        } else if ("PATATAPUM".equals(type)) {
            state = plant.isArmed() ? "patatapum-activa" : "patatapum-inactiva";
        } else if ("CARRONIVORA".equals(type) && plant.getDigestRemaining() > 0) {
            state = "carronivora-digiriendo";
        }
        int size = Math.max(7, (int) (cell * .62));
        if (sprites.draw(g, "plants/" + state + ".png", x + (cell - size) / 2, y + (cell - size) / 2, size, size)) {
            if (plant.getMaxHealth() > 0) {
                drawHealthBar(g, x + cell / 5, y + cell - 8, (int) (cell * .6), plant.getHealth() / plant.getMaxHealth());
            }
            return;
        }
        Color color;
        switch (type) {
            case "LANZAGUISANTES":
                color = new Color(39, 128, 59);
                break;
            case "NUEZ":
                color = new Color(164, 101, 54);
                break;
            case "PATATAPUM":
                color = plant.isArmed() ? new Color(245, 99, 47) : new Color(99, 76, 55);
                break;
            case "HIELAGUISANTES":
                color = new Color(57, 153, 199);
                break;
            case "CARRONIVORA":
                color = new Color(136, 60, 146);
                break;
            case "PINCHOHIERBA":
                color = new Color(62, 105, 68);
                break;
            default:
                color = new Color(59, 176, 134);
        }
        int cx = x + cell / 2;
        int cy = y + cell / 2;
        g.setColor(color);
        g.fillOval(cx - size / 2, cy - size / 2, size, size);
        g.setColor(new Color(24, 55, 28));
        g.drawOval(cx - size / 2, cy - size / 2, size, size);
        if (cell > 26) {
            String abbreviation = type.substring(0, Math.min(3, type.length()));
            g.setFont(new Font("SansSerif", Font.BOLD, Math.max(9, cell / 6)));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.WHITE);
            g.drawString(abbreviation, cx - fm.stringWidth(abbreviation) / 2,
                    cy + fm.getAscent() / 3);
        }
        if ("NUEZ".equals(type) && plant.getHealth() <= 400 * 0.65) {
            g.setColor(new Color(57, 38, 20));
            g.setStroke(new BasicStroke(2));
            g.drawLine(cx - size / 4, cy - size / 4, cx, cy + size / 5);
            if (plant.getHealth() < 400 * 0.35) {
                g.drawLine(cx, cy + size / 5, cx + size / 4, cy - size / 4);
                g.drawLine(cx - size / 4, cy + size / 4, cx + size / 5, cy + size / 4);
            }
        }
        if (plant.getMaxHealth() > 0) {
            drawHealthBar(g, x + cell / 5,
                    y + cell - 8, (int) (cell * 0.6),
                    plant.getHealth() / plant.getMaxHealth());
        }
    }

    private void drawHealthBar(Graphics2D g, int x, int y, int width, double fraction) {
        g.setColor(new Color(62, 31, 29));
        g.fillRect(x, y, width, 4);
        g.setColor(new Color(129, 229, 97));
        g.fillRect(x, y, (int) (width * Math.max(0, Math.min(1, fraction))), 4);
    }
}