package uhc.resource.item.components;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🏷️ **Component ID Registry**
 * <p>
 * Defines the resource locations for Minecraft Data Components.
 * These IDs are used as keys in the 'components' NBT map of an item stack.
 * </p>
 */
public enum ComponentId {
    // --- 🛠️ Standard Components ---
    CUSTOM_NAME,
    LORE,
    ENCHANTMENTS,
    UNBREAKABLE,
    PROFILE,
    DAMAGE,
    ATTRIBUTE_MODIFIERS,
    POTION_CONTENTS,
    INSTRUMENT,
    USE_COOLDOWN;

    private final String path;
    private final String namespace;

    /**
     * Default constructor using the Enum name as the path.
     * Converts SCREAMING_SNAKE_CASE to snake_case.
     */
    ComponentId() {
        this.path = name().toLowerCase();
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Constructor allowing a custom path override.
     * @param path The specific snake_case path for the component.
     */
    ComponentId(String path) {
        this.path = Objects.requireNonNull(path);
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Full constructor for cross-namespace components (e.g., from mods or datapacks).
     */
    ComponentId(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace);
        this.path = Objects.requireNonNull(path);
    }

    /**
     * Returns the full namespaced key (e.g., "minecraft:custom_name").
     * @return The resource location string.
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}