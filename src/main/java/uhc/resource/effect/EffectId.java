package uhc.resource.effect;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🧪 **Effect and Potion Registry ID Mapper**
 * <p>
 * This enum serves as the central registry for status effects and potion identifiers.
 * It maps high-level constants to their specific Minecraft resource locations,
 * handling the discrepancy between effect names (e.g., Speed) and potion names (e.g., Swiftness).
 * </p>
 */
public enum EffectId {

    // --- Standard Obtainable Potions ---

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

    // --- Potion Name Overrides ---

    /** The 'Speed' effect, registered as 'swiftness' in the potion registry. */
    SPEED("swiftness"),
    /** The 'Instant Health' effect, registered as 'healing' for potions. */
    INSTANT_HEALTH("healing"),
    /** The 'Instant Damage' effect, registered as 'harming' for potions. */
    INSTANT_DAMAGE("harming"),
    /** Provides Resistance IV and Slowness IV (Master of the Turtle). */
    TURTLE_MASTER,

    // --- Combat & Utility (Unobtainable via standard potions) ---

    /** Increases mining speed and attack speed. */
    HASTE,
    /** Decreases mining speed and attack speed. */
    MINING_FATIGUE,
    /** Standard jump height increase (Effect ID variant). */
    JUMP_BOOST,
    /** Reduces all incoming damage by 20% per level. */
    RESISTANCE,
    /** Increases the maximum health pool of the entity. */
    HEALTH_BOOST,
    /** Grants temporary "yellow" hearts that cannot be regenerated. */
    ABSORPTION,
    /** Instantly restores hunger and saturation points. */
    SATURATION,
    /** Significantly increases swimming speed. */
    DOLPHINS_GRACE,
    /** Underwater night vision, mining speed, and infinite oxygen. */
    CONDUIT_POWER,
    /** Outlines the entity with a glowing border visible through blocks. */
    GLOWING,

    // --- Negative & Environmental (Unobtainable via standard potions) ---

    /** Distorts the player's vision and wobbles the screen. */
    NAUSEA,
    /** Heavily restricts vision with black fog; prevents sprinting. */
    BLINDNESS,
    /** Depletes the hunger bar rapidly. */
    HUNGER,
    /** Deals lethal damage over time; can kill the entity. */
    WITHER,
    /** Forces the entity to float upwards at a constant rate. */
    LEVITATION,
    /** Periodically pulses the screen to black; limits visibility. */
    DARKNESS,
    /** Decreases the quality of loot from fishing and chests. */
    BAD_LUCK("unluck"),
    /** Freezes an entity's oxygen bar. */
    BREATH_OF_THE_NAUTILUS,

    // --- Ominous & World Events ---

    /** Triggers a Raid upon entering a village (Pre-1.21). */
    BAD_OMEN,
    /** Converts standard Trial Spawners into Ominous Trial Spawners. */
    TRIAL_OMEN,
    /** Triggers a Raid specifically at the player's location. */
    RAID_OMEN,
    /** Grants massive trade discounts from villagers. */
    HERO_OF_THE_VILLAGE,

    // --- 1.21 Trial Chamber Effects ---

    /** 10% chance to spawn 1-3 Silverfish when the entity takes damage. */
    INFESTED,
    /** Spawns 2 Slimes upon the entity's death. */
    OOZING,
    /** Decreases movement in cobwebs; spreads cobwebs upon death. */
    WEAVING,
    /** Emits a wind burst (knockback) upon the entity's death. */
    WIND_CHARGED,

    // --- Technical & Base Potions ---

    /** Standard Water Bottle. */
    WATER,
    /** No-effect base potion (Redstone/Glowstone failure). */
    MUNDANE,
    /** No-effect base potion (Glowstone base). */
    THICK,
    /** The mandatory base for almost all primary potions. */
    AWKWARD,
    /** Represents an empty or invalid potion state. */
    EMPTY;

    // --- Private Fields ---

    /** The unique registry path for this effect (e.g., "swiftness"). */
    private final String path;

    /** The registry namespace (usually {@code DatapackConfig.MINECRAFT_NAMESPACE}). */
    private final String namespace;

    // --- Constructors ---

    /**
     * Default constructor for standard Minecraft effects.
     * Uses the lowercase version of the Enum constant name as the path.
     */
    EffectId() {
        this(DatapackConfig.MINECRAFT_NAMESPACE, null);
    }

    /**
     * Constructor for effects where the registry path differs from the enum name.
     * @param pathOverride The literal string path (e.g., "unluck").
     */
    EffectId(String pathOverride) {
        this(DatapackConfig.MINECRAFT_NAMESPACE, pathOverride);
    }

    /**
     * Comprehensive constructor for full registry identification.
     * * @param namespace The resource namespace (e.g., "minecraft").
     * @param path      The resource path. If null, defaults to lowercase enum name.
     * @throws NullPointerException if namespace is null.
     * @throws IllegalArgumentException if namespace is blank.
     */
    EffectId(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace for EffectId cannot be null.");

        if (namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace for EffectId cannot be empty.");
        }

        // Error Catching: Ensure the path is valid. Replace potential spaces with underscores.
        String resolvedPath = (path == null || path.trim().isEmpty()) ? name().toLowerCase() : path.toLowerCase();
        this.path = resolvedPath.replace(" ", "_");
    }

    // --- Public API ---

    /**
     * Retrieves the specific path used in the registry.
     * @return The path string (e.g., "swiftness").
     */
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the owning namespace.
     * @return The namespace string (usually "minecraft").
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Combines the namespace and path into a Resource Location string.
     * @return The full identifier (e.g., "minecraft:regeneration").
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Returns the full resource location for logging and debugging.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}