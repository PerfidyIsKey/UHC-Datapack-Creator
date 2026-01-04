package uhc.components.functions;

import uhc.components.FunctionName;
import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🔗 **Type-Safe Minecraft Function Paths**
 * <p>
 * This interface serves as the central registry for all {@code .mcfunction} file locations
 * within the datapack. It maps Java constants to normalized resource paths.
 * </p>
 * <p>
 * <b>Logic Branches:</b>
 * <ul>
 * <li><b>Static:</b> Standard functions (e.g., {@code init/load}).</li>
 * <li><b>Indexed:</b> Dynamically generated files (e.g., {@code startup/teams/team_random_1}).</li>
 * </ul>
 * </p>
 */
public interface FunctionPath extends FunctionName {

    // =================================================================================
    // 1. CORE / MAIN GAME FLOW
    // =================================================================================

    /** Logic for resetting all game values and scoreboards. */
    FunctionPath RESET = Internal.RESET;
    /** The 20-tick loop responsible for running main game logic. */
    FunctionPath MAIN_LOOP = Internal.MAIN_LOOP;
    /** A placeholder empty function for clearing schedule hooks. */
    FunctionPath EMPTY = Internal.EMPTY;

    // =================================================================================
    // 2. INITIALIZATION ('init' folder)
    // =================================================================================

    /** Standard Minecraft {@code load.json} entry point. */
    FunctionPath LOAD = Internal.LOAD;
    /** Standard initialization sequence for world settings. */
    FunctionPath INITIALIZE = Internal.INITIALIZE;
    /** Setup logic for scoreboard objectives and global constants. */
    FunctionPath INITIALIZATION = Internal.INITIALIZATION;

    // =================================================================================
    // 3. GAME STARTUP PROCEDURE ('startup' folder)
    // =================================================================================

    /** Command sequence to officially begin the match. */
    FunctionPath START_GAME = Internal.START_GAME;
    /** Logic for randomizing player distribution across the world. */
    FunctionPath SPREAD_PLAYERS = Internal.SPREAD_PLAYERS;
    /** The countdown and timer logic for the game start. */
    FunctionPath GAME_STARTER = Internal.GAME_STARTER;
    /** Mass survival mode assignment for all participants. */
    FunctionPath SURVIVAL_MODE = Internal.SURVIVAL_MODE;
    /** Logic for integrating Discord/Voice proximity calls. */
    FunctionPath INTO_CALLS = Internal.INTO_CALLS;
    /** Initial handout of starting potion effects. */
    FunctionPath START_POTIONS = Internal.START_POTIONS;
    /** Logic for registering player match predictions. */
    FunctionPath PREDICTIONS = Internal.PREDICTIONS;
    /** Background logic for processing prediction results. */
    FunctionPath PREDICTIONS_LOOP = Internal.PREDICTIONS_LOOP;
    /** Base path for randomized team assignments. */
    FunctionPath TEAM_RANDOM = Internal.TEAM_RANDOM;

    // Developer / Debug Tools
    /** Toggles developer-only administrative tools. */
    FunctionPath DEVELOPER_MODE = Internal.DEVELOPER_MODE;
    /** UI controls for manually modifying player potions. */
    FunctionPath DEVELOPER_POTION_CONTROL = Internal.DEVELOPER_POTION_CONTROL;
    /** A high-speed developer tick timer. */
    FunctionPath TIMER_DEVELOPER_20 = Internal.TIMER_DEVELOPER_20;

    // =================================================================================
    // 4. DEATH HANDLER ('death_handler' folder)
    // =================================================================================

    /** Core logic triggered when a player's health reaches zero. */
    FunctionPath HANDLE_PLAYER_DEATH = Internal.HANDLE_PLAYER_DEATH;
    /** Command sequence for returning a player to the game. */
    FunctionPath RESPAWN_PLAYER = Internal.RESPAWN_PLAYER;
    /** Global toggle to prevent players from entering respawn screens. */
    FunctionPath DISABLE_RESPAWN = Internal.DISABLE_RESPAWN;
    /** Spawns a custom player head at death coordinates. */
    FunctionPath DROP_PLAYER_HEADS = Internal.DROP_PLAYER_HEADS;

    // =================================================================================
    // 5. GAME END LOGIC ('game_end' folder)
    // =================================================================================

    /** Transition logic for the final arena shrinking phase. */
    FunctionPath INITIATE_DEATHMATCH = Internal.INITIATE_DEATHMATCH;
    /** Active combat rules for the deathmatch phase. */
    FunctionPath DEATH_MATCH = Internal.DEATH_MATCH;
    /** Evaluates the count of teams with remaining members. */
    FunctionPath TEAMS_ALIVE_CHECK = Internal.TEAMS_ALIVE_CHECK;
    /** Evaluates the survival count based on custom highscore data. */
    FunctionPath TEAMS_HIGHSCORE_ALIVE_CHECK = Internal.TEAMS_HIGHSCORE_ALIVE_CHECK;

    // =================================================================================
    // 6. MAIN TIMERS / SCHEDULING ('timer' folder)
    // =================================================================================

    /** Primary 1-second interval execution loop. */
    FunctionPath TIMER_MAIN_1 = Internal.TIMER_MAIN_1;
    /** Periodic 5-second interval execution loop. */
    FunctionPath TIMER_MAIN_5 = Internal.TIMER_MAIN_5;
    /** Periodic 20-second interval execution loop. */
    FunctionPath TIMER_MAIN_20 = Internal.TIMER_MAIN_20;
    /** Utility to clear all pending scheduled function executions. */
    FunctionPath CLEAR_SCHEDULE = Internal.CLEAR_SCHEDULE;

    // =================================================================================
    // 7. CONTROL POINT ('control_point' folder)
    // =================================================================================

    /** Logic for generating CP structures and zones. */
    FunctionPath INITIALIZE_CONTROL_POINT = Internal.INITIALIZE_CONTROL_POINT;
    /** Entity spawning for capture zone indicators. */
    FunctionPath SPAWN_CONTROL_POINTS = Internal.SPAWN_CONTROL_POINTS;
    /** Interval timer for capture progress calculation. */
    FunctionPath TIMER_CONTROL_POINT_20 = Internal.TIMER_CONTROL_POINT_20;
    /** Logic executed immediately upon a successful team capture. */
    FunctionPath CONTROL_POINT_CAPTURED = Internal.CONTROL_POINT_CAPTURED;
    /** Logic for distributing perks to the capturing team. */
    FunctionPath CONTROL_POINT_PERKS_CHECK = Internal.CONTROL_POINT_PERKS_CHECK;
    /** Spawning logic for the secondary mid-game control point. */
    FunctionPath SECOND_CONTROL_POINT = Internal.SECOND_CONTROL_POINT;
    /** Updates team-specific scores based on occupancy. */
    FunctionPath CONTROL_POINT_TEAM_SCORE = Internal.CONTROL_POINT_TEAM_SCORE;

    /** Base path for the primary control point logic. */
    FunctionPath CONTROL_POINT = Internal.CONTROL_POINT;
    /** Base path for the control point scoreboard management. */
    FunctionPath CONTROL_POINT_SCORE = Internal.CONTROL_POINT_SCORE;
    /** Logic for updating highscore records on the CP. */
    FunctionPath CONTROL_POINT_UPDATE_RECORDS = Internal.CONTROL_POINT_UPDATE_RECORDS;
    /** Controls particle and visual effects for the CP. */
    FunctionPath CONTROL_POINT_VISUALS = Internal.CONTROL_POINT_VISUALS;
    /** Broadcasts status updates for the CP to chat. */
    FunctionPath CONTROL_POINT_MESSAGES = Internal.CONTROL_POINT_MESSAGES;
    /** Protection logic to prevent players from breaking CP beacons. */
    FunctionPath PROTECT_BEACON = Internal.PROTECT_BEACON;
    /** Base path for specific CP perks. */
    FunctionPath PERK = Internal.PERK;

    // =================================================================================
    // 8. TRAITOR FACTION ('traitor' folder)
    // =================================================================================

    /** Checks if the Traitor faction has met victory conditions. */
    FunctionPath TRAITOR_CHECK = Internal.TRAITOR_CHECK;
    /** Logic for handing out special traitor-only gear. */
    FunctionPath TRAITOR_HANDOUT = Internal.TRAITOR_HANDOUT;
    /** Updates the special UI for hidden traitor roles. */
    FunctionPath TRAITOR_ACTIONBAR = Internal.TRAITOR_ACTIONBAR;
    /** 5-second traitor specific interval. */
    FunctionPath TIMER_TRAITOR_5 = Internal.TIMER_TRAITOR_5;
    /** 20-second traitor specific interval. */
    FunctionPath TIMER_TRAITOR_20 = Internal.TIMER_TRAITOR_20;

    // =================================================================================
    // 9. UTILITY / MISC ('util' folder)
    // =================================================================================

    /** Forces a visual update on the player sidebar. */
    FunctionPath UPDATE_SIDEBAR = Internal.UPDATE_SIDEBAR;
    /** Logic to find and display the lowest health among players. */
    FunctionPath UPDATE_MIN_HEALTH = Internal.UPDATE_MIN_HEALTH;
    /** Updates the mining stats objective for participants. */
    FunctionPath UPDATE_MINE_COUNT = Internal.UPDATE_MINE_COUNT;
    /** Purges all player Ender Chests. */
    FunctionPath CLEAR_ENDERCHEST = Internal.CLEAR_ENDERCHEST;
    /** Highlights the position of teammates through walls. */
    FunctionPath LOCATE_TEAMMATE = Internal.LOCATE_TEAMMATE;
    /** Grants Frost Walker behavior to mounts. */
    FunctionPath HORSE_FROST_WALKER = Internal.HORSE_FROST_WALKER;
    /** Scans and removes items restricted by the game rules. */
    FunctionPath REMOVE_BANNED_ITEMS = Internal.REMOVE_BANNED_ITEMS;
    /** Logic for wolf aging and collar customization. */
    FunctionPath WOLF_UPDATES = Internal.WOLF_UPDATES;

    // =================================================================================
    // 10. CARE PACKAGES ('care_packages' folder)
    // =================================================================================

    /** Logic for dropping care packages at random coordinates. */
    FunctionPath DROP_CAREPACKAGES = Internal.DROP_CAREPACKAGES;

    // =================================================================================
    // 11. DISPLAY / MESSAGES ('display' folder)
    // =================================================================================

    /** Shows current leaderboard ranks. */
    FunctionPath DISPLAY_RANK = Internal.DISPLAY_RANK;
    /** Logic for displaying randomized motivational quotes. */
    FunctionPath DISPLAY_QUOTES = Internal.DISPLAY_QUOTES;
    /** Public broadcast for the Iron Man award. */
    FunctionPath ANNOUNCE_IRON_MAN = Internal.ANNOUNCE_IRON_MAN;
    /** Continuous check for Iron Man health criteria. */
    FunctionPath CHECK_IRON_MAN = Internal.CHECK_IRON_MAN;
    /** Messaging for PvP status transitions. */
    FunctionPath MESSAGES_PVP = Internal.MESSAGES_PVP;
    /** Messaging for Eternal Day status. */
    FunctionPath MESSAGES_ETERNAL_DAY = Internal.MESSAGES_ETERNAL_DAY;
    /** Handles single-line scheduled notifications. */
    FunctionPath MESSAGES_SCHEDULE_SINGLE = Internal.MESSAGES_SCHEDULE_SINGLE;
    /** Core victory sequence logic. */
    FunctionPath VICTORY = Internal.VICTORY;
    /** Specific win message for the Traitor. */
    FunctionPath VICTORY_MESSAGE_TRAITOR = Internal.VICTORY_MESSAGE_TRAITOR;
    /** Base for numbered victory message displays. */
    FunctionPath VICTORY_MESSAGE = Internal.VICTORY_MESSAGE;
    /** Logic for minute-based timer announcements. */
    FunctionPath TIMER_MINUTE = Internal.TIMER_MINUTE;

    // =================================================================================
    // 12. FUN COMMANDS ('fun' folder)
    // =================================================================================

    /** Battle Royale shrinking zone mechanics. */
    FunctionPath BATTLE_ROYALE = Internal.BATTLE_ROYALE;
    /** Equipment handout for special events. */
    FunctionPath EQUIP_GEAR = Internal.EQUIP_GEAR;
    /** Logic for invincibility/God Mode. */
    FunctionPath GOD_MODE = Internal.GOD_MODE;

    // --- ⚙️ Core Contract ---

    /**
     * Retrieves the relative path within the namespace (e.g., "init/initialize").
     * @return The non-null relative path string.
     */
    String getPath();

    /**
     * Retrieves the folder location prefix for the function.
     * @return The folder path ending with a slash, or empty string if root.
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
        RESET, MAIN_LOOP, EMPTY,
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

        /** * The pre-formatted folder path with a trailing slash. */
        private final String pathPrefix;

        /** Root folder constructor (no prefix). */
        Internal() { this.pathPrefix = ""; }

        /**
         * Folder-specific constructor.
         * @param folder The folder path string.
         */
        Internal(String folder) {
            Objects.requireNonNull(folder, "Internal Path Error: Folder path cannot be null.");
            this.pathPrefix = folder.endsWith("/") ? folder : folder + "/";
        }

        @Override public String getPath() { return pathPrefix + this.name().toLowerCase(); }
        @Override public String getPathPrefix() { return pathPrefix; }
        @Override public String getIdentifier() { return DatapackConfig.CUSTOM_NAMESPACE + ":" + getPath(); }
        @Override public String toString() { return getIdentifier(); }
    }

    /**
     * Implementation for functions requiring a numerical suffix.
     * @param base  The base FunctionPath definition.
     * @param index The specific index number.
     */
    record IndexedFunction(FunctionPath base, int index) implements FunctionPath {

        @Override
        public String getPath() {
            try {
                // Formatting: folder/name_index
                return base.getPath() + "_" + index;
            } catch (Exception e) {
                throw new IllegalStateException("Indexing Failure: Could not resolve path for index " + index, e);
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