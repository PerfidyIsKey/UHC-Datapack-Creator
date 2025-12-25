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

    /** Increases melee damage. Maps to {@code minecraft:sharpness}. */
    SHARPNESS,

    /** Sets targets on fire. Maps to {@code minecraft:fire_aspect}. */
    FIRE_ASPECT,

    /** Increases damage against aquatic mobs. Maps to {@code minecraft:impaling}. */
    IMPALING,

    // --- 🏹 Ranged Enchantments ---

    /** Allows arrows/tridents to pass through entities. Maps to {@code minecraft:piercing}. */
    PIERCING,

    /** Causes tridents to return to the thrower. Maps to {@code minecraft:loyalty}. */
    LOYALTY,

    /** Increases bow damage. Maps to {@code minecraft:power}. */
    POWER,

    // --- 🛠️ Tool Enchantments ---

    /** Increases mining and breaking speed. Maps to {@code minecraft:efficiency}. */
    EFFICIENCY,

    // --- 💀 Curses ---

    /** Causes the item to disappear on player death. Maps to {@code minecraft:vanishing_curse}. */
    VANISHING_CURSE;

    // --- ⚙️ State & Fields ---

    /** * The namespace part of the resource location (e.g., "minecraft"). */
    private final String namespace;

    /** * The path part of the resource location (e.g., "sharpness"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Item Constructor**
     * <p>Automatically uses the vanilla namespace and converts the enum name
     * to a lowercase path.</p>
     */
    EnchantmentId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = this.name().toLowerCase();
    }

    /**
     * 🟡 **Path-only Constructor**
     * <p>Used for specific Minecraft IDs that might differ from the enum constant name.</p>
     * @param path The enchantment path component.
     * @throws NullPointerException if path is null.
     */
    EnchantmentId(String path) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.path = Objects.requireNonNull(path, "Enchantment path cannot be null.").toLowerCase().trim();
        this.validate();
    }

    /**
     * 🔴 **Full Custom Constructor**
     * <p>Allows for a fully unique path and namespace combination (e.g., for mods).</p>
     * @param namespace The resource namespace.
     * @param path      The specific enchantment path.
     * @throws NullPointerException if path or namespace is null.
     */
    EnchantmentId(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase().trim();
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase().trim();
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace part of the identifier.
     * @return The assigned namespace string.
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path part of the identifier.
     * @return The lowercase path string.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the full formatted resource location.
     * @return The namespaced string (e.g., "minecraft:sharpness").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
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