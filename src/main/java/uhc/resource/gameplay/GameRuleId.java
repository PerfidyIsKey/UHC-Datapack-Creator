package uhc.resource.gameplay;

import uhc.util.NameConversion;
import java.util.Objects;

/**
 * 📜 **Game Rule Identifier Registry (GameRuleId)**
 * <p>
 * This registry contains the comprehensive list of Minecraft game rules used to modify world behavior.
 * Instead of hardcoding strings, this enum dynamically generates command identifiers from the
 * constant names using {@link NameConversion#sssToPascal(String)}, then converting to lowerCamelCase.
 * </p>
 */
public enum GameRuleId {

    // --- 📢 Announcement & Feedback Rules ---

    /** Whether advancements should be announced in chat. */
    ANNOUNCE_ADVANCEMENTS(RuleType.BOOLEAN),
    /** Whether command blocks should notify admins when they perform a command. */
    COMMAND_BLOCK_OUTPUT(RuleType.BOOLEAN),
    /** Whether command feedback from players is shown in chat. */
    SEND_COMMAND_FEEDBACK(RuleType.BOOLEAN),
    /** Whether death messages are put into chat. */
    SHOW_DEATH_MESSAGES(RuleType.BOOLEAN),
    /** Whether admin commands are logged to the server log. */
    LOG_ADMIN_COMMANDS(RuleType.BOOLEAN),

    // --- 🌍 World & Environment Rules ---

    /** Whether the daylight cycle progresses. */
    DO_DAYLIGHT_CYCLE(RuleType.BOOLEAN),
    /** Whether the weather cycle progresses. */
    DO_WEATHER_CYCLE(RuleType.BOOLEAN),
    /** Whether fire should spread and expire. */
    DO_FIRE_TICK(RuleType.BOOLEAN),
    /** Whether vines can spread. */
    DO_VINES_SPREAD(RuleType.BOOLEAN),
    /** The maximum height of snow accumulation. */
    SNOW_ACCUMULATION_HEIGHT(RuleType.INTEGER),
    /** Whether water source blocks can be created via conversion. */
    WATER_SOURCE_CONVERSION(RuleType.BOOLEAN),
    /** Whether lava source blocks can be created via conversion. */
    LAVA_SOURCE_CONVERSION(RuleType.BOOLEAN),
    /** The speed of random block ticks per chunk section. */
    RANDOM_TICK_SPEED(RuleType.INTEGER),

    // --- 🛡️ Player & Damage Rules ---

    /** Whether players naturally regenerate health. */
    NATURAL_REGENERATION(RuleType.BOOLEAN),
    /** Whether players keep inventory on death. */
    KEEP_INVENTORY(RuleType.BOOLEAN),
    /** Whether players take damage from drowning. */
    DROWNING_DAMAGE(RuleType.BOOLEAN),
    /** Whether players take damage from falling. */
    FALL_DAMAGE(RuleType.BOOLEAN),
    /** Whether players take damage from fire. */
    FIRE_DAMAGE(RuleType.BOOLEAN),
    /** Whether players take damage from freezing. */
    FREEZE_DAMAGE(RuleType.BOOLEAN),
    /** Whether players respawn immediately without a death screen. */
    DO_IMMEDIATE_RESPAWN(RuleType.BOOLEAN),
    /** Whether players must know a recipe to craft. */
    DO_LIMITED_CRAFTING(RuleType.BOOLEAN),
    /** The radius in blocks around spawn for new players. */
    SPAWN_RADIUS(RuleType.INTEGER),

    // --- 👾 Mob & Spawning Rules ---

    /** Whether mobs should naturally spawn. */
    DO_MOB_SPAWNING(RuleType.BOOLEAN),
    /** Whether mobs should drop loot. */
    DO_MOB_LOOT(RuleType.BOOLEAN),
    /** Whether mobs can modify blocks (e.g. Endermen). */
    MOB_GRIEFING(RuleType.BOOLEAN),
    /** Whether Phantoms can spawn. */
    DO_INSOMNIA(RuleType.BOOLEAN),
    /** Whether Pillager patrols can spawn. */
    DO_PATROL_SPAWNING(RuleType.BOOLEAN),
    /** Whether Wandering Traders can spawn. */
    DO_TRADER_SPAWNING(RuleType.BOOLEAN),
    /** Whether Wardens can spawn. */
    DO_WARDEN_SPAWNING(RuleType.BOOLEAN),
    /** Whether raids are disabled. */
    DISABLE_RAIDS(RuleType.BOOLEAN),
    /** Max entities per block before cramming damage occurs. */
    MAX_ENTITY_CRAMMING(RuleType.INTEGER),

    // --- 📦 Drop & Explosion Rules ---

    /** Whether non-mob entities drop items. */
    DO_ENTITY_DROPS(RuleType.BOOLEAN),
    /** Whether blocks drop items when broken. */
    DO_TILE_DROPS(RuleType.BOOLEAN),
    /** Whether block explosion items decay. */
    BLOCK_EXPLOSION_DROP_DECAY(RuleType.BOOLEAN),
    /** Whether mob explosion items decay. */
    MOB_EXPLOSION_DROP_DECAY(RuleType.BOOLEAN),
    /** Whether TNT explosion items decay. */
    TNT_EXPLOSION_DROP_DECAY(RuleType.BOOLEAN),

    // --- ⚙️ Technical & Performance Rules ---

    /** Max blocks a command can modify. */
    COMMAND_MODIFICATION_BLOCK_LIMIT(RuleType.INTEGER),
    /** Max length of command block chains. */
    MAX_COMMAND_CHAIN_LENGTH(RuleType.INTEGER),
    /** Whether to disable the elytra movement speed check. */
    DISABLE_ELYTRA_MOVEMENT_CHECK(RuleType.BOOLEAN),
    /** Whether spectators can generate chunks. */
    SPECTATORS_GENERATE_CHUNKS(RuleType.BOOLEAN),
    /** Whether the debug screen is simplified. */
    REDUCED_DEBUG_INFO(RuleType.BOOLEAN),

    // --- 🏹 Miscellaneous & Modern Rules ---

    /** Whether Ender Pearls vanish when the thrower dies. */
    ENDER_PEARLS_VANISH_ON_DEATH(RuleType.BOOLEAN),
    /** Whether angry mobs stay angry when the target dies. */
    FORGIVE_DEAD_PLAYERS(RuleType.BOOLEAN),
    /** Whether mobs attack any nearby player when angered. */
    UNIVERSAL_ANGER(RuleType.BOOLEAN),
    /** Whether sounds like Ender Dragon death are heard globally. */
    GLOBAL_SOUND_EVENTS(RuleType.BOOLEAN),
    /** Percentage of players required to sleep to skip night. */
    PLAYERS_SLEEPING_PERCENTAGE(RuleType.INTEGER),
    /** Whether the locator bar is shown on maps. */
    LOCATOR_BAR(RuleType.BOOLEAN);

    // --- 📄 Fields ---

    /** The command-ready string (e.g., "doDaylightCycle"). */
    private final String commandName;

    /** The associated data type (BOOLEAN or INTEGER) for this rule. */
    private final RuleType type;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for defining rules and their types.
     * <p><b>Logic:</b> Transforms the constant name (SCREAMING_SNAKE) into lowerCamelCase
     * using the conversion utility and a first-character transformation.</p>
     * @param type The non-null {@link RuleType}.
     * @throws NullPointerException if type is null.
     * @throws RuntimeException if the name transformation fails.
     */
    GameRuleId(RuleType type) {
        this.type = Objects.requireNonNull(type, "GameRule Construction Error: RuleType cannot be null.");

        try {
            // Convert "DO_DAYLIGHT_CYCLE" to "DoDaylightCycle"
            final String pascal = NameConversion.sssToPascal(this.name());

            // Convert "DoDaylightCycle" to "doDaylightCycle"
            this.commandName = Character.toLowerCase(pascal.charAt(0)) + pascal.substring(1);

            if (this.commandName.isBlank()) {
                throw new IllegalStateException("Resulting command name is blank.");
            }
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to transform GameRuleId name: " + this.name(), e);
        }
    }

    // --- 🔍 Static Registry Methods ---

    /**
     * Looks up a GameRuleId from its Minecraft string representation.
     * @param rawName The raw string (e.g. "keepInventory").
     * @return The matching ID, or null if not found.
     */
    public static GameRuleId fromString(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return null;
        }

        final String target = rawName.trim();
        for (GameRuleId rule : values()) {
            if (rule.commandName.equalsIgnoreCase(target)) {
                return rule;
            }
        }
        return null;
    }

    // --- 🛰️ Accessors & Overrides ---

    /** @return The lowerCamelCase string used in Minecraft commands. */
    public String getCommandName() {
        return commandName;
    }

    /** @return The {@link RuleType} required for this rule's value. */
    public RuleType getType() {
        return type;
    }

    /** @return The command-safe string. */
    @Override
    public String toString() {
        return commandName;
    }

    // --- 🏷️ Type Definition ---

    /**
     * Defines the supported data types for Minecraft Gamerules.
     */
    public enum RuleType {
        /** Represents boolean (true/false) values. */
        BOOLEAN,
        /** Represents whole number (integer) values. */
        INTEGER
    }
}