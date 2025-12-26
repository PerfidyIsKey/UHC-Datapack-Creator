package uhc.score;

import uhc.resource.block.BlockId;
import uhc.resource.item.ItemId;
import uhc.text.TextComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 🗃️ **Scoreboard Objective Registry**
 * <p>
 * Centralized storage for all objective definitions used in the UHC.
 * This registry handles static constants, indexed collections, and
 * Cartesian coordinate systems for scoreboard tracking.
 * </p>
 */
public final class ScoreboardObjectives {

    // --- 🕒 Time & Match Tracking ---

    /** Tracks the elapsed game time in ticks. */
    public static final ScoreboardObjective GAME_TIME = new ScoreboardObjective(
            ScoreboardObjectiveId.GAME_TIME, ScoreboardCriteria.DUMMY);

    /** Tracks real-world time; displayed on the sidebar as "Elapsed Time". */
    public static final ScoreboardObjective REAL_TIME = new ScoreboardObjective(
            ScoreboardObjectiveId.REAL_TIME, ScoreboardCriteria.DUMMY)
            .displayName(TextComponent.text("Elapsed Time"))
            .displaySidebar(true);

    /** A dedicated timer specifically for sidebar display logic. */
    public static final ScoreboardObjective SIDEBAR_TIME = new ScoreboardObjective(
            ScoreboardObjectiveId.SIDEBAR_TIME, ScoreboardCriteria.DUMMY);

    // --- ⛏️ Resource & Mining Tracking ---

    /** Tracks the number of Golden Apples used/crafted. */
    public static final ScoreboardObjective APPLES = new ScoreboardObjective(
            ScoreboardObjectiveId.APPLES, ScoreboardCriteria.used(ItemId.GOLDEN_APPLE))
            .displayName(TextComponent.text("Golden apples"))
            .displaySidebar(true);

    /** Tracks stone blocks mined by the player. */
    public static final ScoreboardObjective STONE = new ScoreboardObjective(
            ScoreboardObjectiveId.STONE, ScoreboardCriteria.mined(BlockId.STONE));

    /** Tracks diorite blocks mined by the player. */
    public static final ScoreboardObjective DIORITE = new ScoreboardObjective(
            ScoreboardObjectiveId.DIORITE, ScoreboardCriteria.mined(BlockId.DIORITE));

    /** Tracks andesite blocks mined by the player. */
    public static final ScoreboardObjective ANDESITE = new ScoreboardObjective(
            ScoreboardObjectiveId.ANDESITE, ScoreboardCriteria.mined(BlockId.ANDESITE));

    /** Tracks granite blocks mined by the player. */
    public static final ScoreboardObjective GRANITE = new ScoreboardObjective(
            ScoreboardObjectiveId.GRANITE, ScoreboardCriteria.mined(BlockId.GRANITE));

    /** Tracks deepslate blocks mined by the player. */
    public static final ScoreboardObjective DEEPSLATE = new ScoreboardObjective(
            ScoreboardObjectiveId.DEEPSLATE, ScoreboardCriteria.mined(BlockId.DEEPSLATE));

    /** A leaderboard objective for mining statistics. */
    public static final ScoreboardObjective MINING = new ScoreboardObjective(
            ScoreboardObjectiveId.MINING, ScoreboardCriteria.DUMMY)
            .displayName(TextComponent.text("I like mining-leaderboard"))
            .displaySidebar(true);

    // --- ⚔️ Combat & Statistics ---

    /** Renders player health as hearts in the tab list. */
    public static final ScoreboardObjective HEARTS = new ScoreboardObjective(
            ScoreboardObjectiveId.HEARTS, ScoreboardCriteria.HEALTH);

    /** Tracks total player deaths. */
    public static final ScoreboardObjective DEATHS = new ScoreboardObjective(
            ScoreboardObjectiveId.DEATHS, ScoreboardCriteria.DEATH_COUNT);

    /** Tracks player kills; displayed on the sidebar by default. */
    public static final ScoreboardObjective KILLS = new ScoreboardObjective(
            ScoreboardObjectiveId.KILLS, ScoreboardCriteria.PLAYER_KILL_COUNT)
            .displaySidebar(true);

    /** Tracks current competitive rank. */
    public static final ScoreboardObjective RANK = new ScoreboardObjective(
            ScoreboardObjectiveId.RANK, ScoreboardCriteria.DUMMY);

    /** Tracks the minimum health reached by a player for specific challenges. */
    public static final ScoreboardObjective MIN_HEALTH = new ScoreboardObjective(
            ScoreboardObjectiveId.MIN_HEALTH, ScoreboardCriteria.DUMMY);

    /** Tracks total damage taken by the player. */
    public static final ScoreboardObjective DAMAGE_TAKEN = new ScoreboardObjective(
            ScoreboardObjectiveId.DAMAGE_TAKEN, ScoreboardCriteria.custom(CustomStatistics.DAMAGE_TAKEN));

    /** Tracks cumulative kills in a temporary phase. */
    public static final ScoreboardObjective TEMP_KILLS = new ScoreboardObjective(
            ScoreboardObjectiveId.TEMP_KILLS, ScoreboardCriteria.PLAYER_KILL_COUNT);

    /** Flag (0/1) used to identify the killer in events. */
    public static final ScoreboardObjective IS_KILLER = new ScoreboardObjective(
            ScoreboardObjectiveId.IS_KILLER, ScoreboardCriteria.DUMMY);

    // --- 🚩 Control Point (CP) Logic ---

    /** Tracks progress score for capturing control points. */
    public static final ScoreboardObjective CP_SCORE = new ScoreboardObjective(
            ScoreboardObjectiveId.CP_SCORE, ScoreboardCriteria.DUMMY)
            .displayName(TextComponent.text("Control Point score"));

    /** Tracks the highest score reached for control points. */
    public static final ScoreboardObjective CP_HIGHSCORE = new ScoreboardObjective(
            ScoreboardObjectiveId.CP_HIGHSCORE, ScoreboardCriteria.DUMMY);

    // --- 🛠️ Technical & Miscellaneous ---

    /** Tracks victory points or total wins. */
    public static final ScoreboardObjective VICTORY = new ScoreboardObjective(
            ScoreboardObjectiveId.VICTORY, ScoreboardCriteria.DUMMY);

    /** Tracks the age of tamed wolves. */
    public static final ScoreboardObjective WOLF_AGE = new ScoreboardObjective(
            ScoreboardObjectiveId.WOLF_AGE, ScoreboardCriteria.DUMMY);

    /** Stores index for cycling UI flavor text. */
    public static final ScoreboardObjective RANDOM_QUOTES = new ScoreboardObjective(
            ScoreboardObjectiveId.RANDOM_QUOTES, ScoreboardCriteria.DUMMY);

    /** Flag indicating if a player has received their match perk. */
    public static final ScoreboardObjective RECEIVED_PERK = new ScoreboardObjective(
            ScoreboardObjectiveId.RECEIVED_PERK, ScoreboardCriteria.DUMMY);

    /** Flag indicating if a player has joined a team. */
    public static final ScoreboardObjective FOUND_TEAM = new ScoreboardObjective(
            ScoreboardObjectiveId.FOUND_TEAM, ScoreboardCriteria.DUMMY);

    /** Tracks distance to specific markers or center. */
    public static final ScoreboardObjective DISTANCE = new ScoreboardObjective(
            ScoreboardObjectiveId.DISTANCE, ScoreboardCriteria.DUMMY);

    /** Tracks usage of the Goat Horn. */
    public static final ScoreboardObjective TIMES_CALLED = new ScoreboardObjective(
            ScoreboardObjectiveId.TIMES_CALLED, ScoreboardCriteria.used(ItemId.GOAT_HORN));

    // --- 🛰️ Automated Indexed Objectives ---

    /** Tracks wolf collar states (CollarCheck0 - CollarCheck3). */
    public static final List<ScoreboardObjective> COLLAR_CHECKS =
            generateIndexed(ScoreboardObjectiveId.COLLAR_CHECK, ScoreboardCriteria.DUMMY, 4);

    /** Tracks specific control point instances. */
    public static final List<ScoreboardObjective> CONTROL_POINT =
            generateIndexed(ScoreboardObjectiveId.CONTROL_POINT, ScoreboardCriteria.DUMMY, 2);

    /** Flag for players being on a specific CP zone. */
    public static final List<ScoreboardObjective> ON_CP =
            generateIndexed(ScoreboardObjectiveId.ON_CP, ScoreboardCriteria.DUMMY, 2);

    /** Stores the ID of the previous CP the player was at. */
    public static final List<ScoreboardObjective> PREV_CP =
            generateIndexed(ScoreboardObjectiveId.PREV_CP, ScoreboardCriteria.DUMMY, 2);

    /** Technical objectives for rendering CP info on HUD. */
    public static final List<ScoreboardObjective> DISPLAY_CP =
            generateIndexed(ScoreboardObjectiveId.DISPLAY_CP, ScoreboardCriteria.DUMMY, 2);

    /** Technical objectives for CP team coloring. */
    public static final List<ScoreboardObjective> COLOR_CP =
            generateIndexed(ScoreboardObjectiveId.COLOR_CP, ScoreboardCriteria.DUMMY, 2);

    /** Tracks spatial coordinates (PosX, PosY, PosZ). */
    public static final List<ScoreboardObjective> POSITION =
            generateCartesian(ScoreboardObjectiveId.POS, ScoreboardCriteria.DUMMY);

    /** Mathematical helper objectives (SquareX, SquareY, SquareZ). */
    public static final List<ScoreboardObjective> SQUARE =
            generateCartesian(ScoreboardObjectiveId.SQUARE, ScoreboardCriteria.DUMMY);

    // --- 📦 Master Collection ---

    /** * A complete array containing every objective defined in this registry.
     * Useful for mass-registration during initialization.
     */
    public static final ScoreboardObjective[] ALL;

    static {
        List<ScoreboardObjective> allList = new ArrayList<>();
        try {
            // Add static singletons
            Collections.addAll(allList,
                    GAME_TIME, REAL_TIME, SIDEBAR_TIME, HEARTS, APPLES, STONE, DIORITE,
                    ANDESITE, GRANITE, DEEPSLATE, MINING, DEATHS, KILLS, RANK, MIN_HEALTH,
                    VICTORY, WOLF_AGE, RANDOM_QUOTES, DAMAGE_TAKEN, CP_SCORE, CP_HIGHSCORE,
                    RECEIVED_PERK, TEMP_KILLS, IS_KILLER, FOUND_TEAM, DISTANCE, TIMES_CALLED
            );

            // Add automated lists
            allList.addAll(COLLAR_CHECKS);
            allList.addAll(CONTROL_POINT);
            allList.addAll(ON_CP);
            allList.addAll(PREV_CP);
            allList.addAll(DISPLAY_CP);
            allList.addAll(COLOR_CP);
            allList.addAll(POSITION);
            allList.addAll(SQUARE);
        } catch (Exception e) {
            System.err.println("❌ Critical Error: Failed to initialize Scoreboard Registry: " + e.getMessage());
        }
        ALL = allList.toArray(new ScoreboardObjective[0]);
    }

    // --- 🏗️ Factory Logic ---

    /**
     * Private constructor to prevent instantiation.
     */
    private ScoreboardObjectives() {}

    /**
     * Generates a list of objectives with numerical suffixes.
     * @param baseId   The base {@link ScoreboardObjectiveId}.
     * @param criteria The {@link ScoreboardCriteria} to use.
     * @param count    The number of versions to create.
     * @return An unmodifiable list of indexed objectives.
     */
    private static List<ScoreboardObjective> generateIndexed(ScoreboardObjectiveId baseId, ScoreboardCriteria criteria, int count) {
        Objects.requireNonNull(baseId);
        List<ScoreboardObjective> generated = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ScoreboardObjectiveId indexedId = ScoreboardObjectiveId.indexed(baseId, i);
            generated.add(new ScoreboardObjective(indexedId, criteria)
                    .displayName(TextComponent.text(indexedId.getObjectiveName())));
        }
        return Collections.unmodifiableList(generated);
    }

    /**
     * Generates a list of objectives suffixed with X, Y, and Z.
     * @param baseId   The base {@link ScoreboardObjectiveId}.
     * @param criteria The {@link ScoreboardCriteria} to use.
     * @return An unmodifiable list of 3 Cartesian objectives.
     */
    private static List<ScoreboardObjective> generateCartesian(ScoreboardObjectiveId baseId, ScoreboardCriteria criteria) {
        Objects.requireNonNull(baseId);
        List<ScoreboardObjective> axes = new ArrayList<>();
        String[] labels = {"X", "Y", "Z"};

        for (String axis : labels) {
            ScoreboardObjectiveId axisId = ScoreboardObjectiveId.custom(baseId.getObjectiveName() + axis);
            axes.add(new ScoreboardObjective(axisId, criteria)
                    .displayName(TextComponent.text(axisId.getObjectiveName()))
                    .displaySidebar(false));
        }
        return Collections.unmodifiableList(axes);
    }
}