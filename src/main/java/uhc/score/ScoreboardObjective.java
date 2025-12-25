package uhc.score;

import java.util.Objects;

/**
 * 🏆 **Scoreboard Objective Registry**
 * <p>
 * Defines the unique internal identifiers for all scoreboard objectives
 * used to track game state, player statistics, and technical variables
 * within the UHC environment.
 * </p>
 */
public enum ScoreboardObjective {

    // --- 🕒 Time & Match Tracking ---

    /** Technical dummy score used for internal time calculations. */
    TIME_DUM,
    /** The primary match timer displayed to players. */
    TIME,
    /** Secondary timer used for specific event durations (e.g., grace period). */
    TIME2,

    // --- 📊 UI & Display Management ---

    /** Technical objective used to manage sidebar display logic. */
    SIDE_DUM,
    /** Tracks player health; often rendered as heart icons in the tab list. */
    HEARTS,
    /** Tracks the number of golden apples consumed or crafted. */
    APPLES,

    // --- ⛏️ Mining & Resource Tracking ---

    /** Total count of stone blocks mined. */
    STONE,
    /** Total count of diorite blocks mined. */
    DIORITE,
    /** Total count of andesite blocks mined. */
    ANDESITE,
    /** Total count of granite blocks mined. */
    GRANITE,
    /** Total count of deepslate blocks mined. */
    DEEPSLATE,
    /** General mining progress or "Mining Mania" event score. */
    MINING,

    // --- ⚔️ Combat & Statistics ---

    /** Total deaths per player during the session. */
    DEATHS,
    /** Total player-on-player kills. */
    KILLS,
    /** Temporary kill counter for specific match phases. */
    TEMP_KILLS,
    /** Flag (0 or 1) indicating if the player was the last killer in a sequence. */
    IS_KILLER,
    /** Competitive rank or leaderboard position. */
    RANK,
    /** Tracks the lowest health reached by a player (for specific awards). */
    MIN_HEALTH,
    /** Tracks total damage received by the player. */
    DAMAGE_TAKEN,

    // --- 🚩 Control Point (CP) Logic ---

    /** The current score or capture progress for a control point. */
    CP_SCORE,
    /** The all-time high score for control point retention. */
    CP_HIGHSCORE,
    /** General identifier for control point status. */
    CONTROL_POINT,
    /** Flag (0 or 1) indicating if a player is currently standing on a CP. */
    ON_CP,
    /** Stores the ID of the previous control point occupied. */
    PREV_CP,
    /** Technical objective used for rendering CP info on the HUD. */
    DISPLAY_CP,
    /** Technical objective used to manage CP-specific team colors. */
    COLOR_CP,

    // --- 🛠️ Technical & Mechanic Helpers ---

    /** Tracks the age of a wolf for specific taming or growth mechanics. */
    WOLF_AGE,
    /** Technical check used for wolf collar color logic. */
    COLLAR_CHECK,
    /** Tracks how many times a specific event or ability was called. */
    TIMES_CALLED,
    /** Used to select and cycle through random flavor text in the UI. */
    RANDOM_QUOTES,
    /** Counter for how many victory points or wins a player has. */
    VICTORY,
    /** Tracks if a player has received their match perk. */
    RECEIVED_PERK,
    /** Flag indicating if a player has successfully found/formed a team. */
    FOUND_TEAM,
    /** General distance tracker (e.g., distance to center or border). */
    DISTANCE,
    /** Coordinate-based technical objective (usually for relative Y-level). */
    POS,
    /** Mathematical helper for calculating squared distances without square roots. */
    SQUARE;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase name used in the Minecraft objective registry.
     * <p><b>Example:</b> {@code ScoreboardObjective.TIME_DUM.getObjectiveName()} returns {@code "time_dum"}.</p>
     * @return The lowercase identifier.
     */
    public String getObjectiveName() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a ScoreboardObjective from its string name.
     * <p><b>Error Catching:</b> Handles case-insensitive matches. If the input is
     * null, blank, or not found in the registry, it returns {@code null} to
     * allow the caller to handle custom or dynamically created objectives.</p>
     * @param input The raw objective name (e.g., "Kills", "time_dum").
     * @return The matching {@link ScoreboardObjective}, or {@code null} if invalid.
     */
    public static ScoreboardObjective fromString(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        String target = input.toUpperCase().trim();
        try {
            return ScoreboardObjective.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped objectives from external data
            return null;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the objective name for direct use in scoreboard command strings.
     * <p><b>Implementation:</b> Delegates to {@link #getObjectiveName()}.</p>
     * @return The lowercase identifier.
     */
    @Override
    public String toString() {
        return getObjectiveName();
    }
}