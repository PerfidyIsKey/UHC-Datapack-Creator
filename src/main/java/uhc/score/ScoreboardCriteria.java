package uhc.score;

import java.util.Objects;

/**
 * 📊 **Scoreboard Criteria Registry**
 * <p>
 * Defines the underlying logic that dictates how an objective's score is updated.
 * Criteria range from manually managed "dummy" values to automatic game-tracked
 * statistics like health or kill counts.
 * </p>
 */
public enum ScoreboardCriteria {

    // --- 🛠️ Manual & State Criteria ---

    /**
     * **Manual Value (Dummy)**
     * <p>Scores are only changed by commands or external logic. Most common for UHC
     * timers, coin systems, and custom event tracking.</p>
     */
    DUMMY,

    /**
     * **Custom Statistics**
     * <p>Used for specialized Minecraft statistics (e.g., {@code custom:jump} or
     * {@code custom:time_since_death}).</p>
     */
    CUSTOM,

    // --- ❤️ Life & Death Criteria ---

    /**
     * **Health Tracker**
     * <p>Automatically tracks the player's health. Value ranges from 0 to 20
     * (for 10 hearts). Changes instantly as the player takes damage or heals.</p>
     */
    HEALTH,

    /**
     * **Death Tracker**
     * <p>Automatically increments by 1 every time a player dies.</p>
     */
    DEATH_COUNT,

    /**
     * **Kill Tracker (Players)**
     * <p>Automatically increments by 1 when the player kills another player.</p>
     */
    PLAYER_KILL_COUNT,

    // --- ⛏️ Interaction Criteria ---

    /**
     * **Block/Item Usage**
     * <p>Tracks how many times a specific item or block has been used.</p>
     * <p><b>Format:</b> Usually requires a suffix (e.g., {@code used:diamond_sword}).</p>
     */
    USED,

    /**
     * **Mining Tracker**
     * <p>Tracks how many times a specific block has been mined.</p>
     * <p><b>Format:</b> Usually requires a suffix (e.g., {@code mined:diamond_ore}).</p>
     */
    MINED;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase identifier used by Minecraft for this criteria.
     * <p><b>Note:</b> For {@code DEATH_COUNT} and {@code PLAYER_KILL_COUNT},
     * this returns the snake_case name used by the game engine.</p>
     * @return The criteria string (e.g., "dummy", "deathCount").
     */
    public String getCriteriaName() {
        if (this == DEATH_COUNT) return "deathCount";
        if (this == PLAYER_KILL_COUNT) return "playerKillCount";
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a ScoreboardCriteria from a raw string.
     * <p><b>Error Catching:</b> Handles case-insensitive matches and checks for
     * namespaced criteria (e.g., "minecraft.used" or "stat.mined"). If the input
     * is null or invalid, it defaults to {@link #DUMMY} to prevent objective
     * creation failure.</p>
     * @param input The raw criteria string (e.g., "health", "used:stone").
     * @return The matching {@link ScoreboardCriteria}, or {@link #DUMMY} as a fallback.
     */
    public static ScoreboardCriteria fromString(String input) {
        if (input == null || input.isBlank()) {
            return DUMMY;
        }

        String target = input.toLowerCase().trim();

        // Handle namespaced variants often found in NBT or older versions
        if (target.contains("used")) return USED;
        if (target.contains("mined")) return MINED;
        if (target.contains("custom")) return CUSTOM;

        try {
            // Check for specific camelCase overrides
            if (target.equals("deathcount")) return DEATH_COUNT;
            if (target.equals("playerkillcount")) return PLAYER_KILL_COUNT;

            return ScoreboardCriteria.valueOf(target.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped or complex stat strings
            return DUMMY;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the criteria name for direct use in {@code /scoreboard objectives add}.
     * @return The result of {@link #getCriteriaName()}.
     */
    @Override
    public String toString() {
        return getCriteriaName();
    }
}