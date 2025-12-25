package uhc.resource.effect;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🧪 **Effect and Potion Registry ID Mapper**
 * <p>
 * This registry contains status effects and potion identifiers. It implements
 * {@link ResourceLocation} to provide a unified way to reference effects in
 * commands ({@code /effect}) and potion NBT data.
 * </p>
 * <p>
 * It handles the discrepancies between internal effect names and their
 * potion registry counterparts (e.g., the {@code SPEED} effect is
 * registered as {@code swiftness} for potions).
 * </p>
 */
public enum EffectId implements ResourceLocation {

    // --- 🛡️ Standard Obtainable Potions ---

    /** Prevents the entity from taking damage from fire, lava, and magma. */
    FIRE_RESISTANCE,
    /** Makes the entity invisible; armor and held items remain visible. */
    INVISIBILITY,
    /** Increases the quality of loot from fishing and certain chests. */
    LUCK,
    /** Deals damage over time; cannot reduce health below 1.5 hearts. */
    POISON,
    /** Slowly restores health over time. */
    REGENERATION,
    /** Reduces movement speed and field of view. */
    SLOWNESS,
    /** Increases melee damage dealt by the entity. */
    STRENGTH,
    /** Decreases melee damage dealt by the entity. */
    WEAKNESS,
    /** Allows the player to see in total darkness and underwater. */
    NIGHT_VISION,
    /** Increases jump height and reduces fall damage. */
    LEAPING,
    /** Prevents the oxygen bar from depleting underwater. */
    WATER_BREATHING,
    /** Reduces falling speed and negates all fall damage. */
    SLOW_FALLING,

    // --- 🌀 Potion Name Overrides ---

    /** Registered as {@code swiftness} in potion NBT. Mapping: {@code SPEED -> swiftness}. */
    SPEED("swiftness"),
    /** Registered as {@code healing} in potion NBT. Mapping: {@code INSTANT_HEALTH -> healing}. */
    INSTANT_HEALTH("healing"),
    /** Registered as {@code harming} in potion NBT. Mapping: {@code INSTANT_DAMAGE -> harming}. */
    INSTANT_DAMAGE("harming"),
    /** Grants Resistance IV and Slowness IV. */
    TURTLE_MASTER,

    // --- ⚔️ Combat & Utility ---

    /** Increases mining and attack speed. */
    HASTE,
    /** Decreases mining and attack speed. */
    MINING_FATIGUE,
    /** Increases jump height. */
    JUMP_BOOST,
    /** Reduces all incoming damage. */
    RESISTANCE,
    /** Increases the maximum health pool. */
    HEALTH_BOOST,
    /** Grants temporary "yellow" hearts. */
    ABSORPTION,
    /** Instantly restores hunger and saturation. */
    SATURATION,
    /** Increases swimming speed significantly. */
    DOLPHINS_GRACE,
    /** Provides underwater vision, mining speed, and oxygen. */
    CONDUIT_POWER,
    /** Outlines the entity with a glowing border. */
    GLOWING,

    // --- 💀 Negative & Environmental ---

    /** Distorts vision and wobbles the screen. */
    NAUSEA,
    /** Restricts vision and prevents sprinting. */
    BLINDNESS,
    /** Depletes the hunger bar rapidly. */
    HUNGER,
    /** Deals lethal damage over time. */
    WITHER,
    /** Forces the entity to float upwards. */
    LEVITATION,
    /** Periodically pulses the screen to black. */
    DARKNESS,
    /** Decreases loot quality. Registered as {@code unluck}. */
    BAD_LUCK("unluck"),
    /** Freezes an entity's oxygen bar. */
    BREATH_OF_THE_NAUTILUS,

    // --- 🚩 Ominous & World Events ---

    /** Triggers a Raid upon entering a village. */
    BAD_OMEN,
    /** Converts Trial Spawners into Ominous variants. */
    TRIAL_OMEN,
    /** Triggers a localized Raid. */
    RAID_OMEN,
    /** Grants massive trade discounts from villagers. */
    HERO_OF_THE_VILLAGE,

    // --- 🛡️ 1.21 Trial Chamber Effects ---

    /** Spawns Silverfish when taking damage. */
    INFESTED,
    /** Spawns Slimes upon death. */
    OOZING,
    /** Spreads cobwebs upon death. */
    WEAVING,
    /** Emits a wind burst upon death. */
    WIND_CHARGED,

    // --- ⚗️ Technical & Base Potions ---

    /** Standard Water Bottle base. */
    WATER,
    /** No-effect base potion. */
    MUNDANE,
    /** No-effect base potion (Glowstone). */
    THICK,
    /** Mandatory base for most primary potions. */
    AWKWARD,
    /** Represents an empty or invalid potion state. */
    EMPTY;

    // --- ⚙️ Internal State ---

    /** The registry namespace (e.g., "minecraft"). */
    private final String namespace;

    /** The unique registry path (e.g., "swiftness"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Minecraft Constructor**
     * <p>Uses the lowercase enum name as the path and the standard
     * namespace from {@link DatapackConfig}.</p>
     */
    EffectId() {
        this(DatapackConfig.MINECRAFT_NAMESPACE, null);
    }

    /**
     * 🟡 **Custom Path Constructor**
     * <p>Used when the Minecraft registry name differs from the Enum constant name.</p>
     * @param pathOverride The literal string path (e.g., "unluck").
     */
    EffectId(String pathOverride) {
        this(DatapackConfig.MINECRAFT_NAMESPACE, pathOverride);
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Provides complete control over namespace and path. Performs sanitization and validation.</p>
     * <p><b>Error Catching:</b> Validates for nulls and blank namespaces. Replaces illegal spaces
     * in the path with underscores and converts to lowercase.</p>
     * @param namespace The resource namespace.
     * @param path      The resource path (defaults to lowercase name if null).
     * @throws NullPointerException if namespace is null.
     * @throws IllegalStateException if the final ResourceLocation is malformed.
     */
    EffectId(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase().trim();

        if (this.namespace.isEmpty()) {
            throw new IllegalStateException("Namespace cannot be empty.");
        }

        // Resolve path and sanitize for illegal characters/spaces
        String resolvedPath = (path == null || path.isBlank()) ? this.name().toLowerCase() : path.toLowerCase().trim();
        this.path = resolvedPath.replace(" ", "_");

        // Validate the final identifier against the ResourceLocation pattern
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace associated with this effect.
     * @return The namespace string (e.g., "minecraft").
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path associated with this effect.
     * @return The path string (e.g., "swiftness").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Combines the namespace and path into a valid identifier.
     * @return The full identifier string (e.g., "minecraft:swiftness").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Performs a syntax check on the identifier.
     * <p><b>Error Catching:</b> Uses {@code VALID_PATTERN} from the interface to ensure
     * no illegal characters (caps, spaces, etc.) are present in the final key.</p>
     * @throws IllegalStateException if the identifier is malformed.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
            throw new IllegalStateException("Malformed EffectId: " + getResourceLocation());
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the full resource location as a string.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}