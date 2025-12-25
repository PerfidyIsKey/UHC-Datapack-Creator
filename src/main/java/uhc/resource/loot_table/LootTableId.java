package uhc.resource.loot_table;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 📜 **Loot Table Registry**
 * <p>
 * Defines the resource locations for Loot Tables used in NBT data.
 * This enum ensures that namespaces and paths are formatted correctly
 * for Minecraft's NBT parser.
 * </p>
 * <p>
 * <b>Usage:</b> {@code ChestNBT.create().lootTable(LootTableId.CHESTS_ABANDONED_MINESHAFT)}
 * </p>
 */
public enum LootTableId {

    // --- 🏆 Custom UHC Tables ---
    SUPPLY_DROP(DatapackConfig.CUSTOM_NAMESPACE),
    STARTER_KIT(DatapackConfig.CUSTOM_NAMESPACE),

    // --- 🏛️ World Generation (Chests) ---
    CHESTS_ABANDONED_MINESHAFT,
    CHESTS_BURIED_TREASURE,
    CHESTS_DESERT_PYRAMID,
    CHESTS_END_CITY_TREASURE,
    CHESTS_JUNGLE_TEMPLE,
    CHESTS_NETHER_BRIDGE,
    CHESTS_PILLAGER_OUTPOST,
    CHESTS_SHIPWRECK_TREASURE,
    CHESTS_SIMPLE_DUNGEON,
    CHESTS_STRONGHOLD_CORRIDOR,
    CHESTS_VILLAGE_WEAPONSMITH,

    // --- ⚔️ Gameplay & Entities ---
    ENTITIES_SHEEP,
    FISHING_FISH;

    private final String namespace;

    /**
     * Constructor for custom namespaces (usually for datapacks).
     * @param namespace The namespace to prefix (e.g., "uhc").
     */
    LootTableId(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null.");
    }

    /**
     * Default constructor for standard Minecraft loot tables.
     * Prefixes with {@link DatapackConfig#MINECRAFT_NAMESPACE}.
     */
    LootTableId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Returns the full NBT-compliant resource location.
     * <p>
     * <b>Catch:</b> Converts underscores to slashes for base-game parity
     * (e.g., CHESTS_DESERT_PYRAMID -> minecraft:chests/desert_pyramid).
     * </p>
     * @return The formatted resource location string.
     */
    public String getResourceLocation() {
        // Validation: Ensure namespace isn't empty (avoids ":path" errors)
        String ns = namespace.isEmpty() ? DatapackConfig.MINECRAFT_NAMESPACE : namespace;

        // Minecraft uses slashes for paths. We replace underscores in the enum name
        // to match the directory structure of loot tables.
        String path = name().toLowerCase().replace("_", "/");

        return ns + ":" + path;
    }

    /**
     * @return The resource location (convenience for string concatenation).
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}