package uhc.resource.enchantment;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🖋️ **Enchantment Identifier Registry**
 * <p>
 * Defines valid enchantment identifiers used in item components. By implementing
 * {@link ResourceLocation}, these constants can be used interchangeably with other
 * namespaced resources in the UHC engine.
 * </p>
 */
public enum EnchantmentId implements ResourceLocation {

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
     */
    private final String location;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **No-Argument Constructor**
     * <p>Generates a location using the default Minecraft namespace and the
     * lowercase enum name.</p>
     */
    EnchantmentId() {
        this.location = DatapackConfig.MINECRAFT_NAMESPACE + ":" + this.name().toLowerCase();
    }

    /**
     * 🟡 **Path-only Constructor**
     * <p>Used for specific Minecraft IDs that differ from the enum constant name.</p>
     * @param path The enchantment path component.
     * @throws NullPointerException if path is null.
     */
    EnchantmentId(String path) {
        Objects.requireNonNull(path, "Enchantment path cannot be null.");
        this.location = DatapackConfig.MINECRAFT_NAMESPACE + ":" + path.toLowerCase().trim();
    }

    /**
     * 🔴 **Custom Namespace Constructor**
     * <p>Supports identifiers from external or modded namespaces.</p>
     * @param namespace The custom namespace.
     * @param path The enchantment path.
     * @throws NullPointerException if any parameter is null.
     */
    EnchantmentId(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null.");
        Objects.requireNonNull(path, "Path cannot be null.");
        this.location = namespace.toLowerCase().trim() + ":" + path.toLowerCase().trim();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the full formatted resource location.
     * @return The namespaced string (e.g., "minecraft:sharpness").
     */
    @Override
    public String getResourceLocation() {
        return location;
    }

    /**
     * Validates the internal location string against Minecraft naming standards.
     * <p><b>Error Catching:</b> Inherits default validation from the interface
     * to ensure the string contains only allowed characters and exactly one colon.</p>
     * @throws IllegalStateException if the location is syntactically invalid.
     */
    @Override
    public void validate() throws IllegalStateException {
        ResourceLocation.super.validate();
    }

    // --- 📝 Overrides ---

    /**
     * Returns the enchantment identifier for direct string concatenation.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}