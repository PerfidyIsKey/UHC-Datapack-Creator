package uhc.resource.item.components;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🏷️ **Component ID Registry**
 * <p>
 * Defines the resource locations for Minecraft Data Components (1.20.5+).
 * These IDs serve as keys within the 'components' NBT compound.
 * </p>
 */
public enum ComponentId {

    // --- ✨ Visual & Identity ---
    /** Changes the display name of the item. */
    CUSTOM_NAME,
    /** Adds additional text lines to the item tooltip. */
    LORE,
    /** Stores the player profile for Player Heads. */
    PROFILE,

    // --- ⚔️ Combat & Mechanics ---
    /** Stores the item's current damage/durability. */
    DAMAGE,
    /** Makes the item immune to durability loss. */
    UNBREAKABLE,
    /** List of enchantments and their levels. */
    ENCHANTMENTS,
    /** Modifiers for player stats (Attack Speed, Health, etc.). */
    ATTRIBUTE_MODIFIERS,

    // --- 🧪 Consumables & Utility ---
    /** Defines potion types and custom status effects. */
    POTION_CONTENTS,
    /** Data for instruments like Goat Horns. */
    INSTRUMENT,
    /** Controls item cooldowns (e.g. Ender Pearls). */
    USE_COOLDOWN,

    // --- 💾 Data & Meta ---
    /** Container for arbitrary, non-vanilla NBT data. */
    CUSTOM_DATA;

    private final String path;
    private final String namespace;

    /**
     * Default constructor. Converts Enum name to snake_case.
     */
    ComponentId() {
        this.path = name().toLowerCase();
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Constructor allowing a custom path override within the Minecraft namespace.
     */
    ComponentId(String path) {
        this.path = Objects.requireNonNull(path);
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Full constructor for custom namespaces.
     */
    ComponentId(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace);
        this.path = Objects.requireNonNull(path);
    }

    /**
     * Returns the full namespaced key (e.g., "minecraft:custom_data").
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}