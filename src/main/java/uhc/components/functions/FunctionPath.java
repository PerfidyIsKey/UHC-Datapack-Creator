package uhc.components.functions;

import uhc.components.FunctionName;
import uhc.core.DatapackConfig;

/**
 * 🔗 **Type-Safe Minecraft Function Paths**
 * <p>
 * Maps Java constants to {@code .mcfunction} paths. Supports dynamic lookup for
 * numbered functions like Teams, Control Points, and Victory Messages.
 * </p>
 */
public enum FunctionPath implements FunctionName {

    // =================================================================================
    // 1. CORE / MAIN GAME FLOW
    // =================================================================================
    RESET,
    MAIN_LOOP,

    // =================================================================================
    // 2. INITIALIZATION ('init' folder)
    // =================================================================================
    LOAD("init"),
    INITIALIZE("init"),

    // =================================================================================
    // 3. GAME STARTUP PROCEDURE ('startup' folder)
    // =================================================================================
    START_GAME("startup"),
    SPREAD_PLAYERS("startup"),
    GAME_STARTER("startup"),
    SURVIVAL_MODE("startup"),
    INTO_CALLS("startup"),
    START_POTIONS("startup"),

    DEVELOPER_MODE("startup/developer"),
    DEVELOPER_POTION_CONTROL("startup/developer"),
    TIMER_DEVELOPER_20("startup/developer"),
    PREDICTIONS("startup/predictions"),
    PREDICTIONS_LOOP("startup/predictions"),

    // Numbered Teams (Accessible via fromTeamNumber)
    TEAM_RANDOM_1("startup/teams"),
    TEAM_RANDOM_2("startup/teams"),
    TEAM_RANDOM_3("startup/teams"),
    TEAM_RANDOM_4("startup/teams"),
    TEAM_RANDOM_5("startup/teams"),
    TEAM_RANDOM_6("startup/teams"),
    TEAM_RANDOM_7("startup/teams"),
    TEAM_RANDOM_8("startup/teams"),

    // =================================================================================
    // 4. DEATH HANDLER ('death_handler' folder)
    // =================================================================================
    HANDLE_PLAYER_DEATH("death_handler"),
    RESPAWN_PLAYER("death_handler"),
    DISABLE_RESPAWN("death_handler"),
    DROP_PLAYER_HEADS("death_handler"),

    // =================================================================================
    // 5. GAME END LOGIC ('game_end' folder)
    // =================================================================================
    INITIATE_DEATHMATCH("game_end"),
    DEATH_MATCH("game_end"),
    TEAMS_ALIVE_CHECK("game_end"),
    TEAMS_HIGHSCORE_ALIVE_CHECK("game_end"),

    // =================================================================================
    // 6. MAIN TIMERS / SCHEDULING ('timer' folder)
    // =================================================================================
    TIMER_MAIN_1("timer"),
    TIMER_MAIN_5("timer"),
    TIMER_MAIN_20("timer"),
    CLEAR_SCHEDULE("timer"),

    // =================================================================================
    // 7. CONTROL POINT / PERKS ('control_point' folder)
    // =================================================================================
    INITIALIZE_CONTROL_POINT("control_point"),
    SPAWN_CONTROL_POINTS("control_point"),
    TIMER_CONTROL_POINT_20("control_point"),
    CONTROL_POINT_CAPTURED("control_point"),
    CONTROL_POINT_PERKS_CHECK("control_point"),
    SECOND_CONTROL_POINT("control_point/spawn"),

    // Numbered Control Points (Accessible via fromControlPoint)
    CONTROL_POINT_1("control_point"),
    CONTROL_POINT_2("control_point"),

    // Numbered Perks
    PERK_1("control_point/perks"),
    PERK_2("control_point/perks"),
    PERK_3("control_point/perks"),
    PERK_4("control_point/perks"),

    CONTROL_POINT_SCORE_1("control_point/score"),
    CONTROL_POINT_SCORE_2("control_point/score"),
    CONTROL_POINT_TEAM_SCORE("control_point/score"),
    CONTROL_POINT_UPDATE_RECORDS_1("control_point/score"),
    CONTROL_POINT_UPDATE_RECORDS_2("control_point/score"),
    CONTROL_POINT_VISUALS_1("control_point/visuals"),
    CONTROL_POINT_VISUALS_2("control_point/visuals"),
    CONTROL_POINT_MESSAGES_1("control_point/messages"),
    CONTROL_POINT_MESSAGES_2("control_point/messages"),
    PROTECT_BEACON_1("control_point/visuals"),
    PROTECT_BEACON_2("control_point/visuals"),

    // =================================================================================
    // 8. TRAITOR FACTION ('traitor' folder)
    // =================================================================================
    TRAITOR_CHECK("traitor"),
    TRAITOR_HANDOUT("traitor"),
    TRAITOR_ACTIONBAR("traitor"),
    TIMER_TRAITOR_5("traitor"),
    TIMER_TRAITOR_20("traitor"),

    // =================================================================================
    // 9. UTILITY / MISC ('util' folder)
    // =================================================================================
    UPDATE_SIDEBAR("util"),
    UPDATE_MIN_HEALTH("util"),
    UPDATE_MINE_COUNT("util"),
    CLEAR_ENDERCHEST("util"),
    LOCATE_TEAMMATE("util"),
    HORSE_FROST_WALKER("util"),
    REMOVE_BANNED_ITEMS("util"),
    WOLF_UPDATES("util"),

    // =================================================================================
    // 10. CARE PACKAGES ('care_packages' folder)
    // =================================================================================
    DROP_CAREPACKAGES("care_packages"),

    // =================================================================================
    // 11. DISPLAY / MESSAGES ('display' folder)
    // =================================================================================
    DISPLAY_RANK("display"),
    DISPLAY_QUOTES("display"),
    ANNOUNCE_IRON_MAN("display"),
    CHECK_IRON_MAN("display"),
    MESSAGES_PVP("display"),
    MESSAGES_ETERNAL_DAY("display"),
    MESSAGES_SCHEDULE_SINGLE("display"),
    TIMER_MINUTE_1("display/timer"),
    TIMER_MINUTE_2("display/timer"),

    // Victory Messages (Accessible via fromVictoryMessage)
    VICTORY("display/victory"),
    VICTORY_MESSAGE_TRAITOR("display/victory"),
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

    // =================================================================================
    // 12. FUN COMMANDS ('fun' folder)
    // =================================================================================
    BATTLE_ROYALE("fun"),
    EQUIP_GEAR("fun"),
    GOD_MODE("fun")

    ; // End of constants

    private final String pathPrefix;

    FunctionPath() {
        this.pathPrefix = "";
    }

    FunctionPath(String folderLocation) {
        String normalizedPath = folderLocation.trim().replace('\\', '/');
        if (!normalizedPath.isEmpty() && !normalizedPath.endsWith("/")) {
            normalizedPath += "/";
        }
        this.pathPrefix = normalizedPath;
    }

    // --- Dynamic Lookup Methods ---

    /** Programmatic access to TEAM_RANDOM_X */
    public static FunctionPath fromTeamNumber(int num) {
        return fromNumberedConstant("TEAM_RANDOM_", num);
    }

    /** Programmatic access to CONTROL_POINT_X */
    public static FunctionPath fromControlPoint(int num) {
        return fromNumberedConstant("CONTROL_POINT_", num);
    }

    /** Programmatic access to VICTORY_MESSAGE_X */
    public static FunctionPath fromVictoryMessage(int num) {
        return fromNumberedConstant("VICTORY_MESSAGE_", num);
    }

    /** Programmatic access to PERKS_X */
    public static FunctionPath fromPerk(int num) {
        return fromNumberedConstant("PERKS_", num);
    }

    /** Helper to handle enum value lookup logic */
    private static FunctionPath fromNumberedConstant(String prefix, int num) {
        try {
            return FunctionPath.valueOf(prefix + num);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No function path constant found for: " + prefix + num);
        }
    }

    // --- Implementation ---

    public String getPath() {
        return pathPrefix + this.name().toLowerCase();
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