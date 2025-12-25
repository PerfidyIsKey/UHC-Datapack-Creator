package uhc.resource.effect;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🧪 **Comprehensive Effect and Potion Registry ID Mapper**
 * <p>
 * This registry manages identifiers for all Minecraft status effects and potion registry entries.
 * It provides a dual-path mapping system to resolve discrepancies where the command name
 * (e.g., {@code speed}) differs from the potion registry name (e.g., {@code swiftness}).
 * </p>
 */
public enum EffectId implements ResourceLocation {

    // --- 🛡️ Standard Obtainable Potions ---

    /** Fire Resistance: Grants immunity to fire, lava, and magma damage. */
    FIRE_RESISTANCE,
    /** Invisibility: Renders the entity model invisible to others. */
    INVISIBILITY,
    /** Luck: Increases the quality of loot tables (fishing, chests). */
    LUCK,
    /** Poison: Deals periodic damage; cannot kill the entity. */
    POISON,
    /** Regeneration: Gradually restores health points over time. */
    REGENERATION,
    /** Slowness: Reduces movement speed and field of view. */
    SLOWNESS,
    /** Strength: Increases melee attack damage dealt by the entity. */
    STRENGTH,
    /** Weakness: Decreases melee attack damage dealt by the entity. */
    WEAKNESS,
    /** Night Vision: Allows clear vision in darkness and underwater. */
    NIGHT_VISION,
    /** Leaping: Increases jump height and reduces fall damage. */
    LEAPING,
    /** Water Breathing: Prevents the oxygen bar from depleting. */
    WATER_BREATHING,
    /** Slow Falling: Negates fall damage and reduces descent speed. */
    SLOW_FALLING,

    // --- 🌀 Potion Name Overrides ---

    /** ⚡ Speed / Swiftness: Effect registry: {@code speed}, Potion registry: {@code swiftness}. */
    SPEED("speed", "swiftness"),
    /** ❤️ Instant Health / Healing: Effect registry: {@code instant_health}, Potion registry: {@code healing}. */
    INSTANT_HEALTH("instant_health", "healing"),
    /** 💔 Instant Damage / Harming: Effect registry: {@code instant_damage}, Potion registry: {@code harming}. */
    INSTANT_DAMAGE("instant_damage", "harming"),
    /** 🐢 Turtle Master: Grants massive resistance but heavy slowness. */
    TURTLE_MASTER,

    // --- ⚔️ Combat & Utility ---

    /** Haste: Increases mining speed and melee attack speed. */
    HASTE,
    /** Mining Fatigue: Drastically slows mining and attack speed. */
    MINING_FATIGUE,
    /** Jump Boost: Increases jump height (command variant of Leaping). */
    JUMP_BOOST,
    /** Resistance: Reduces all incoming damage by a percentage. */
    RESISTANCE,
    /** Health Boost: Adds extra maximum health heart containers. */
    HEALTH_BOOST,
    /** Absorption: Grants temporary bonus "yellow" hearts. */
    ABSORPTION,
    /** Saturation: Instantly restores hunger and saturation points. */
    SATURATION,
    /** Dolphin's Grace: Significantly increases swimming speed. */
    DOLPHINS_GRACE,
    /** Conduit Power: Provides vision, mining speed, and oxygen underwater. */
    CONDUIT_POWER,
    /** Glowing: Highlights the entity's outline through blocks. */
    GLOWING,

    // --- 💀 Negative & Environmental ---

    /** Nausea: Distorts the camera and wobbles the screen. */
    NAUSEA,
    /** Blindness: Severely restricts vision and prevents sprinting. */
    BLINDNESS,
    /** Hunger: Causes the food bar to deplete more rapidly. */
    HUNGER,
    /** Wither: Deals periodic lethal damage over time. */
    WITHER,
    /** Levitation: Forces the entity to float upwards. */
    LEVITATION,
    /** Darkness: Periodically dims the screen to total blackness. */
    DARKNESS,
    /** 🍀 Bad Luck / Unluck: Effect registry: {@code bad_luck}, Potion registry: {@code unluck}. */
    BAD_LUCK("bad_luck", "unluck"),
    /** Breath of the Nautilus: Prevents oxygen depletion (technical variant). */
    BREATH_OF_THE_NAUTILUS,

    // --- 🚩 Ominous & World Events ---

    /** Bad Omen: Triggers a Raid when entering a village. */
    BAD_OMEN,
    /** Trial Omen: Converts Trial Spawners into Ominous variants. */
    TRIAL_OMEN,
    /** Raid Omen: Triggers a localized Raid at current coordinates. */
    RAID_OMEN,
    /** Hero of the Village: Grants trade discounts from Villagers. */
    HERO_OF_THE_VILLAGE,

    // --- 🛡️ 1.21 Trial Chamber Effects ---

    /** Infested: Spawns Silverfish when taking damage. */
    INFESTED,
    /** Oozing: Spawns Slimes upon the entity's death. */
    OOZING,
    /** Weaving: Spreads cobwebs upon the entity's death. */
    WEAVING,
    /** Wind Charged: Emits a wind burst upon the entity's death. */
    WIND_CHARGED,

    // --- ⚗️ Technical & Base Potions ---

    /** Water Bottle: The base for all brewed potions. */
    WATER,
    /** Mundane Potion: A no-effect base potion variant. */
    MUNDANE,
    /** Thick Potion: A no-effect base potion variant (Glowstone). */
    THICK,
    /** Awkward Potion: The primary base for effect-bearing potions. */
    AWKWARD,
    /** Empty: Represents an invalid or missing potion state. */
    EMPTY;

    // --- ⚙️ State & Fields ---

    /** * The registry namespace (usually {@code "minecraft"}). */
    private final String namespace;

    /** * The path used for {@code /effect} commands and status registry. */
    private final String effectPath;

    /** * The path used for potion registry NBT (e.g., in {@code potion_contents}). */
    private final String potionPath;

    // --- 🏗️ Constructors ---

    /**
     * Default constructor for effects where registry names are identical.
     * <p>Automatically converts the enum name to lowercase for both paths.</p>
     */
    EffectId() {
        this(null, null);
    }

    /**
     * Specialized constructor for effects with divergent registry paths.
     * <p><b>Error Catching:</b> Sanitizes input by trimming whitespace and
     * converting to lowercase to prevent ResourceLocation violations.</p>
     * @param effectPath The path for status effects (e.g. "speed").
     * @param potionPath The path for potion items (e.g. "swiftness").
     */
    EffectId(String effectPath, String potionPath) {
        // Namespace is centralized to maintain consistency across the project
        this.namespace = Objects.requireNonNull(DatapackConfig.MINECRAFT_NAMESPACE, "Namespace config is missing.");

        // Resolve paths: default to lowercase enum name if null is passed
        this.effectPath = (effectPath == null || effectPath.isBlank())
                ? this.name().toLowerCase()
                : effectPath.toLowerCase().trim();

        this.potionPath = (potionPath == null || potionPath.isBlank())
                ? this.name().toLowerCase()
                : potionPath.toLowerCase().trim();

        // Fail-fast validation during class loading
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /** @return The registry namespace. */
    @Override public String getNamespace() { return namespace; }

    /** @return The status effect registry path. */
    @Override public String getPath() { return effectPath; }

    /** @return The potion registry path. */
    public String getPotionPath() { return potionPath; }

    /** @return Combined identifier (e.g., {@code "minecraft:speed"}). */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + effectPath;
    }

    /**
     * **Proactive Error Catching**
     * <p>Validates that no components are null and the final string matches
     * Minecraft's allowed character patterns (a-z, 0-9, _, -, .).</p>
     * @throws IllegalStateException if the ResourceLocation is syntactically invalid.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (namespace == null || effectPath == null || potionPath == null) {
            throw new IllegalStateException("EffectId components cannot be null for constant: " + this.name());
        }

        // Checks against the VALID_PATTERN inherited from ResourceLocation interface
        if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
            throw new IllegalStateException("Malformed ResourceLocation in EffectId: " + getResourceLocation());
        }
    }

    // --- 📝 Overrides ---

    /** @return The full resource location string. */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}