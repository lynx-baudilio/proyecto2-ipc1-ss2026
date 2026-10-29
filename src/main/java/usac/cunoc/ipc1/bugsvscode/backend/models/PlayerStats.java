package usac.cunoc.ipc1.bugsvscode.backend.models;

/**
 * Estadísticas consolidadas de una cuenta, sin dependencias de UI.
 */
public final class PlayerStats {

    private final String nickname;
    private int gamesPlayed, gamesWon, gamesLost, plantsDestroyed, zombiesSummoned, brainsSpent, bestVictoryTimeSeconds = -1, lawnmowersActivated;

    public PlayerStats(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getPlantsDestroyed() {
        return plantsDestroyed;
    }

    public int getZombiesSummoned() {
        return zombiesSummoned;
    }

    public int getBrainsSpent() {
        return brainsSpent;
    }

    public int getBestVictoryTimeSeconds() {
        return bestVictoryTimeSeconds;
    }

    public int getLawnmowersActivated() {
        return lawnmowersActivated;
    }

    public double getWinRate() {
        return gamesPlayed == 0 ? 0 : 100.0 * gamesWon / gamesPlayed;
    }

    public double getAveragePlants() {
        return gamesPlayed == 0 ? 0 : (double) plantsDestroyed / gamesPlayed;
    }

    public void setValue(String name, int value) {
        switch (name) {
            case "gamesPlayed":
                gamesPlayed = value;
                break;
            case "gamesWon":
                gamesWon = value;
                break;
            case "gamesLost":
                gamesLost = value;
                break;
            case "plantsDestroyed":
                plantsDestroyed = value;
                break;
            case "zombiesSummoned":
                zombiesSummoned = value;
                break;
            case "brainsSpent":
                brainsSpent = value;
                break;
            case "bestVictoryTimeSeconds":
                bestVictoryTimeSeconds = value;
                break;
            case "lawnmowersActivated":
                lawnmowersActivated = value;
                break;
            default:
                throw new IllegalArgumentException("Clave no admitida: " + name);
        }
    }

    public void addGame(boolean victory, int plants, int summoned, int spent, int mowers, int elapsed) {
        gamesPlayed++;
        if (victory) {
            gamesWon++;
            if (bestVictoryTimeSeconds < 0 || elapsed < bestVictoryTimeSeconds) {
                bestVictoryTimeSeconds = elapsed;
            }
        } else {
            gamesLost++;
        }
        plantsDestroyed += plants;
        zombiesSummoned += summoned;
        brainsSpent += spent;
        lawnmowersActivated += mowers;
    }
}