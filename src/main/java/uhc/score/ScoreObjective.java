package uhc.score;

import java.util.Objects;

/**
 * 📊 **Score Objective Registry**
 * <p>
 * Defines type-safe keys for common Minecraft scoreboard objectives.
 * This registry ensures that objective names used in selectors (e.g., {@code scores={Kills=1..}})
 * are always spelled correctly and match the internal game registry.
 * </p>
 */
public enum ScoreObjective {

    // --- 🎮 Statistics Objectives ---

    /** * Tracks the total amount of damage a player has received.
     * <p><b>Internal Name:</b> {@code DamageTaken}</p>
     */
    DAMAGE_TAKEN("DamageTaken"),

    /** * Tracks the total number of times a player has died.
     * <p><b>Internal Name:</b> {@code Deaths}</p>
     */
    DEATHS("Deaths"),

    /** * Tracks the total number of player kills a player has achieved.
     * <p><b>Internal Name:</b> {@code Kills}</p>
     */
    KILLS("Kills"),

    /** * Represents the player's current numerical rank or leaderboard position.
     * <p><b>Internal Name:</b> {@code Rank}</p>
     */
    RANK("Rank"),

    // --- 🛠️ Technical Objectives ---

    /** * A utility counter used to track how many times a specific event or
     * function has been triggered for an entity.
     * <p><b>Internal Name:</b> {@code TimesCalled}</p>
     */
    TIMES_CALLED("TimesCalled");

    // --- ⚙️ Internal State ---

    /** The exact, case-sensitive string name of the objective as defined in-game. */
    private final String objectiveName;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for the score objective mapping.
     * <p><b>Error Catching:</b> Ensures the objective name is not null or blank
     * during initialization to prevent malformed command generation.</p>
     * @param objectiveName The literal string name used in Minecraft.
     */
    ScoreObjective(String objectiveName) {
        this.objectiveName = Objects.requireNonNull(objectiveName, "Objective name cannot be null.");

        if (objectiveName.isBlank()) {
            throw new IllegalArgumentException("Objective name cannot be empty for " + name());
        }
    }

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the exact string name of the objective as defined in-game.
     * <p><b>Example:</b> {@code ScoreObjective.DAMAGE_TAKEN.getObjectiveName()}
     * returns {@code "DamageTaken"}.</p>
     * @return The case-sensitive objective identifier.
     */
    public String getObjectiveName() {
        return objectiveName;
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a ScoreObjective from a raw string identifier.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup. If the input is
     * null or unrecognized, it returns {@code null} rather than throwing an
     * exception, allowing the caller to handle custom objectives gracefully.</p>
     * @param rawName The raw objective string (e.g., "kills" or "DamageTaken").
     * @return The matching {@link ScoreObjective}, or {@code null} if not found.
     */
    public static ScoreObjective fromString(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return null;
        }

        String target = rawName.trim();
        for (ScoreObjective objective : values()) {
            if (objective.objectiveName.equalsIgnoreCase(target)) {
                return objective;
            }
        }
        return null;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the exact string name of the objective, suitable for direct
     * insertion into Minecraft command selectors and NBT strings.
     * <p><b>Implementation:</b> Delegates to {@link #getObjectiveName()}.</p>
     * @return The literal objective name.
     */
    @Override
    public String toString() {
        return getObjectiveName();
    }
}