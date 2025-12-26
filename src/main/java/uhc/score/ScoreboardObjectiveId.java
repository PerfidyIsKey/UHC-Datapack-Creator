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

    /** Tracks total game time in ticks. */
    ScoreboardObjectiveId GAME_TIME = Internal.GAME_TIME;
    /** Tracks elapsed real-world time. */
    ScoreboardObjectiveId REAL_TIME = Internal.REAL_TIME;

    // --- 📊 UI & Display Management ---

    /** Technical objective used for sidebar timing logic. */
    ScoreboardObjectiveId SIDEBAR_TIME = Internal.SIDEBAR_TIME;
    /** Standard health tracking for tab-list display. */
    ScoreboardObjectiveId HEARTS = Internal.HEARTS;
    /** Tracks consumption of golden apples. */
    ScoreboardObjectiveId APPLES = Internal.APPLES;

    // --- ⛏️ Mining & Resource Tracking ---

    /** Statistics for stone blocks mined. */
    ScoreboardObjectiveId STONE = Internal.STONE;
    /** Statistics for diorite blocks mined. */
    ScoreboardObjectiveId DIORITE = Internal.DIORITE;
    /** Statistics for andesite blocks mined. */
    ScoreboardObjectiveId ANDESITE = Internal.ANDESITE;
    /** Statistics for granite blocks mined. */
    ScoreboardObjectiveId GRANITE = Internal.GRANITE;
    /** Statistics for deepslate blocks mined. */
    ScoreboardObjectiveId DEEPSLATE = Internal.DEEPSLATE;
    /** Cumulative score for mining leaderboards. */
    ScoreboardObjectiveId MINING = Internal.MINING;

    // --- ⚔️ Combat & Statistics ---

    /** Tracks player death count. */
    ScoreboardObjectiveId DEATHS = Internal.DEATHS;
    /** Tracks player-on-player kills. */
    ScoreboardObjectiveId KILLS = Internal.KILLS;
    /** Temporary kill counter for specific game phases. */
    ScoreboardObjectiveId TEMP_KILLS = Internal.TEMP_KILLS;
    /** Boolean flag (0/1) identifying a killer in events. */
    ScoreboardObjectiveId IS_KILLER = Internal.IS_KILLER;
    /** Competitive rank or leaderboard position. */
    ScoreboardObjectiveId RANK = Internal.RANK;
    /** Records the lowest health reached by a player. */
    ScoreboardObjectiveId MIN_HEALTH = Internal.MIN_HEALTH;
    /** Tracks total damage received by entities. */
    ScoreboardObjectiveId DAMAGE_TAKEN = Internal.DAMAGE_TAKEN;

    // --- 🚩 Control Point (CP) Logic ---

    /** Current capture progress score. */
    ScoreboardObjectiveId CP_SCORE = Internal.CP_SCORE;
    /** Highest score reached during CP retention. */
    ScoreboardObjectiveId CP_HIGHSCORE = Internal.CP_HIGHSCORE;
    /** Generic identifier for control points. */
    ScoreboardObjectiveId CONTROL_POINT = Internal.CONTROL_POINT;
    /** Flag indicating if a player is within a CP zone. */
    ScoreboardObjectiveId ON_CP = Internal.ON_CP;
    /** Stores the ID of the previous control point visited. */
    ScoreboardObjectiveId PREV_CP = Internal.PREV_CP;
    /** Technical objective for CP HUD rendering. */
    ScoreboardObjectiveId DISPLAY_CP = Internal.DISPLAY_CP;
    /** Objective used for CP team coloring logic. */
    ScoreboardObjectiveId COLOR_CP = Internal.COLOR_CP;

    // --- 🛠️ Technical & Mechanic Helpers ---

    /** Tracks the age of tamed wolves. */
    ScoreboardObjectiveId WOLF_AGE = Internal.WOLF_AGE;
    /** Technical check for wolf collar updates. */
    ScoreboardObjectiveId COLLAR_CHECK = Internal.COLLAR_CHECK;
    /** General counter for ability or event activations. */
    ScoreboardObjectiveId TIMES_CALLED = Internal.TIMES_CALLED;
    /** Index selector for cycling flavor text/quotes. */
    ScoreboardObjectiveId RANDOM_QUOTES = Internal.RANDOM_QUOTES;
    /** Counter for victory points or match wins. */
    ScoreboardObjectiveId VICTORY = Internal.VICTORY;
    /** Flag indicating a player has received their perk. */
    ScoreboardObjectiveId RECEIVED_PERK = Internal.RECEIVED_PERK;
    /** Flag indicating a player has successfully joined a team. */
    ScoreboardObjectiveId FOUND_TEAM = Internal.FOUND_TEAM;
    /** Tracks physical distance to markers or center. */
    ScoreboardObjectiveId DISTANCE = Internal.DISTANCE;
    /** Base objective for Cartesian position tracking. */
    ScoreboardObjectiveId POS = Internal.POS;
    /** Base objective for entity velocity tracking. */
    ScoreboardObjectiveId VELOCITY = Internal.VELOCITY;
    /** Helper for distance-squared mathematical operations. */
    ScoreboardObjectiveId SQUARE = Internal.SQUARE;

    // --- 🛰️ Core Contract ---

    /**
     * Retrieves the formatted PascalCase identifier.
     * <p><b>Example:</b> {@code GAME_TIME} returns {@code "GameTime"}.</p>
     * @return The objective string used in Minecraft commands.
     */
    String getObjectiveName();

    /**
     * Validates that the objective name is compatible with Minecraft's engine.
     * <p>Checks for nullity, whitespace, and the legacy 16-character limit.</p>
     * @throws IllegalStateException if the name is invalid or too long.
     */
    default void validate() throws IllegalStateException {
        String name = getObjectiveName();
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Scoreboard objective name cannot be null or empty.");
        }
        if (name.length() > 16) {
            throw new IllegalStateException("Objective name '" + name + "' exceeds 16-character limit (" + name.length() + ").");
        }
    }

    // --- 🛠️ Static Factory Methods ---

    /**
     * Creates a custom objective identifier from a raw string.
     * <p>Automatically formats the input to PascalCase and validates the result.</p>
     * @param rawName The raw name (e.g., "capture_progress").
     * @return A validated {@link ScoreboardObjectiveId}.
     * @throws NullPointerException if rawName is null.
     */
    static ScoreboardObjectiveId custom(String rawName) {
        return new CustomObjective(Objects.requireNonNull(rawName, "Custom objective name cannot be null."));
    }

    /**
     * Creates a numbered version of a base objective.
     * <p>Example: {@code indexed(CONTROL_POINT, 1)} becomes {@code "ControlPoint1"}.</p>
     * @param base The base identifier.
     * @param index The index (must be non-negative).
     * @return A new validated {@link ScoreboardObjectiveId}.
     * @throws NullPointerException if base is null.
     * @throws IllegalArgumentException if index is negative.
     */
    static ScoreboardObjectiveId indexed(ScoreboardObjectiveId base, int index) {
        Objects.requireNonNull(base, "Cannot index a null objective.");
        if (index < 0) {
            throw new IllegalArgumentException("Objective index cannot be negative: " + index);
        }
        return new IndexedObjective(base.getObjectiveName(), index);
    }

    /**
     * Internal utility to convert Snake_Case or Space Case to PascalCase.
     * @param input The raw input string.
     * @return The formatted PascalCase string.
     */
    private static String formatPascal(String input) {
        if (input == null || input.isEmpty()) return "";
        try {
            return Arrays.stream(input.split("[_ ]"))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
                    .collect(Collectors.joining());
        } catch (Exception e) {
            return input; // Fallback to raw input if parsing fails
        }
    }

    // --- 📦 Internal Implementations ---

    /**
     * Internal enum representing the fixed, static registry of objectives.
     */
    enum Internal implements ScoreboardObjectiveId {
        GAME_TIME, REAL_TIME, SIDEBAR_TIME, HEARTS, APPLES,
        STONE, DIORITE, ANDESITE, GRANITE, DEEPSLATE, MINING,
        DEATHS, KILLS, TEMP_KILLS, IS_KILLER, RANK, MIN_HEALTH, DAMAGE_TAKEN,
        CP_SCORE, CP_HIGHSCORE, CONTROL_POINT, ON_CP, PREV_CP, DISPLAY_CP, COLOR_CP,
        WOLF_AGE, COLLAR_CHECK, TIMES_CALLED, RANDOM_QUOTES, VICTORY,
        RECEIVED_PERK, FOUND_TEAM, DISTANCE, POS, VELOCITY, SQUARE;

        private final String pascalName;

        Internal() {
            this.pascalName = ScoreboardObjectiveId.formatPascal(this.name());
        }

        @Override public String getObjectiveName() { return pascalName; }
        @Override public String toString() { return getObjectiveName(); }
    }

    /**
     * Represents a custom objective created at runtime.
     * @param rawName The raw string input to be formatted.
     */
    record CustomObjective(String rawName) implements ScoreboardObjectiveId {
        @Override public String getObjectiveName() { return ScoreboardObjectiveId.formatPascal(rawName); }
        @Override public String toString() { return getObjectiveName(); }
        public CustomObjective { this.validate(); } // Compact constructor for immediate validation
    }

    /**
     * Represents a versioned/indexed objective.
     * @param basePascalName The name of the parent objective.
     * @param index The unique version number.
     */
    record IndexedObjective(String basePascalName, int index) implements ScoreboardObjectiveId {
        @Override public String getObjectiveName() { return basePascalName + index; }
        @Override public String toString() { return getObjectiveName(); }
        public IndexedObjective { this.validate(); } // Compact constructor for immediate validation
    }
}