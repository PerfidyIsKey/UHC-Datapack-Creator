package uhc.score;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🏆 **Scoreboard Objective Registry**
 * <p>
 * This interface acts as the central authority for all scoreboard objective identifiers.
 * It ensures that all objective names follow a strict <b>PascalCase</b> format
 * (e.g., {@code GAME_TIME} becomes {@code "GameTime"}).
 * </p>
 * <p>
 * It provides a type-safe way to reference objectives, supporting static singletons,
 * indexed versions (e.g., {@code CollarCheck0}), and custom runtime identifiers.
 * </p>
 */
public interface ScoreboardObjectiveId {

    // --- 🕒 Time & Match Tracking ---

    /** Tracks total game time in Minecraft ticks. */
    ScoreboardObjectiveId GAME_TIME = Internal.GAME_TIME;

    /** Tracks elapsed real-world time for match duration monitoring. */
    ScoreboardObjectiveId REAL_TIME = Internal.REAL_TIME;

    // --- 📊 UI & Display Management ---

    /** Technical objective used to drive sidebar timing animations and logic. */
    ScoreboardObjectiveId SIDEBAR_TIME = Internal.SIDEBAR_TIME;

    /** Standard health tracking for tab-list and nameplate displays. */
    ScoreboardObjectiveId HEARTS = Internal.HEARTS;

    /** Statistics tracking for the consumption of golden apples. */
    ScoreboardObjectiveId APPLES = Internal.APPLES;

    // --- ⛏️ Mining & Resource Tracking ---

    /** Total count of stone blocks mined by the player. */
    ScoreboardObjectiveId STONE = Internal.STONE;

    /** Total count of diorite blocks mined by the player. */
    ScoreboardObjectiveId DIORITE = Internal.DIORITE;

    /** Total count of andesite blocks mined by the player. */
    ScoreboardObjectiveId ANDESITE = Internal.ANDESITE;

    /** Total count of granite blocks mined by the player. */
    ScoreboardObjectiveId GRANITE = Internal.GRANITE;

    /** Total count of deepslate variants mined by the player. */
    ScoreboardObjectiveId DEEPSLATE = Internal.DEEPSLATE;

    /** Calculated cumulative score used for mining-specific leaderboards. */
    ScoreboardObjectiveId MINING = Internal.MINING;

    // --- ⚔️ Combat & Statistics ---

    /** The number of times a player has died. */
    ScoreboardObjectiveId DEATHS = Internal.DEATHS;

    /** Total player-on-player kills recorded. */
    ScoreboardObjectiveId KILLS = Internal.KILLS;

    /** Volatile kill counter used for specific game phases or bounties. */
    ScoreboardObjectiveId TEMP_KILLS = Internal.TEMP_KILLS;

    /** Binary flag (0 or 1) used to identify a killer entity during death events. */
    ScoreboardObjectiveId IS_KILLER = Internal.IS_KILLER;

    /** The player's current competitive rank or leaderboard index. */
    ScoreboardObjectiveId RANK = Internal.RANK;

    /** Record of the minimum health percentage reached by a player during the match. */
    ScoreboardObjectiveId MIN_HEALTH = Internal.MIN_HEALTH;

    /** Cumulative damage points received from all sources. */
    ScoreboardObjectiveId DAMAGE_TAKEN = Internal.DAMAGE_TAKEN;

    // --- 🚩 Control Point (CP) Logic ---

    /** The current numerical capture progress for an active objective. */
    ScoreboardObjectiveId CP_SCORE = Internal.CP_SCORE;

    /** The all-time high score reached during a single objective retention. */
    ScoreboardObjectiveId CP_HIGHSCORE = Internal.CP_HIGHSCORE;

    /** Base identifier for control point logic. */
    ScoreboardObjectiveId CONTROL_POINT = Internal.CONTROL_POINT;

    /** State flag indicating if a player is physically within a capture zone. */
    ScoreboardObjectiveId ON_CP = Internal.ON_CP;

    /** Stores the ID of the control point a player was previously interacting with. */
    ScoreboardObjectiveId PREV_CP = Internal.PREV_CP;

    /** Trigger objective used to update the CP status on the player's HUD. */
    ScoreboardObjectiveId DISPLAY_CP = Internal.DISPLAY_CP;

    /** Stores color-coding data for team-based control point ownership. */
    ScoreboardObjectiveId COLOR_CP = Internal.COLOR_CP;

    // --- 🛠️ Technical & Mechanic Helpers ---

    /** Used for managing growth and despawn timers of tamed wolves. */
    ScoreboardObjectiveId WOLF_AGE = Internal.WOLF_AGE;

    /** Internal ticker to verify and sync wolf collar colors. */
    ScoreboardObjectiveId COLLAR_CHECK = Internal.COLLAR_CHECK;

    /** General purpose counter for ability or script executions. */
    ScoreboardObjectiveId TIMES_CALLED = Internal.TIMES_CALLED;

    /** Randomized seed index used to select flavor text/quotes for the sidebar. */
    ScoreboardObjectiveId RANDOM_QUOTES = Internal.RANDOM_QUOTES;

    /** Counter for match victories or seasonal win points. */
    ScoreboardObjectiveId VICTORY = Internal.VICTORY;

    /** State flag confirming a player has successfully redeemed their starting perk. */
    ScoreboardObjectiveId RECEIVED_PERK = Internal.RECEIVED_PERK;

    /** State flag used during the automated team-sorting phase. */
    ScoreboardObjectiveId FOUND_TEAM = Internal.FOUND_TEAM;

    /** Calculated distance value to world-border or objective markers. */
    ScoreboardObjectiveId DISTANCE = Internal.DISTANCE;

    /** Base objective component for X/Y/Z coordinate tracking. */
    ScoreboardObjectiveId POS = Internal.POS;

    /** Base objective component for vector velocity calculations. */
    ScoreboardObjectiveId VELOCITY = Internal.VELOCITY;

    /** Helper objective used for squaring values in distance-squared algorithms. */
    ScoreboardObjectiveId SQUARE = Internal.SQUARE;

    // --- 🛰️ Core Contract ---

    /**
     * Retrieves the final formatted PascalCase string for use in Minecraft commands.
     * <p>Example: {@code CP_HIGHSCORE} -> {@code "CpHighscore"}.</p>
     * @return A non-null string containing the objective name.
     */
    String getObjectiveName();

    // --- 🛠️ Static Factory Methods ---

    /**
     * Instantiates a custom objective identifier from a raw string input.
     * <p>The input will be strictly formatted to PascalCase (removing underscores/spaces).</p>
     * @param rawName The unformatted name string; must not be null or blank.
     * @return A validated {@link ScoreboardObjectiveId} record.
     * @throws NullPointerException if the rawName is null.
     * @throws IllegalArgumentException if the rawName is empty or contains only whitespace.
     */
    static ScoreboardObjectiveId custom(String rawName) {
        Objects.requireNonNull(rawName, "Custom Objective Error: Raw name cannot be null.");
        if (rawName.isBlank()) {
            throw new IllegalArgumentException("Custom Objective Error: Name cannot be empty or blank.");
        }
        return new CustomObjective(rawName);
    }

    /**
     * Derives a numbered variant of an existing objective.
     * <p>Used for multi-objective logic like {@code Pos0}, {@code Pos1}, {@code Pos2}.</p>
     * @param base  The source identifier; must not be null.
     * @param index The version number; must be non-negative.
     * @return A new {@link ScoreboardObjectiveId} with the index appended.
     * @throws NullPointerException if the base objective is null.
     * @throws IllegalArgumentException if the index is negative.
     */
    static ScoreboardObjectiveId indexed(ScoreboardObjectiveId base, int index) {
        Objects.requireNonNull(base, "Indexing Error: The base objective cannot be null.");
        if (index < 0) {
            throw new IllegalArgumentException("Indexing Error: Objective index [" + index + "] cannot be negative.");
        }
        return new IndexedObjective(base.getObjectiveName(), index);
    }

    /**
     * Internal utility to transform raw strings into the Minecraft-standard PascalCase.
     * <p>Splits by underscores or spaces and capitalizes the first letter of each word.</p>
     * @param input The raw string to process.
     * @return The formatted PascalCase string.
     * @throws IllegalArgumentException if the resulting string is invalid or empty.
     */
    private static String formatPascal(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Formatting Error: Cannot generate PascalCase from null or blank input.");
        }

        try {
            String result = Arrays.stream(input.split("[_ ]"))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
                    .collect(Collectors.joining());

            if (result.isEmpty()) {
                throw new IllegalStateException("Formatting Error: Resulting PascalCase string for '" + input + "' is empty.");
            }
            return result;
        } catch (Exception e) {
            throw new IllegalStateException("Formatting Error: Critical failure while parsing '" + input + "'. Details: " + e.getMessage(), e);
        }
    }

    // --- 📦 Internal Implementations ---

    /**
     * Static registry of all core objectives used by the system.
     */
    enum Internal implements ScoreboardObjectiveId {
        GAME_TIME, REAL_TIME, SIDEBAR_TIME, HEARTS, APPLES,
        STONE, DIORITE, ANDESITE, GRANITE, DEEPSLATE, MINING,
        DEATHS, KILLS, TEMP_KILLS, IS_KILLER, RANK, MIN_HEALTH, DAMAGE_TAKEN,
        CP_SCORE, CP_HIGHSCORE, CONTROL_POINT, ON_CP, PREV_CP, DISPLAY_CP, COLOR_CP,
        WOLF_AGE, COLLAR_CHECK, TIMES_CALLED, RANDOM_QUOTES, VICTORY,
        RECEIVED_PERK, FOUND_TEAM, DISTANCE, POS, VELOCITY, SQUARE;

        /** Cached PascalCase name to avoid repeated string processing. */
        private final String pascalName;

        /**
         * Enum constructor that automatically triggers the PascalCase conversion.
         */
        Internal() {
            this.pascalName = ScoreboardObjectiveId.formatPascal(this.name());
        }

        @Override public String getObjectiveName() { return pascalName; }
        @Override public String toString() { return getObjectiveName(); }
    }

    /**
     * Record representing a custom runtime objective.
     * @param rawName The raw string to be formatted on access.
     */
    record CustomObjective(String rawName) implements ScoreboardObjectiveId {
        @Override public String getObjectiveName() { return ScoreboardObjectiveId.formatPascal(rawName); }
        @Override public String toString() { return getObjectiveName(); }

        public CustomObjective {
            Objects.requireNonNull(rawName, "Custom Record Error: Name cannot be null.");
        }
    }

    /**
     * Record representing an indexed version of a base objective.
     * @param basePascalName The name of the parent objective.
     * @param index The unique version number.
     */
    record IndexedObjective(String basePascalName, int index) implements ScoreboardObjectiveId {
        @Override public String getObjectiveName() { return basePascalName + index; }
        @Override public String toString() { return getObjectiveName(); }

        public IndexedObjective {
            Objects.requireNonNull(basePascalName, "Indexed Record Error: Base name cannot be null.");
        }
    }
}