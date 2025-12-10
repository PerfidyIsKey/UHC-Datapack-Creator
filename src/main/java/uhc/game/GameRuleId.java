package uhc.game;

// https://minecraft.fandom.com/wiki/Game_rule
public enum GameRuleId {
    ANNOUNCE_ADVANCEMENTS("announceAdvancements"),
    BLOCK_EXPLOSION_DROP_DECAY("blockExplosionDropDecay"),
    COMMAND_BLOCK_OUTPUT("commandBlockOutput"),
    COMMAND_MODIFICATION_BLOCK_LIMIT("commandModificationBlockLimit"),
    DISABLE_ELYTRA_MOVEMENT_CHECK("disableElytraMovementCheck"),
    DISABLE_RAIDS("disableRaids"),
    DO_DAYLIGHT_CYCLE("doDaylightCycle"),
    DO_ENTITY_DROPS("doEntityDrops"),
    DO_FIRE_TICK("doFireTick"),
    DO_INSOMNIA("doInsomnia"),
    DO_IMMEDIATE_RESPAWN("doImmediateRespawn"),
    DO_LIMITED_CRAFTING("doLimitedCrafting"),
    DO_MOB_LOOT("doMobLoot"),
    DO_MOB_SPAWNING("doMobSpawning"),
    DO_PATROL_SPAWNING("doPatrolSpawning"),
    DO_TILE_DROPS("doTileDrops"),
    DO_TRADER_SPAWNING("doTraderSpawning"),
    DO_VINES_SPREAD("doVinesSpread"),
    DO_WEATHER_CYCLE("doWeatherCycle"),
    DO_WARDEN_SPAWNING("doWardenSpawning"),
    DROWNING_DAMAGE("drowningDamage"),
    ENDER_PEARLS_VANISH_ON_DEATH("enderPearlsVanishOnDeath"),
    FALL_DAMAGE("fallDamage"),
    FIRE_DAMAGE("fireDamage"),
    FORGIVE_DEAD_PLAYERS("forgiveDeadPlayers"),
    FREEZE_DAMAGE("freezeDamage"),
    GLOBAL_SOUND_EVENTS("globalSoundEvents"),
    KEEP_INVENTORY("keepInventory"),
    LAVA_SOURCE_CONVERSION("lavaSourceConversion"),
    LOCATOR_BAR("locatorBar"),
    LOG_ADMIN_COMMANDS("logAdminCommands"),
    MAX_COMMAND_CHAIN_LENGTH("maxCommandChainLength"),
    MAX_ENTITY_CRAMMING("maxEntityCramming"),
    MOB_EXPLOSION_DROP_DECAY("mobExplosionDropDecay"),
    MOB_GRIEFING("mobGriefing"),
    NATURAL_REGENERATION("naturalRegeneration"),
    PLAYERS_SLEEPING_PERCENTAGE("playersSleepingPercentage"),
    RANDOM_TICK_SPEED("randomTickSpeed"),
    REDUCED_DEBUG_INFO("reducedDebugInfo"),
    SEND_COMMAND_FEEDBACK("sendCommandFeedback"),
    SHOW_DEATH_MESSAGES("showDeathMessages"),
    SNOW_ACCUMULATION_HEIGHT("snowAccumulationHeight"),
    SPAWN_RADIUS("spawnRadius"),
    SPECTATORS_GENERATE_CHUNKS("spectatorsGenerateChunks"),
    TNT_EXPLOSION_DROP_DECAY("tntExplosionDropDecay"),
    UNIVERSAL_ANGER("universalAnger"),
    WATER_SOURCE_CONVERSION("waterSourceConversion");

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