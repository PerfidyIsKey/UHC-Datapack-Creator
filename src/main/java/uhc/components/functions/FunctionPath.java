package uhc.components.functions;

/**
 * 🔗 **Type-Safe Minecraft Function Paths**
 * * Defines all available .mcfunction files in the datapack, organized by logical folders.
 */
public enum FunctionPath {

    // =================================================================================
    // 1. CORE / MAIN GAME FLOW (Root or 'core' folder)
    // =================================================================================
    /** Root function path, used for global reset. Represents: /function/reset.mcfunction */
    RESET,

    /** Main game loop called by a repeating timer. Represents: /function/main_loop.mcfunction */
    MAIN_LOOP,

    // =================================================================================
    // 2. INITIALIZATION ('init' folder) - Now focused on core setup
    // =================================================================================
    /** Runs on pack load. Represents: /function/init/load.mcfunction */
    LOAD("init"),

    /** Initial game setup. Represents: /function/init/initialize.mcfunction */
    INITIALIZE("init"),

    // =================================================================================
    // 3. GAME STARTUP PROCEDURE ('startup' folder)
    // =================================================================================
    /** Starts the game sequence after initial setup. Represents: /function/startup/start_game.mcfunction */
    START_GAME("startup"),

    /** Spreads players across the map. Represents: /function/startup/spread_players.mcfunction */
    SPREAD_PLAYERS("startup"),

    /** Handles game starter functionality. Represents: /function/startup/game_starter.mcfunction */
    GAME_STARTER("startup"),

    /** Sets the game mode to survival. Represents: /function/startup/survival_mode.mcfunction */
    SURVIVAL_MODE("startup"),

    /** Displays intro call messages. Represents: /function/startup/into_calls.mcfunction */
    INTO_CALLS("startup"),

    /** Starts potion sequences (deprecated/utility). Represents: /function/startup/start_potions.mcfunction */
    START_POTIONS("startup"),

    /** Developer debug timer (e.g., every 20 seconds). MOVED FROM 'timer' Represents: /function/startup/timer_developer_20.mcfunction */
    TIMER_DEVELOPER_20("startup"), // Moved here

    // STARTUP - Predictions Sub-folder
    PREDICTIONS("startup/predictions"),
    PREDICTIONS_LOOP("startup/predictions"),

    // STARTUP - Random Teams Sub-folder
    RANDOM_TEAMS1("startup/teams"),
    RANDOM_TEAMS2("startup/teams"),
    RANDOM_TEAMS3("startup/teams"),
    RANDOM_TEAMS4("startup/teams"),
    RANDOM_TEAMS5("startup/teams"),
    RANDOM_TEAMS6("startup/teams"),
    RANDOM_TEAMS7("startup/teams"),
    RANDOM_TEAMS8("startup/teams"),

    // STARTUP - Developer Mode Sub-folder
    DEVELOPER_MODE("startup/developer"),
    DEVELOPER_POTION_CONTROL("startup/developer"),

    // =================================================================================
    // 4. DEATH HANDLER ('death_handler' folder)
    // =================================================================================
    /** Handles player death event. MOVED FROM ROOT. Represents: /function/death_handler/handle_player_death.mcfunction */
    HANDLE_PLAYER_DEATH("death_handler"),

    /** Handles player respawn logic. MOVED FROM ROOT. Represents: /function/death_handler/respawn_player.mcfunction */
    RESPAWN_PLAYER("death_handler"),

    /** Disables player respawn. MOVED FROM 'util'. Represents: /function/death_handler/disable_respawn.mcfunction */
    DISABLE_RESPAWN("death_handler"),

    /** Drops player heads upon death. MOVED FROM 'util'. Represents: /function/death_handler/drop_player_heads.mcfunction */
    DROP_PLAYER_HEADS("death_handler"),

    // =================================================================================
    // 5. GAME END LOGIC ('game_end' folder)
    // =================================================================================
    /** Logic to initiate the deathmatch phase. MOVED FROM 'teams'. Represents: /function/game_end/initiate_deathmatch.mcfunction */
    INITIATE_DEATHMATCH("game_end"),

    /** Main deathmatch commands. MOVED FROM 'teams'. Represents: /function/game_end/death_match.mcfunction */
    DEATH_MATCH("game_end"),

    /** Checks which teams are still alive. MOVED FROM 'teams'. Represents: /function/game_end/teams_alive_check.mcfunction */
    TEAMS_ALIVE_CHECK("game_end"),
    TEAMS_HIGHSCORE_ALIVE_CHECK("game_end"),

    // =================================================================================
    // 6. MAIN TIMERS / SCHEDULING ('timer' folder)
    // =================================================================================
    /** Main repeating timer (e.g., every 1 second). Represents: /function/timer/main_1.mcfunction */
    TIMER_MAIN_1("timer"),

    /** Main repeating timer (e.g., every 5 seconds). Represents: /function/timer/main_5.mcfunction */
    TIMER_MAIN_5("timer"),

    /** Main repeating timer (e.g., every 20 seconds). Represents: /function/timer/main_20.mcfunction */
    TIMER_MAIN_20("timer"),

    /** Clears all scheduled functions. MOVED FROM 'timer'. Represents: /function/timer/clear_schedule.mcfunction */
    CLEAR_SCHEDULE("timer"),

    // =================================================================================
    // 7. CONTROL POINT / PERKS ('control_point' folder)
    // =================================================================================
    /** Initializes control point structures. Represents: /function/control_point/initialize.mcfunction */
    INITIALIZE_CONTROL_POINT("control_point"),

    /** Spawns all control point entities. Represents: /function/control_point/spawn.mcfunction */
    SPAWN_CONTROL_POINTS("control_point"),

    /** Timer for control points (e.g., every 20 seconds). MOVED FROM 'timer'. Represents: /function/control_point/timer_20.mcfunction */
    TIMER_CONTROL_POINT_20("control_point"), // Moved here

    /** Logic for the first control point. Represents: /function/control_point/point_1.mcfunction */
    CONTROL_POINT_1("control_point"),

    /** Logic for the second control point. Represents: /function/control_point/point_2.mcfunction */
    CONTROL_POINT_2("control_point"),

    /** Logic when a control point is captured. Represents: /function/control_point/captured.mcfunction */
    CONTROL_POINT_CAPTURED("control_point"),

    /** Checks if a player gets perk benefits. Represents: /function/control_point/perks_check.mcfunction */
    CONTROL_POINT_PERKS_CHECK("control_point"),

    // CONTROL POINT - Perks Sub-folder
    PERK_1("control_point/perks"),
    PERK_2("control_point/perks"),
    PERK_3("control_point/perks"),
    PERK_4("control_point/perks"),

    // CONTROL POINT - Sub-routines
    CONTROL_POINT_SCORE_1("control_point/score"),
    CONTROL_POINT_SCORE_2("control_point/score"),
    CONTROL_POINT_TEAM_SCORE("control_point/score"),
    CONTROL_POINT_UPDATE_RECORDS_1("control_point/score"),
    CONTROL_POINT_UPDATE_RECORDS_2("control_point/score"),
    CONTROL_POINT_VISUALS_1("control_point/visuals"),
    CONTROL_POINT_VISUALS_2("control_point/visuals"),
    CONTROL_POINT_MESSAGES_1("control_point/messages"),
    CONTROL_POINT_MESSAGES_2("control_point/messages"),
    SECOND_CONTROL_POINT("control_point/spawn"),

    /** Protection logic for beacons (Now part of visuals). Represents: /function/control_point/visuals/protect_beacon_1.mcfunction */
    PROTECT_BEACON_1("control_point/visuals"),
    PROTECT_BEACON_2("control_point/visuals"),

    // =================================================================================
    // 8. TRAITOR FACTION ('traitor' folder)
    // =================================================================================
    /** Traitor check logic. Represents: /function/traitor/check.mcfunction */
    TRAITOR_CHECK("traitor"),

    /** Traitor item handout logic. Represents: /function/traitor/handout.mcfunction */
    TRAITOR_HANDOUT("traitor"),

    /** Traitor action bar display. Represents: /function/traitor/actionbar.mcfunction */
    TRAITOR_ACTIONBAR("traitor"),

    /** Traitor check timer (e.g., every 5 seconds). Represents: /function/traitor/timer_5.mcfunction */
    TIMER_TRAITOR_5("traitor"),

    /** Traitor check timer (e.g., every 20 seconds). Represents: /function/traitor/timer_20.mcfunction */
    TIMER_TRAITOR_20("traitor"),

    // =================================================================================
    // 9. UTILITY / MISC ('util' folder)
    // =================================================================================
    /** Updates the health sidebar. Represents: /function/util/update_sidebar.mcfunction */
    UPDATE_SIDEBAR("util"),

    /** Updates the world border minimum health. Represents: /function/util/update_min_health.mcfunction */
    UPDATE_MIN_HEALTH("util"),

    /** Updates the player's mined block count. Represents: /function/util/update_mine_count.mcfunction */
    UPDATE_MINE_COUNT("util"),

    /** Clears ender chest contents. Represents: /function/util/clear_enderchest.mcfunction */
    CLEAR_ENDERCHEST("util"),

    /** Checks if a teammate is nearby. Represents: /function/util/locate_teammate.mcfunction */
    LOCATE_TEAMMATE("util"),

    /** Updates horse frost walker state. Represents: /function/util/horse_frost_walker.mcfunction */
    HORSE_FROST_WALKER("util"),

    /** Removes banned items from player inventories. MOVED FROM 'init'. Represents: /function/util/remove_banned_items.mcfunction */
    REMOVE_BANNED_ITEMS("util"),

    /** Wolf/Pet score updates. MOVED FROM 'teams'. Represents: /function/util/wolf_updates.mcfunction */
    WOLF_UPDATES("util"),

    // =================================================================================
    // 10. CARE PACKAGES ('care_packages' folder)
    // =================================================================================
    /** Drops care packages in the world. Represents: /function/care_packages/drop.mcfunction */
    DROP_CAREPACKAGES("care_packages"),

    // =================================================================================
    // 11. DISPLAY / MESSAGES ('display' folder)
    // =================================================================================
    /** Displays game rank (e.g., scoreboard) messages. Represents: /function/display/rank.mcfunction */
    DISPLAY_RANK("display"),

    /** Displays random quotes. Represents: /function/display/quotes.mcfunction */
    DISPLAY_QUOTES("display"),

    /** Announces Iron Man kill streak. Represents: /function/display/announce_iron_man.mcfunction */
    ANNOUNCE_IRON_MAN("display"),

    /** Checks if player achieved Iron Man status. Represents: /function/display/check_iron_man.mcfunction */
    CHECK_IRON_MAN("display"),

    /** Displays PVP enabled message. Represents: /function/display/messages_pvp.mcfunction */
    MESSAGES_PVP("display"),

    /** Displays Eternal Day message. Represents: /function/display/messages_eternal_day.mcfunction */
    MESSAGES_ETERNAL_DAY("display"),

    /** Schedules a single message display. Represents: /function/display/messages_schedule_single.mcfunction */
    MESSAGES_SCHEDULE_SINGLE("display"),

    // DISPLAY - Timer Display Sub-folder (Moved from 'timer/display')
    MINUTE_1("display/timer"), // Moved here
    MINUTE_2("display/timer"), // Moved here

    // DISPLAY - Victory Sub-folder
    VICTORY("display/victory"),
    VICTORY_MESSAGE_0("display/victory"),
    VICTORY_MESSAGE_1("display/victory"),
    VICTORY_MESSAGE_2("display/victory"),
    VICTORY_MESSAGE_3("display/victory"),
    VICTORY_MESSAGE_4("display/victory"),
    VICTORY_MESSAGE_5("display/victory"),
    VICTORY_MESSAGE_6("display/victory"),
    VICTORY_MESSAGE_7("display/victory"),
    VICTORY_MESSAGE_8("display/victory"),
    VICTORY_MESSAGE_9("display/victory"),
    VICTORY_MESSAGE_10("display/victory"),
    VICTORY_MESSAGE_11("display/victory"),
    VICTORY_MESSAGE_12("display/victory"),
    VICTORY_MESSAGE_TRAITOR("display/victory"),

    // =================================================================================
    // 12. FUN COMMANDS ('fun' folder)
    // =================================================================================
    /** Main battle royale commands. MOVED FROM 'teams'. Represents: /function/fun/battle_royale.mcfunction */
    BATTLE_ROYALE("fun"),

    /** Toggles god mode. MOVED FROM 'debug'. Represents: /function/fun/god_mode.mcfunction */
    GOD_MODE("fun"),
    /** Equips default starting gear. Represents: /function/startup/equip_gear.mcfunction */
    EQUIP_GEAR("startup")

    ; // Semicolon required to separate enum constants from fields/methods

    // The Minecraft-compliant path prefix (e.g., "init/" or "loop/main/")
    private final String pathPrefix;

    /**
     * Private constructor for functions located in the root 'function' folder.
     * * The path prefix defaults to an empty string.
     */
    FunctionPath() {
        this.pathPrefix = "";
    }

    /**
     * Private constructor for functions located within a specified folder structure.
     * * The input path is automatically normalized to use forward slashes ('/')
     * and a trailing slash is added to act as a prefix.
     * * @param folderLocation The folder path (e.g., "init" or "loop/main").
     */
    FunctionPath(String folderLocation) {
        if (folderLocation == null || folderLocation.isBlank()) {
            throw new IllegalArgumentException("Folder location cannot be null or empty if constructor is used.");
        }

        // Normalize slashes (Windows uses '\', Minecraft requires '/')
        String normalizedPath = folderLocation.trim().replace('\\', '/');

        // Ensure the path ends with a forward slash for use as a prefix
        if (!normalizedPath.endsWith("/")) {
            normalizedPath += "/";
        }

        this.pathPrefix = normalizedPath;
    }

    /**
     * Generates the final, complete Minecraft function path string.
     * * The path is formatted as: {@code [pathPrefix][enumName (lowercase)]}.
     * Example: For LOAD("init"), returns "init/load".
     * * @return The complete function path string, excluding the .mcfunction extension.
     */
    public String getPath() {
        // Ensure the enum constant name is converted to lowercase for compliance.
        String functionName = this.name().toLowerCase();

        // Concatenate the path prefix (e.g., "init/") with the function name (e.g., "load")
        return pathPrefix + functionName;
    }

    @Override
    public String toString() {
        return getPath();
    }
}