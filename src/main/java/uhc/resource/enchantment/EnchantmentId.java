package uhc.resource.enchantment;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🖋️ **Enchantment Identifier Registry**
 * <p>
 * This enumeration defines valid enchantment resource locations used primarily
 * in the {@code enchantments} item component.
 * </p>
 * <p>
 * It supports three modes of construction: automatic path generation from
 * enum constants, custom pathing for specific Minecraft IDs, and full
 * namespaced custom IDs for modded or external content.
 * </p>
 */
public enum EnchantmentId {

    // --- ⚔️ Combat Enchantments ---

    /** Increases melee damage. */
    SHARPNESS,

    /** Sets targets on fire. */
    FIRE_ASPECT,

    /** Increases damage against aquatic mobs. */
    IMPALING,

    // --- 🏹 Ranged Enchantments ---

    /** Allows arrows/tridents to pass through entities. */
    PIERCING,

    /** Causes tridents to return to the thrower. */
    LOYALTY,

    /** Increases bow damage. */
    POWER,

    // --- 🛠️ Tool Enchantments ---

    /** Increases mining and breaking speed. */
    EFFICIENCY,

    // --- 💀 Curses ---

    /** Causes the item to disappear on player death. */
    VANISHING_CURSE;

    // --- ⚙️ State & Fields ---

    /** * The finalized namespaced identifier (e.g., "minecraft:sharpness").
     * This field is immutable to ensure consistency across the UHC engine.
     */
    private final String resourceLocation;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **No-Argument Constructor**
     * <p>Utilizes the default Minecraft namespace from {@link DatapackConfig}
     * and automatically converts the enum's name to lowercase as the path.</p>
     * <p><b>Example:</b> {@code SHARPNESS} becomes {@code "minecraft:sharpness"}.</p>
     */
    EnchantmentId() {
        this.resourceLocation = DatapackConfig.MINECRAFT_NAMESPACE + ":" + this.name().toLowerCase();
    }

    /**
     * 🟡 **Path-only Constructor**
     * <p>Used when the desired Minecraft path differs from the enum constant name.</p>
     * <p><b>Error Catching:</b> Forces lowercase on the path and trims whitespace
     * to prevent registry mismatches.</p>
     * @param path The specific enchantment path component (e.g., "protection").
     * @throws NullPointerException if the path is null.
     */
    EnchantmentId(String path) {
        Objects.requireNonNull(path, "Enchantment path cannot be null.");
        this.resourceLocation = DatapackConfig.MINECRAFT_NAMESPACE + ":" + path.toLowerCase().trim();
    }

    /**
     * 🔴 **Custom Namespace Constructor**
     * <p>Supports identifiers from external namespaces, such as modded enchantments.</p>
     * <p><b>Error Catching:</b> Validates both namespace and path for nullity
     * and enforces standard lowercase formatting.</p>
     * @param namespace The custom namespace (e.g., "cyclic").
     * @param path The enchantment path component.
     * @throws NullPointerException if any parameter is null.
     */
    EnchantmentId(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null for enchantment ID.");
        Objects.requireNonNull(path, "Path cannot be null for enchantment ID.");
        this.resourceLocation = namespace.toLowerCase().trim() + ":" + path.toLowerCase().trim();
    }

    // --- 🛰️ Accessors & Overrides ---

    /**
     * Retrieves the full, formatted namespaced identifier string.
     * @return The resource location (e.g., "minecraft:efficiency").
     */
    public String getResourceLocation() {
        return resourceLocation;
    }

    /**
     * Returns the enchantment identifier for direct use in JSON or command strings.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}