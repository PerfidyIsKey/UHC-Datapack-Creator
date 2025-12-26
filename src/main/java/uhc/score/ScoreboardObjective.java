package uhc.score;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🏆 **Scoreboard Objective Registry**
 * <p>
 * This interface acts as the central authority for all scoreboard objective identifiers.
 * It ensures that all objective names follow a strict <b>PascalCase</b> format
 * (e.g., {@code TIME_DUM} becomes {@code TimeDum}).
 * </p>
 * <p>
 * It supports both static singleton objectives and dynamic indexed versions
 * (e.g., {@code ControlPoint1}) through a type-safe implementation.
 * </p>
 */
public interface ScoreboardObjective {

    // --- 🕒 Time & Match Tracking ---

    /** Technical dummy score for internal timer calculations. */
    ScoreboardObjective TIME_DUM = Internal.TIME_DUM;
    /** Primary match timer displayed to players. */
    ScoreboardObjective TIME = Internal.TIME;
    /** Secondary event timer (e.g., grace periods). */
    ScoreboardObjective TIME2 = Internal.TIME2;

    // --- 📊 UI & Display Management ---

    /** Technical objective for sidebar logic. */
    ScoreboardObjective SIDE_DUM = Internal.SIDE_DUM;
    /** Health tracking (rendered as hearts in tab). */
    ScoreboardObjective HEARTS = Internal.HEARTS;
    /** Tracks golden apple consumption/crafting. */
    ScoreboardObjective APPLES = Internal.APPLES;

    // --- ⛏️ Mining & Resource Tracking ---

    /** Total stone blocks mined. */
    ScoreboardObjective STONE = Internal.STONE;
    /** Total diorite mined. */
    ScoreboardObjective DIORITE = Internal.DIORITE;
    /** Total andesite mined. */
    ScoreboardObjective ANDESITE = Internal.ANDESITE;
    /** Total granite mined. */
    ScoreboardObjective GRANITE = Internal.GRANITE;
    /** Total deepslate mined. */
    ScoreboardObjective DEEPSLATE = Internal.DEEPSLATE;
    /** General mining event progress. */
    ScoreboardObjective MINING = Internal.MINING;

    // --- ⚔️ Combat & Statistics ---

    /** Total player deaths. */
    ScoreboardObjective DEATHS = Internal.DEATHS;
    /** Total player-on-player kills. */
    ScoreboardObjective KILLS = Internal.KILLS;
    /** Temporary phase-specific kill counter. */
    ScoreboardObjective TEMP_KILLS = Internal.TEMP_KILLS;
    /** Flag (0/1) for identifying the last killer. */
    ScoreboardObjective IS_KILLER = Internal.IS_KILLER;
    /** Competitive rank position. */
    ScoreboardObjective RANK = Internal.RANK;
    /** Lowest health recorded for a player. */
    ScoreboardObjective MIN_HEALTH = Internal.MIN_HEALTH;
    /** Total damage received by the player. */
    ScoreboardObjective DAMAGE_TAKEN = Internal.DAMAGE_TAKEN;

    // --- 🚩 Control Point (CP) Logic ---

    /** Current capture progress score. */
    ScoreboardObjective CP_SCORE = Internal.CP_SCORE;
    /** Highscore for control point retention. */
    ScoreboardObjective CP_HIGHSCORE = Internal.CP_HIGHSCORE;
    /** General control point identifier. */
    ScoreboardObjective CONTROL_POINT = Internal.CONTROL_POINT;
    /** Flag (0/1) if player is on a capture zone. */
    ScoreboardObjective ON_CP = Internal.ON_CP;
    /** ID of the previous control point. */
    ScoreboardObjective PREV_CP = Internal.PREV_CP;
    /** Technical objective for CP HUD rendering. */
    ScoreboardObjective DISPLAY_CP = Internal.DISPLAY_CP;
    /** Technical objective for CP team coloring. */
    ScoreboardObjective COLOR_CP = Internal.COLOR_CP;

    // --- 🛠️ Technical & Mechanic Helpers ---

    /** Age of tamed wolves. */
    ScoreboardObjective WOLF_AGE = Internal.WOLF_AGE;
    /** Check for wolf collar color updates. */
    ScoreboardObjective COLLAR_CHECK = Internal.COLLAR_CHECK;
    /** Counter for ability/event calls. */
    ScoreboardObjective TIMES_CALLED = Internal.TIMES_CALLED;
    /** Selector for cycling UI flavor text. */
    ScoreboardObjective RANDOM_QUOTES = Internal.RANDOM_QUOTES;
    /** Counter for victory points or total wins. */
    ScoreboardObjective VICTORY = Internal.VICTORY;
    /** Flag for match perk distribution. */
    ScoreboardObjective RECEIVED_PERK = Internal.RECEIVED_PERK;
    /** Flag for team formation status. */
    ScoreboardObjective FOUND_TEAM = Internal.FOUND_TEAM;
    /** Distance to center or world border. */
    ScoreboardObjective DISTANCE = Internal.DISTANCE;
    /** Relative Y-level or coordinate marker. */
    ScoreboardObjective POS = Internal.POS;
    /** Mathematical helper for distance calculations. */
    ScoreboardObjective SQUARE = Internal.SQUARE;

    // --- 🛰️ Core Contract ---

    /**
     * Retrieves the formatted PascalCase identifier.
     * <p><b>Example:</b> {@code TIME_DUM} -> {@code "TimeDum"}.</p>
     * @return The objective string for the Minecraft registry.
     */
    String getObjectiveName();

    /**
     * Ensures the objective name is valid for game commands.
     * @throws IllegalStateException if the name is empty or exceeds the 16-character limit.
     */
    default void validate() throws IllegalStateException {
        String name = getObjectiveName();
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Scoreboard objective name cannot be null or empty.");
        }
        // Legacy 16-char limit is enforced for packet compatibility.
        if (name.length() > 16) {
            throw new IllegalStateException("Objective name '" + name + "' is too long (" + name.length() + " chars). Max is 16.");
        }
    }

    // --- 🛠️ Static Factory Methods ---

    /**
     * Creates a numbered version of a base objective.
     * <p>Used for multiple instances like {@code ControlPoint1}, {@code ControlPoint2}.</p>
     * @param base The base {@link ScoreboardObjective} to copy the name from.
     * @param index The index to append.
     * @return A new type-safe objective instance.
     * @throws NullPointerException if base is null.
     * @throws IllegalArgumentException if index is negative.
     */
    static ScoreboardObjective indexed(ScoreboardObjective base, int index) {
        Objects.requireNonNull(base, "Cannot index a null objective.");
        if (index < 0) {
            throw new IllegalArgumentException("Objective index cannot be negative: " + index);
        }
        return new IndexedObjective(base.getObjectiveName(), index);
    }

    /**
     * Formats a raw SNAKE_CASE string into PascalCase.
     * @param input The raw enum name.
     * @return The formatted string, or an empty string if input is null.
     */
    private static String formatPascal(String input) {
        if (input == null || input.isEmpty()) return "";
        try {
            return Arrays.stream(input.split("_"))
                    .filter(s -> !s.isEmpty())
                    .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
                    .collect(Collectors.joining());
        } catch (Exception e) {
            // Fail-safe: return the original input if complex parsing fails
            return input;
        }
    }

    // --- 📦 Internal Implementations ---

    /**
     * Singleton registry for fixed objectives.
     */
    enum Internal implements ScoreboardObjective {
        TIME_DUM, TIME, TIME2, SIDE_DUM, HEARTS, APPLES,
        STONE, DIORITE, ANDESITE, GRANITE, DEEPSLATE, MINING,
        DEATHS, KILLS, TEMP_KILLS, IS_KILLER, RANK, MIN_HEALTH, DAMAGE_TAKEN,
        CP_SCORE, CP_HIGHSCORE, CONTROL_POINT, ON_CP, PREV_CP, DISPLAY_CP, COLOR_CP,
        WOLF_AGE, COLLAR_CHECK, TIMES_CALLED, RANDOM_QUOTES, VICTORY,
        RECEIVED_PERK, FOUND_TEAM, DISTANCE, POS, SQUARE;

        private final String pascalName;

        Internal() {
            this.pascalName = ScoreboardObjective.formatPascal(this.name());
        }

        @Override public String getObjectiveName() { return pascalName; }
        @Override public String toString() { return getObjectiveName(); }
    }

    /**
     * Record representing a versioned objective.
     */
    record IndexedObjective(String basePascalName, int index) implements ScoreboardObjective {
        @Override public String getObjectiveName() { return basePascalName + index; }
        @Override public String toString() { return getObjectiveName(); }
    }
}