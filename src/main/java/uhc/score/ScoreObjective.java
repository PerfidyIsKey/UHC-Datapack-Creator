package uhc.score;

/**
 * Defines type-safe keys for common score objectives.
 * Ensures objective names used in selectors are always spelled correctly.
 */
public enum ScoreObjective {
    DAMAGE_TAKEN("DamageTaken"),
    DEATHS("Deaths"),
    KILLS("Kills"),
    RANK("Rank"),
    TIMES_CALLED("TimesCalled");

    private final String objectiveName;

    ScoreObjective(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    /**
     * Returns the exact string name of the objective as defined in-game.
     */
    @Override
    public String toString() {
        return objectiveName;
    }
}