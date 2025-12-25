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

    // --- Standard Minecraft Potions ---
    /** Standard fire resistance effect. */
    FIRE_RESISTANCE,
    /** Standard invisibility effect. */
    INVISIBILITY,
    /** Luck effect (typically found in RPG elements or custom loot). */
    LUCK,
    /** Poison status effect. */
    POISON,
    /** Health regeneration effect. */
    REGENERATION,
    /** Movement speed reduction effect. */
    SLOWNESS,
    /** Attack damage increase effect. */
    STRENGTH,
    /** Attack damage reduction effect. */
    WEAKNESS,
    /** Vision enhancement in dark areas. */
    NIGHT_VISION,
    /** Jump boost and fall damage reduction. */
    LEAPING,
    /** Ability to breathe underwater. */
    WATER_BREATHING,
    /** Slows falling speed and prevents fall damage. */
    SLOW_FALLING,

    // --- Potion Name Overrides ---
    // These constants use overrides because the Potion ID differs from the Effect ID.

    /** The 'Speed' effect, which is registered as 'swiftness' in the potion registry. */
    SPEED("swiftness"),
    /** The 'Instant Health' effect, registered as 'healing' for potions. */
    INSTANT_HEALTH("healing"),
    /** The 'Instant Damage' effect, registered as 'harming' for potions. */
    INSTANT_DAMAGE("harming"),
    /** Resistance and Slowness combined. */
    TURTLE_MASTER,

    // --- 1.21 Trial Chamber Effects ---
    /** Chance to spawn Silverfish upon death. */
    INFESTED,
    /** Spawns Slimes upon death. */
    OOZING,
    /** Spawns Cobwebs upon death. */
    WEAVING,
    /** Triggers a wind burst upon death. */
    WIND_CHARGED,

    // --- Technical & Base Potions ---
    /** Base water bottle. */
    WATER,
    /** Base potion with no effects. */
    MUNDANE,
    /** Base potion with no effects. */
    THICK,
    /** The standard base for all effect-bearing potions. */
    AWKWARD,
    /** Used for the Uncraftable Potion. */
    EMPTY;

    // --- Internal Fields ---

    /** The specific path of the resource (e.g., "regeneration" or "swiftness"). */
    private final String path;

    /** The namespace owning the resource (e.g., "minecraft" or a custom datapack ID). */
    private final String namespace;

    // --- Constructors ---

    /** * Default constructor for standard Minecraft effects.
     * Uses the enum name converted to lowercase as the path.
     */
    EffectId() {
        this(DatapackConfig.MINECRAFT_NAMESPACE, null);
    }

    /** * Constructor for Minecraft effects with a specific path override.
     * @param pathOverride The literal string used in the registry (e.g., "swiftness").
     */
    EffectId(String pathOverride) {
        this(DatapackConfig.MINECRAFT_NAMESPACE, pathOverride);
    }

    /** * Comprehensive constructor for fully custom namespaces and paths.
     * <p><b>Error Catching:</b> Validates that the namespace is not null or empty.</p>
     * * @param namespace The resource namespace (e.g., "uhc_core").
     * @param path      The resource path. If null, defaults to lowercase enum name.
     * @throws NullPointerException if namespace is null.
     * @throws IllegalArgumentException if namespace is empty.
     */
    EffectId(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace for EffectId cannot be null.");

        if (namespace.trim().isEmpty()) {
            throw new IllegalArgumentException("Namespace for EffectId cannot be empty.");
        }

        this.path = (path == null || path.trim().isEmpty()) ? name().toLowerCase() : path;
    }

    // --- Public Logic Methods ---

    /** * Returns the resource path.
     * @return The path string (e.g., "strong_regeneration").
     */
    public String getPath() {
        return path;
    }

    /** * Returns the namespace.
     * @return The namespace string (usually {@link DatapackConfig#MINECRAFT_NAMESPACE}).
     */
    public String getNamespace() {
        return namespace;
    }

    /** * Combines the namespace and path into a full Resource Location.
     * @return A string formatted as "namespace:path".
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Returns the full resource location as the string representation.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}