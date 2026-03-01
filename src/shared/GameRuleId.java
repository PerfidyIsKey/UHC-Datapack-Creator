package shared;

// https://minecraft.fandom.com/wiki/Game_rule
public enum GameRuleId {
    SHOW_ADVANCEMENT_MESSAGES("show_advancement_messages"),
    BLOCK_EXPLOSION_DROP_DECAY("block_explosion_drop_decay"),
    COMMAND_BLOCK_OUTPUT("command_block_output"),
    MAX_BLOCK_MODIFICATIONS("max_block_modifications"),
    ELYTRA_MOVEMENT_CHECK("elytra_movement_check"), // Logic inverted
    PLAYER_MOVEMENT_CHECK("player_movement_check"), // Logic inverted
    RAIDS("raids"), // Logic inverted
    ADVANCE_TIME("advance_time"),
    ENTITY_DROPS("entity_drops"),
    DO_FIRE_TICK("do_fire_tick"),
    SPAWN_PHANTOMS("spawn_phantoms"),
    IMMEDIATE_RESPAWN("immediate_respawn"),
    LIMITED_CRAFTING("limited_crafting"),
    MOB_DROPS("mob_drops"),
    SPAWN_MOBS("spawn_mobs"),
    SPAWN_PATROLS("spawn_patrols"),
    BLOCK_DROPS("block_drops"),
    SPAWN_WANDERING_TRADERS("spawn_wandering_traders"),
    SPREAD_VINES("spread_vines"),
    ADVANCE_WEATHER("advance_weather"),
    SPAWN_WARDENS("spawn_wardens"),
    DROWNING_DAMAGE("drowning_damage"),
    ENDER_PEARLS_VANISH_ON_DEATH("ender_pearls_vanish_on_death"),
    FALL_DAMAGE("fall_damage"),
    FIRE_DAMAGE("fire_damage"),
    FORGIVE_DEAD_PLAYERS("forgive_dead_players"),
    FREEZE_DAMAGE("freeze_damage"),
    GLOBAL_SOUND_EVENTS("global_sound_events"),
    KEEP_INVENTORY("keep_inventory"),
    LAVA_SOURCE_CONVERSION("lava_source_conversion"),
    LOCATOR_BAR("locator_bar"),
    LOG_ADMIN_COMMANDS("log_admin_commands"),
    MAX_COMMAND_SEQUENCE_LENGTH("max_command_sequence_length"),
    MAX_COMMAND_FORKS("max_command_forks"),
    MAX_ENTITY_CRAMMING("max_entity_cramming"),
    MOB_EXPLOSION_DROP_DECAY("mob_explosion_drop_decay"),
    MOB_GRIEFING("mob_griefing"),
    NATURAL_HEALTH_REGENERATION("natural_health_regeneration"),
    PLAYERS_SLEEPING_PERCENTAGE("players_sleeping_percentage"),
    RANDOM_TICK_SPEED("random_tick_speed"),
    REDUCED_DEBUG_INFO("reduced_debug_info"),
    SEND_COMMAND_FEEDBACK("send_command_feedback"),
    SHOW_DEATH_MESSAGES("show_death_messages"),
    MAX_SNOW_ACCUMULATION_HEIGHT("max_snow_accumulation_height"),
    RESPAWN_RADIUS("respawn_radius"),
    SPECTATORS_GENERATE_CHUNKS("spectators_generate_chunks"),
    SPAWNER_BLOCKS_WORK("spawner_blocks_work"),
    TNT_EXPLOSION_DROP_DECAY("tnt_explosion_drop_decay"),
    UNIVERSAL_ANGER("universal_anger"),
    WATER_SOURCE_CONVERSION("water_source_conversion");

    private final String commandName;

    GameRuleId(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Returns the lower-camel-case string required by the Minecraft command syntax.
     */
    @Override
    public String toString() {
        return commandName;
    }
}