package uhc.resource.gameplay;

/**
 * 📜 **Game Rule Identifier Registry**
 * <p>
 * Defines the comprehensive list of Minecraft game rules used to modify world behavior.
 * This enum maps Java constants to the specific lower-camel-case strings required
 * by the {@code /gamerule} command.
 * </p>
 * <p>Reference: <a href="https://minecraft.fandom.com/wiki/Game_rule">Minecraft Wiki - Game Rules</a></p>
 */
public enum GameRuleId {

    // --- 📢 Announcement & Feedback Rules ---

    /** Whether advancements should be announced in chat. */
    ANNOUNCE_ADVANCEMENTS("announceAdvancements"),
    /** Whether command blocks should notify admins when they perform a command. */
    COMMAND_BLOCK_OUTPUT("commandBlockOutput"),
    /** Whether the feedback from commands executed by a player should show up in chat. */
    SEND_COMMAND_FEEDBACK("sendCommandFeedback"),
    /** Whether death messages are put into chat when a player dies. */
    SHOW_DEATH_MESSAGES("showDeathMessages"),
    /** Whether admin commands should be logged to the server log. */
    LOG_ADMIN_COMMANDS("logAdminCommands"),

    // --- 🌍 World & Environment Rules ---

    /** Whether the daylight cycle and moon phases progress. */
    DO_DAYLIGHT_CYCLE("doDaylightCycle"),
    /** Whether the weather cycle progresses. */
    DO_WEATHER_CYCLE("doWeatherCycle"),
    /** Whether fire should spread and naturally expire. */
    DO_FIRE_TICK("doFireTick"),
    /** Whether vines can spread to adjacent blocks. */
    DO_VINES_SPREAD("doVinesSpread"),
    /** Whether snow accumulation can exceed one layer. */
    SNOW_ACCUMULATION_HEIGHT("snowAccumulationHeight"),
    /** Whether water source blocks can be created via conversion. */
    WATER_SOURCE_CONVERSION("waterSourceConversion"),
    /** Whether lava source blocks can be created via conversion. */
    LAVA_SOURCE_CONVERSION("lavaSourceConversion"),
    /** How often a random block tick occurs per chunk section per game tick. */
    RANDOM_TICK_SPEED("randomTickSpeed"),

    // --- 🛡️ Player & Damage Rules ---

    /** Whether players should naturally regenerate health (critical for UHC). */
    NATURAL_REGENERATION("naturalRegeneration"),
    /** Whether players keep their inventory and XP on death. */
    KEEP_INVENTORY("keepInventory"),
    /** Whether players take damage from drowning. */
    DROWNING_DAMAGE("drowningDamage"),
    /** Whether players take damage from falling. */
    FALL_DAMAGE("fallDamage"),
    /** Whether players take damage from fire. */
    FIRE_DAMAGE("fireDamage"),
    /** Whether players take damage from freezing. */
    FREEZE_DAMAGE("freezeDamage"),
    /** Whether players should respawn immediately without showing the death screen. */
    DO_IMMEDIATE_RESPAWN("doImmediateRespawn"),
    /** Whether players must possess a recipe to craft an item. */
    DO_LIMITED_CRAFTING("doLimitedCrafting"),
    /** The number of blocks from spawn where new players are placed. */
    SPAWN_RADIUS("spawnRadius"),

    // --- 👾 Mob & Spawning Rules ---

    /** Whether mobs should naturally spawn. */
    DO_MOB_SPAWNING("doMobSpawning"),
    /** Whether mobs should drop items (loot). */
    DO_MOB_LOOT("doMobLoot"),
    /** Whether mobs can modify blocks (e.g., Endermen picking up blocks). */
    MOB_GRIEFING("mobGriefing"),
    /** Whether Phantoms can spawn at night. */
    DO_INSOMNIA("doInsomnia"),
    /** Whether Pillager patrols can spawn. */
    DO_PATROL_SPAWNING("doPatrolSpawning"),
    /** Whether Wandering Traders can spawn. */
    DO_TRADER_SPAWNING("doTraderSpawning"),
    /** Whether Wardens can spawn. */
    DO_WARDEN_SPAWNING("doWardenSpawning"),
    /** Whether raids are disabled. */
    DISABLE_RAIDS("disableRaids"),
    /** Maximum number of entities a single space can hold before they take cramming damage. */
    MAX_ENTITY_CRAMMING("maxEntityCramming"),

    // --- 📦 Drop & Explosion Rules ---

    /** Whether non-mob entities (like frames or armor stands) drop items. */
    DO_ENTITY_DROPS("doEntityDrops"),
    /** Whether blocks (tiles) drop items when broken. */
    DO_TILE_DROPS("doTileDrops"),
    /** Whether items dropped by a block explosion decay/disappear. */
    BLOCK_EXPLOSION_DROP_DECAY("blockExplosionDropDecay"),
    /** Whether items dropped by a mob explosion decay/disappear. */
    MOB_EXPLOSION_DROP_DECAY("mobExplosionDropDecay"),
    /** Whether items dropped by a TNT explosion decay/disappear. */
    TNT_EXPLOSION_DROP_DECAY("tntExplosionDropDecay"),

    // --- ⚙️ Technical & Performance Rules ---

    /** Maximum blocks a command can modify (e.g., /fill or /clone). */
    COMMAND_MODIFICATION_BLOCK_LIMIT("commandModificationBlockLimit"),
    /** Maximum length of a command block chain. */
    MAX_COMMAND_CHAIN_LENGTH("maxCommandChainLength"),
    /** Whether to disable the server's check for elytra movement speed. */
    DISABLE_ELYTRA_MOVEMENT_CHECK("disableElytraMovementCheck"),
    /** Whether spectators can generate chunks. */
    SPECTATORS_GENERATE_CHUNKS("spectatorsGenerateChunks"),
    /** Whether the debug screen is simplified. */
    REDUCED_DEBUG_INFO("reducedDebugInfo"),

    // --- 🏹 Miscellaneous & Modern Rules ---

    /** Whether Ender Pearls vanish when the player who threw them dies. */
    ENDER_PEARLS_VANISH_ON_DEATH("enderPearlsVanishOnDeath"),
    /** Whether angry neutral mobs stay angry when the target dies. */
    FORGIVE_DEAD_PLAYERS("forgiveDeadPlayers"),
    /** Whether neutral mobs attack any nearby player when angered. */
    UNIVERSAL_ANGER("universalAnger"),
    /** Whether certain sounds (like the Ender Dragon death) are heard globally. */
    GLOBAL_SOUND_EVENTS("globalSoundEvents"),
    /** The percentage of players required to sleep to skip the night. */
    PLAYERS_SLEEPING_PERCENTAGE("playersSleepingPercentage"),
    /** Whether the locator bar is shown on maps. */
    LOCATOR_BAR("locatorBar");

    // --- ⚙️ State & Fields ---

    /** * The specific string identifier used by Minecraft in command syntax. */
    private final String commandName;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for defining the command string for each rule.
     * @param commandName The lower-camel-case string (e.g., "doDaylightCycle").
     */
    GameRuleId(String commandName) {
        this.commandName = commandName;
    }

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the command-ready string for the gamerule.
     * @return The identifier used in {@code /gamerule <rule>}.
     */
    public String getCommandName() {
        return commandName;
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a GameRuleId from its command string.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup. If no matching
     * rule is found, it returns {@code null} rather than throwing an exception,
     * allowing for graceful handling of unsupported rules in older versions.</p>
     * @param rawName The raw string from a command or config (e.g., "keepInventory").
     * @return The matching {@link GameRuleId}, or {@code null} if unrecognized.
     */
    public static GameRuleId fromString(String rawName) {
        if (rawName == null || rawName.isBlank()) return null;

        String target = rawName.trim();
        for (GameRuleId rule : values()) {
            if (rule.commandName.equalsIgnoreCase(target)) {
                return rule;
            }
        }
        return null;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lower-camel-case string required by the Minecraft command syntax.
     * @return The result of {@link #getCommandName()}.
     */
    @Override
    public String toString() {
        return commandName;
    }
}