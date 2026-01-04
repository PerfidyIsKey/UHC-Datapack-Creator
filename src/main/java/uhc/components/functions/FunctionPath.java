package uhc.components.functions;

import uhc.components.FunctionName;
import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🔗 **Type-Safe Minecraft Function Paths**
 * <p>
 * Maps Java constants to {@code .mcfunction} paths within the datapack. This interface
 * supports both static enum constants and dynamically generated indexed paths
 * (e.g., {@code TEAM_RANDOM} with index {@code 1} becomes {@code "startup/teams/team_random_1"}).
 * </p>
 * <p>
 * <b>Strict Policy:</b> This implementation uses a "fail-fast" approach. Any null
 * parameters or invalid states will trigger immediate exceptions to prevent
 * generating a corrupted datapack.
 * </p>
 */
public interface FunctionPath extends FunctionName {

    // =================================================================================
    // 1. CORE / MAIN GAME FLOW
    // =================================================================================

    /** Reset the entire game state. */
    FunctionPath RESET = Internal.RESET;
    /** The primary tick loop for game logic. */
    FunctionPath MAIN_LOOP = Internal.MAIN_LOOP;

    // =================================================================================
    // 2. INITIALIZATION ('init' folder)
    // =================================================================================

    /** Standard Minecraft load function. */
    FunctionPath LOAD = Internal.LOAD;
    /** Game-specific internal initialization. */
    FunctionPath INITIALIZE = Internal.INITIALIZE;
    /** Setup for scoreboards and storage. */
    FunctionPath INITIALIZATION = Internal.INITIALIZATION;

    // =================================================================================
    // 3. GAME STARTUP PROCEDURE ('startup' folder)
    // =================================================================================

    /** Triggers the start of the match. */
    FunctionPath START_GAME = Internal.START_GAME;
    /** Teleports players to random locations. */
    FunctionPath SPREAD_PLAYERS = Internal.SPREAD_PLAYERS;
    /** Logic for counting down the game start. */
    FunctionPath GAME_STARTER = Internal.GAME_STARTER;
    /** Sets all players to survival mode. */
    FunctionPath SURVIVAL_MODE = Internal.SURVIVAL_MODE;
    /** Logic for initial voice or proximity calls. */
    FunctionPath INTO_CALLS = Internal.INTO_CALLS;
    /** Distributes starting potions. */
    FunctionPath START_POTIONS = Internal.START_POTIONS;
    /** Handles match result predictions. */
    FunctionPath PREDICTIONS = Internal.PREDICTIONS;
    /** Background loop for prediction processing. */
    FunctionPath PREDICTIONS_LOOP = Internal.PREDICTIONS_LOOP;
    /** Base for team assignments; use {@link #indexed(FunctionPath, int)}. */
    FunctionPath TEAM_RANDOM = Internal.TEAM_RANDOM;

    // Developer Tools
    /** Enables debugging mode. */
    FunctionPath DEVELOPER_MODE = Internal.DEVELOPER_MODE;
    /** Manual control for potion distribution. */
    FunctionPath DEVELOPER_POTION_CONTROL = Internal.DEVELOPER_POTION_CONTROL;
    /** 1-second timer for developer tasks. */
    FunctionPath TIMER_DEVELOPER_20 = Internal.TIMER_DEVELOPER_20;

    // =================================================================================
    // 4. DEATH HANDLER ('death_handler' folder)
    // =================================================================================

    /** Primary logic for player elimination. */
    FunctionPath HANDLE_PLAYER_DEATH = Internal.HANDLE_PLAYER_DEATH;
    /** Logic for player respawning (if enabled). */
    FunctionPath RESPAWN_PLAYER = Internal.RESPAWN_PLAYER;
    /** Prevents players from respawning in-game. */
    FunctionPath DISABLE_RESPAWN = Internal.DISABLE_RESPAWN;
    /** Drops player heads upon death. */
    FunctionPath DROP_PLAYER_HEADS = Internal.DROP_PLAYER_HEADS;

    // =================================================================================
    // 5. GAME END LOGIC ('game_end' folder)
    // =================================================================================

    /** Prepares the world for deathmatch. */
    FunctionPath INITIATE_DEATHMATCH = Internal.INITIATE_DEATHMATCH;
    /** Final phase combat logic. */
    FunctionPath DEATH_MATCH = Internal.DEATH_MATCH;
    /** Checks for the last team standing. */
    FunctionPath TEAMS_ALIVE_CHECK = Internal.TEAMS_ALIVE_CHECK;
    /** Highscore-based victory check. */
    FunctionPath TEAMS_HIGHSCORE_ALIVE_CHECK = Internal.TEAMS_HIGHSCORE_ALIVE_CHECK;

    // =================================================================================
    // 6. MAIN TIMERS / SCHEDULING ('timer' folder)
    // =================================================================================

    /** 20-tick (1 second) main timer. */
    FunctionPath TIMER_MAIN_1 = Internal.TIMER_MAIN_1;
    /** 5-second interval timer. */
    FunctionPath TIMER_MAIN_5 = Internal.TIMER_MAIN_5;
    /** 20-second interval timer. */
    FunctionPath TIMER_MAIN_20 = Internal.TIMER_MAIN_20;
    /** Cancels all active schedules. */
    FunctionPath CLEAR_SCHEDULE = Internal.CLEAR_SCHEDULE;

    // =================================================================================
    // 7. CONTROL POINT / PERKS ('control_point' folder)
    // =================================================================================

    /** Setup for CP structures. */
    FunctionPath INITIALIZE_CONTROL_POINT = Internal.INITIALIZE_CONTROL_POINT;
    /** Spawns CP entities into the world. */
    FunctionPath SPAWN_CONTROL_POINTS = Internal.SPAWN_CONTROL_POINTS;
    /** 1-second timer for CP capture logic. */
    FunctionPath TIMER_CONTROL_POINT_20 = Internal.TIMER_CONTROL_POINT_20;
    /** Logic triggered upon full capture. */
    FunctionPath CONTROL_POINT_CAPTURED = Internal.CONTROL_POINT_CAPTURED;
    /** Validates player status for perks. */
    FunctionPath CONTROL_POINT_PERKS_CHECK = Internal.CONTROL_POINT_PERKS_CHECK;
    /** Spawns the secondary control point. */
    FunctionPath SECOND_CONTROL_POINT = Internal.SECOND_CONTROL_POINT;
    /** Updates team scores based on CP ownership. */
    FunctionPath CONTROL_POINT_TEAM_SCORE = Internal.CONTROL_POINT_TEAM_SCORE;

    /** Base path for CP logic; use {@link #indexed(FunctionPath, int)}. */
    FunctionPath CONTROL_POINT = Internal.CONTROL_POINT;
    /** Base path for CP scoring. */
    FunctionPath CONTROL_POINT_SCORE = Internal.CONTROL_POINT_SCORE;
    /** Base path for record updates. */
    FunctionPath CONTROL_POINT_UPDATE_RECORDS = Internal.CONTROL_POINT_UPDATE_RECORDS;
    /** Base path for CP particles/visuals. */
    FunctionPath CONTROL_POINT_VISUALS = Internal.CONTROL_POINT_VISUALS;
    /** Base path for CP chat notifications. */
    FunctionPath CONTROL_POINT_MESSAGES = Internal.CONTROL_POINT_MESSAGES;
    /** Base path for beacon protection. */
    FunctionPath PROTECT_BEACON = Internal.PROTECT_BEACON;
    /** Base path for perks; use {@link #indexed(FunctionPath, int)}. */
    FunctionPath PERK = Internal.PERK;

    // =================================================================================
    // 8. TRAITOR FACTION ('traitor' folder)
    // =================================================================================

    /** Checks for traitor win/loss conditions. */
    FunctionPath TRAITOR_CHECK = Internal.TRAITOR_CHECK;
    /** Distributes traitor items. */
    FunctionPath TRAITOR_HANDOUT = Internal.TRAITOR_HANDOUT;
    /** Traitor-specific UI updates. */
    FunctionPath TRAITOR_ACTIONBAR = Internal.TRAITOR_ACTIONBAR;
    /** 5-second traitor interval. */
    FunctionPath TIMER_TRAITOR_5 = Internal.TIMER_TRAITOR_5;
    /** 20-second traitor interval. */
    FunctionPath TIMER_TRAITOR_20 = Internal.TIMER_TRAITOR_20;

    // =================================================================================
    // 9. UTILITY / MISC ('util' folder)
    // =================================================================================

    /** Refresh the sidebar UI. */
    FunctionPath UPDATE_SIDEBAR = Internal.UPDATE_SIDEBAR;
    /** Tracks lowest player health. */
    FunctionPath UPDATE_MIN_HEALTH = Internal.UPDATE_MIN_HEALTH;
    /** Updates block mining statistics. */
    FunctionPath UPDATE_MINE_COUNT = Internal.UPDATE_MINE_COUNT;
    /** Empties enderchests for the match. */
    FunctionPath CLEAR_ENDERCHEST = Internal.CLEAR_ENDERCHEST;
    /** Highlights teammate locations. */
    FunctionPath LOCATE_TEAMMATE = Internal.LOCATE_TEAMMATE;
    /** Special ability for horses. */
    FunctionPath HORSE_FROST_WALKER = Internal.HORSE_FROST_WALKER;
    /** Deletes forbidden items from inventories. */
    FunctionPath REMOVE_BANNED_ITEMS = Internal.REMOVE_BANNED_ITEMS;
    /** Updates wolf age and collar status. */
    FunctionPath WOLF_UPDATES = Internal.WOLF_UPDATES;

    // =================================================================================
    // 10. CARE PACKAGES ('care_packages' folder)
    // =================================================================================

    /** Triggers care package drops. */
    FunctionPath DROP_CAREPACKAGES = Internal.DROP_CAREPACKAGES;

    // =================================================================================
    // 11. DISPLAY / MESSAGES ('display' folder)
    // =================================================================================

    /** Shows current ranks. */
    FunctionPath DISPLAY_RANK = Internal.DISPLAY_RANK;
    /** Cycles through sidebar quotes. */
    FunctionPath DISPLAY_QUOTES = Internal.DISPLAY_QUOTES;
    /** Declares the Iron Man winner. */
    FunctionPath ANNOUNCE_IRON_MAN = Internal.ANNOUNCE_IRON_MAN;
    /** Checks health for Iron Man eligibility. */
    FunctionPath CHECK_IRON_MAN = Internal.CHECK_IRON_MAN;
    /** Notifies players of PvP status. */
    FunctionPath MESSAGES_PVP = Internal.MESSAGES_PVP;
    /** Notifies players of eternal day status. */
    FunctionPath MESSAGES_ETERNAL_DAY = Internal.MESSAGES_ETERNAL_DAY;
    /** Generic scheduling notification. */
    FunctionPath MESSAGES_SCHEDULE_SINGLE = Internal.MESSAGES_SCHEDULE_SINGLE;
    /** Base for victory logic. */
    FunctionPath VICTORY = Internal.VICTORY;
    /** Traitor-specific win message. */
    FunctionPath VICTORY_MESSAGE_TRAITOR = Internal.VICTORY_MESSAGE_TRAITOR;
    /** Base for numbered victory messages. */
    FunctionPath VICTORY_MESSAGE = Internal.VICTORY_MESSAGE;
    /** Base for minute timers. */
    FunctionPath TIMER_MINUTE = Internal.TIMER_MINUTE;

    // =================================================================================
    // 12. FUN COMMANDS ('fun' folder)
    // =================================================================================

    /** Enables shrink-zone logic. */
    FunctionPath BATTLE_ROYALE = Internal.BATTLE_ROYALE;
    /** Grants experimental equipment. */
    FunctionPath EQUIP_GEAR = Internal.EQUIP_GEAR;
    /** Prevents all damage. */
    FunctionPath GOD_MODE = Internal.GOD_MODE;

    // --- ⚙️ Core Contract ---

    /**
     * Retrieves the relative path within the namespace (e.g., "init/initialize").
     * @return The non-null path string.
     */
    String getPath();

    /**
     * Retrieves the folder location prefix for the function.
     * @return The non-null folder path ending with a slash, or empty string if root.
     */
    String getPathPrefix();

    // --- 🛠️ Static Factory Methods ---

    /**
     * Creates an indexed version of an existing FunctionPath.
     * <p>Example: {@code indexed(TEAM_RANDOM, 1)} becomes {@code "startup/teams/team_random_1"}.</p>
     * @param base  The base function identifier; must not be null.
     * @param index The index number to append; must be non-negative.
     * @return A non-null {@link FunctionPath} representing the indexed file.
     * @throws NullPointerException if base is null.
     * @throws IllegalArgumentException if index is negative.
     */
    static FunctionPath indexed(FunctionPath base, int index) {
        Objects.requireNonNull(base, "Indexing Error: The base function path cannot be null.");
        if (index < 0) {
            throw new IllegalArgumentException("Indexing Error: Function index [" + index + "] cannot be negative.");
        }
        return new IndexedFunction(base, index);
    }

    // --- 📦 Internal Implementations ---

    /**
     * Internal enum containing the static definitions and their folder mapping.
     */
    enum Internal implements FunctionPath {
        RESET, MAIN_LOOP,
        LOAD("init"), INITIALIZE("init"), INITIALIZATION("init"),
        START_GAME("startup"), SPREAD_PLAYERS("startup"), GAME_STARTER("startup"),
        SURVIVAL_MODE("startup"), INTO_CALLS("startup"), START_POTIONS("startup"),
        PREDICTIONS("startup"), PREDICTIONS_LOOP("startup"),
        TEAM_RANDOM("startup/teams"),
        DEVELOPER_MODE("startup/developer"), DEVELOPER_POTION_CONTROL("startup/developer"), TIMER_DEVELOPER_20("startup/developer"),
        HANDLE_PLAYER_DEATH("death_handler"), RESPAWN_PLAYER("death_handler"), DISABLE_RESPAWN("death_handler"), DROP_PLAYER_HEADS("death_handler"),
        INITIATE_DEATHMATCH("game_end"), DEATH_MATCH("game_end"), TEAMS_ALIVE_CHECK("game_end"), TEAMS_HIGHSCORE_ALIVE_CHECK("game_end"),
        TIMER_MAIN_1("timer"), TIMER_MAIN_5("timer"), TIMER_MAIN_20("timer"), CLEAR_SCHEDULE("timer"),
        INITIALIZE_CONTROL_POINT("control_point"), SPAWN_CONTROL_POINTS("control_point"), TIMER_CONTROL_POINT_20("control_point"),
        CONTROL_POINT_CAPTURED("control_point"), CONTROL_POINT_PERKS_CHECK("control_point"), SECOND_CONTROL_POINT("control_point/spawn"),
        CONTROL_POINT("control_point"), CONTROL_POINT_SCORE("control_point/score"), CONTROL_POINT_TEAM_SCORE("control_point/score"),
        CONTROL_POINT_UPDATE_RECORDS("control_point/score"), CONTROL_POINT_VISUALS("control_point/visuals"),
        CONTROL_POINT_MESSAGES("control_point/messages"), PROTECT_BEACON("control_point/visuals"), PERK("control_point/perks"),
        TRAITOR_CHECK("traitor"), TRAITOR_HANDOUT("traitor"), TRAITOR_ACTIONBAR("traitor"), TIMER_TRAITOR_5("traitor"), TIMER_TRAITOR_20("traitor"),
        UPDATE_SIDEBAR("util"), UPDATE_MIN_HEALTH("util"), UPDATE_MINE_COUNT("util"), CLEAR_ENDERCHEST("util"),
        LOCATE_TEAMMATE("util"), HORSE_FROST_WALKER("util"), REMOVE_BANNED_ITEMS("util"), WOLF_UPDATES("util"),
        DROP_CAREPACKAGES("care_packages"),
        DISPLAY_RANK("display"), DISPLAY_QUOTES("display"), ANNOUNCE_IRON_MAN("display"), CHECK_IRON_MAN("display"),
        MESSAGES_PVP("display"), MESSAGES_ETERNAL_DAY("display"), MESSAGES_SCHEDULE_SINGLE("display"),
        VICTORY("display/victory"), VICTORY_MESSAGE_TRAITOR("display/victory"), VICTORY_MESSAGE("display/victory"),
        TIMER_MINUTE("display/timer"),
        BATTLE_ROYALE("fun"), EQUIP_GEAR("fun"), GOD_MODE("fun");

        /** The cached folder path with a trailing slash. */
        private final String pathPrefix;

        /** Root folder constructor. */
        Internal() { this.pathPrefix = ""; }

        /**
         * Folder-specific constructor.
         * @param folder The folder path; will be normalized with a trailing slash.
         */
        Internal(String folder) {
            Objects.requireNonNull(folder, "Internal Error: Folder path cannot be null.");
            this.pathPrefix = folder.endsWith("/") ? folder : folder + "/";
        }

        @Override public String getPath() { return pathPrefix + this.name().toLowerCase(); }
        @Override public String getPathPrefix() { return pathPrefix; }
        @Override public String getIdentifier() { return DatapackConfig.CUSTOM_NAMESPACE + ":" + getPath(); }
        @Override public String toString() { return getIdentifier(); }
    }

    /**
     * Record representing a function file with a numerical suffix.
     * @param base  The parent FunctionPath definition; must not be null.
     * @param index The specific index of this function.
     */
    record IndexedFunction(FunctionPath base, int index) implements FunctionPath {

        @Override
        public String getPath() {
            try {
                // Reconstructs path as: folder/base_name_index
                return base.getPath() + "_" + index;
            } catch (Exception e) {
                throw new IllegalStateException("Indexing Error: Failed to resolve path for indexed function " + index, e);
            }
        }

        @Override
        public String getPathPrefix() {
            return base.getPathPrefix();
        }

        @Override
        public String getIdentifier() {
            return DatapackConfig.CUSTOM_NAMESPACE + ":" + getPath();
        }

        @Override
        public String toString() {
            return getIdentifier();
        }
    }
}