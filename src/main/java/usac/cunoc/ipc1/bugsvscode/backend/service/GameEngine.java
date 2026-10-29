package usac.cunoc.ipc1.bugsvscode.backend.service;

import java.io.Serializable;
import java.util.Random;
import usac.cunoc.ipc1.bugsvscode.backend.models.*;
import usac.cunoc.ipc1.bugsvscode.backend.models.plants.Plant;
import usac.cunoc.ipc1.bugsvscode.backend.models.zombies.Zombie;

/**
 * Reglas deterministas por tick: nunca importa ni manipula Swing.
 */
public final class GameEngine implements Serializable {

    private static final long serialVersionUID = 3L;
    private final Plant[][] plants;
    private final boolean[] mowers;
    private MowerSweep[] mowerSweeps;
    private Zombie[] zombies = new Zombie[16];
    private CrushMark[] crushMarks = new CrushMark[8];
    private int crushCount;
    private Projectile[] projectiles = new Projectile[16];
    private BrainPickup[] pickups = new BrainPickup[24];
    private int zombieCount, projectileCount, pickupCount;
    private final BrainGenerator[] generators;
    private final Difficulty difficulty;
    private final Random random;
    private double remainingSeconds, elapsedSeconds, initialSeconds, aiClock;
    private int brains, brainsCollected, brainsSpent, zombiesInvoked, plantsDestroyed, mowersTriggered;
    private boolean finished, victory, registeredResult, threadedGenerators;

    public GameEngine(LevelConfig config) {
        this(config, Difficulty.EASY, new Random());
    }

    public GameEngine(LevelConfig config, Difficulty difficulty) {
        this(config, difficulty, new Random());
    }

    public GameEngine(LevelConfig config, Difficulty difficulty, Random random) {
        if (config == null || difficulty == null || random == null) {
            throw new IllegalArgumentException("Configuración inválida");
        }
        this.difficulty = difficulty;
        this.random = random;
        initialSeconds = config.getDuracionSegundos();
        remainingSeconds = initialSeconds;
        brains = config.getCerebrosIniciales();
        plants = new Plant[config.getFilas()][config.getColumnas()];
        mowers = new boolean[config.getFilas()];
        mowerSweeps = new MowerSweep[config.getFilas()];
        for (int r = 0; r < getRows(); r++) {
            mowers[r] = true;
            for (int c = 0; c < getCols(); c++) {
                String type = config.getPlanta(r, c);
                if (!"VACIO".equals(type)) {
                    plants[r][c] = PlantFactory.create(type);
                }
            }
        }
        generators = new BrainGenerator[difficulty.getGeneratorCount()];
        for (int i = 0; i < generators.length; i++) {
            generators[i] = new BrainGenerator(random);
        }
    }

    public int getRows() {
        return plants.length;
    }

    public int getCols() {
        return plants[0].length;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Plant getPlant(int row, int col) {
        return plants[row][col];
    }

    public boolean hasMower(int row) {
        return mowers[row];
    }

    public MowerSweep getMowerSweep(int row) {
        return mowerSweeps == null ? null : mowerSweeps[row];
    }

    public int getGeneratorCount() {
        return generators.length;
    }

    public void enableThreadedGenerators() {
        threadedGenerators = true;
    }

    /**
     * Recibe los pulsos de los hilos, siempre ejecutados en EDT.
     */
    public void generatorTick(int id, double delta) {
        if (!threadedGenerators || finished || id < 0 || id >= generators.length) {
            return;
        }
        if (generators[id].advance(delta)) {
            addPickup(new BrainPickup(random.nextDouble() * getCols(), random.nextDouble() * getRows(), id));
        }
    }

    public int getPickupCount() {
        return pickupCount;
    }

    public BrainPickup getPickup(int index) {
        return pickups[index];
    }

    public int getBrainsCollected() {
        return brainsCollected;
    }

    public int getBrainsSpent() {
        return brainsSpent;
    }

    public int getBrains() {
        return brains;
    }

    public double getRemainingSeconds() {
        return remainingSeconds;
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }

    public int getZombiesInvoked() {
        return zombiesInvoked;
    }

    public int getPlantsDestroyed() {
        return plantsDestroyed;
    }

    public int getMowersTriggered() {
        return mowersTriggered;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isVictory() {
        return victory;
    }

    public boolean isRegisteredResult() {
        return registeredResult;
    }

    public void markRegistered() {
        registeredResult = true;
    }

    public int getCrushCount() {
        return crushCount;
    }

    public CrushMark getCrushMark(int i) {
        return crushMarks[i];
    }

    public int getZombieCount() {
        return zombieCount;
    }

    public Zombie getZombie(int index) {
        return zombies[index];
    }

    public int getProjectileCount() {
        return projectileCount;
    }

    public Projectile getProjectile(int index) {
        return projectiles[index];
    }

    public int getBlock() {
        return Math.min(7, (int) (elapsedSeconds * 7 / initialSeconds) + 1);
    }

    public int getCost(String type) {
        int base = ZombieFactory.baseCost(type);
        if (base < 0) {
            return -1;
        }
        if (getBlock() == 4) {
            return (int) Math.round(base * 0.50);
        }
        if (getBlock() == 7) {
            return (int) Math.round(base * 0.25);
        }
        return base;
    }

    public boolean invoke(String type, int row) {
        int cost = getCost(type);
        if (finished || row < 0 || row >= getRows() || cost < 0 || brains < cost) {
            return false;
        }
        brains -= cost;
        brainsSpent += cost;
        addZombie(ZombieFactory.create(type, row));
        zombiesInvoked++;
        return true;
    }

    void addZombie(Zombie zombie) {
        if (zombieCount == zombies.length) {
            Zombie[] next = new Zombie[zombies.length * 2];
            System.arraycopy(zombies, 0, next, 0, zombieCount);
            zombies = next;
        }
        zombies[zombieCount++] = zombie;
    }

    void addProjectile(Projectile p) {
        if (projectileCount == projectiles.length) {
            Projectile[] next = new Projectile[projectiles.length * 2];
            System.arraycopy(projectiles, 0, next, 0, projectileCount);
            projectiles = next;
        }
        projectiles[projectileCount++] = p;
    }

    private void addPickup(BrainPickup p) {
        if (pickupCount == pickups.length) {
            BrainPickup[] next = new BrainPickup[pickups.length * 2];
            System.arraycopy(pickups, 0, next, 0, pickupCount);
            pickups = next;
        }
        pickups[pickupCount++] = p;
    }

    public boolean collectBrainAt(double x, double y) {
        if (finished) {
            return false;
        }
        for (int i = pickupCount - 1; i >= 0; i--) {
            BrainPickup p = pickups[i];
            double dx = p.getX() - x, dy = p.getY() - y;
            if (dx * dx + dy * dy <= .19 * .19) {
                brains += BrainPickup.VALUE;
                brainsCollected++;
                finishGenerator(p);
                removePickup(i);
                return true;
            }
        }
        return false;
    }

    private void finishGenerator(BrainPickup pickup) {
        if (pickup.getGeneratorId() >= 0) {
            generators[pickup.getGeneratorId()].finishCycle(random);
        }
    }

    private void removePickup(int index) {
        System.arraycopy(pickups, index + 1, pickups, index, pickupCount - index - 1);
        pickups[--pickupCount] = null;
    }

    private void updatePickups(double dt) {
        for (int i = pickupCount - 1; i >= 0; i--) {
            pickups[i].advance(dt);
            if (pickups[i].isExpired()) {
                finishGenerator(pickups[i]);
                removePickup(i);
            }
        }
        if (threadedGenerators) {
            return;
        }
        for (int id = 0; id < generators.length; id++) {
            if (generators[id].advance(dt)) {
                addPickup(new BrainPickup(random.nextDouble() * getCols(), random.nextDouble() * getRows(), id));
            }
        }
    }

    void destroyPlant(int row, int col) {
        if (plants[row][col] == null) {
            return;
        }
        plants[row][col] = null;
        plantsDestroyed++;
        int amount = 3 + random.nextInt(4);
        for (int i = 0; i < amount; i++) {
            addPickup(new BrainPickup(col + .2 + random.nextDouble() * .6,
                    row + .2 + random.nextDouble() * .6, -1));
        }
    }

    public void tick(double delta) {
        if (finished || delta <= 0) {
            return;
        }
        // El último pulso no debe contabilizar más tiempo que el restante.
        delta = Math.min(Math.min(delta, .10), remainingSeconds);
        remainingSeconds -= delta;
        elapsedSeconds += delta;
        if (remainingSeconds <= 0) {
            finished = true;
            victory = false;
            return;
        }
        updatePickups(delta);
        advanceCrushMarks(delta);
        advanceMowerSweeps(delta);
        PlantCombat.advancePlants(this, delta);
        PlantCombat.advanceProjectiles(this, delta);
        ZombieCombat.advanceZombies(this, delta);
        removeDead();
        advanceAI(delta);
    }

    void addCrushMark(int row, int col) {
        if (crushCount == crushMarks.length) {
            CrushMark[] next = new CrushMark[crushMarks.length * 2];
            System.arraycopy(crushMarks, 0, next, 0, crushCount);
            crushMarks = next;
        }
        crushMarks[crushCount++] = new CrushMark(row, col);
    }

    /**
     * La casa y las podadoras pertenecen al estado de esta partida.
     */
    void reachHouse(int row) {
        if (mowers[row]) {
            mowers[row] = false;
            mowersTriggered++;
            if (mowerSweeps == null) {
                mowerSweeps = new MowerSweep[getRows()];
            }
            mowerSweeps[row] = new MowerSweep(row, getCols());
            for (int i = 0; i < zombieCount; i++) {
                if (zombies[i].getRow() == row) {
                    zombies[i].kill();
                }
            }
        } else {
            victory = true;
            finished = true;
        }
    }

    private void advanceMowerSweeps(double dt) {
        if (mowerSweeps == null) {
            return; // Compatibilidad con partidas guardadas por versiones anteriores.
        }
        for (int row = 0; row < mowerSweeps.length; row++) {
            MowerSweep sweep = mowerSweeps[row];
            if (sweep != null) {
                sweep.advance(dt);
                if (!sweep.isActive()) {
                    mowerSweeps[row] = null;
                }
            }
        }
    }

    private void advanceCrushMarks(double dt) {
        for (int i = crushCount - 1; i >= 0; i--) {
            crushMarks[i].advance(dt);
            if (crushMarks[i].getRemaining() <= 0) {
                System.arraycopy(crushMarks, i + 1, crushMarks, i, crushCount - i - 1);
                crushMarks[--crushCount] = null;
            }
        }
    }

    private void removeDead() {
        int write = 0;
        for (int i = 0; i < zombieCount; i++) {
            if (zombies[i].isAlive()) {
                zombies[write++] = zombies[i];
            }
        }
        for (int i = write; i < zombieCount; i++) {
            zombies[i] = null;
        }
        zombieCount = write;
        write = 0;
        for (int i = 0; i < projectileCount; i++) {
            if (projectiles[i].isActive()) {
                projectiles[write++] = projectiles[i];
            }
        }
        for (int i = write; i < projectileCount; i++) {
            projectiles[i] = null;
        }
        projectileCount = write;
    }

    private void advanceAI(double delta) {
        if (difficulty.isEasy()) {
            return;
        }
        aiClock += delta;
        double interval = difficulty.isMedium() ? 10.0 : 5.0;
        if (aiClock < interval) {
            return;
        }
        aiClock -= interval;
        if (difficulty.isMedium()) {
            ComputerDefense.medium(plants, random);
        } else {
            ComputerDefense.hard(plants, zombies, zombieCount);
        }
    }
}