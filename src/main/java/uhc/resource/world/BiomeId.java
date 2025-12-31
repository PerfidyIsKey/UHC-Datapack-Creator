package uhc.resource.world;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🌳 **Biome Identifier**
 * <p>
 * Represents all valid Minecraft biomes and their associated environmental data.
 * This enum maps Java constants to Minecraft resource locations and provides a
 * direct link to their native {@link DimensionId}.
 * </p>
 */
public enum BiomeId {

    // --- 🌍 Overworld Biomes ---

    THE_VOID(DimensionId.OVERWORLD),
    PLAINS(DimensionId.OVERWORLD),
    SUNFLOWER_PLAINS(DimensionId.OVERWORLD),
    SNOWY_PLAINS(DimensionId.OVERWORLD),
    ICE_SPIKES(DimensionId.OVERWORLD),
    DESERT(DimensionId.OVERWORLD),
    SWAMP(DimensionId.OVERWORLD),
    MANGROVE_SWAMP(DimensionId.OVERWORLD),
    FOREST(DimensionId.OVERWORLD),
    FLOWER_FOREST(DimensionId.OVERWORLD),
    BIRCH_FOREST(DimensionId.OVERWORLD),
    DARK_FOREST(DimensionId.OVERWORLD),
    PALE_GARDEN(DimensionId.OVERWORLD),
    OLD_GROWTH_BIRCH_FOREST(DimensionId.OVERWORLD),
    OLD_GROWTH_PINE_TAIGA(DimensionId.OVERWORLD),
    OLD_GROWTH_SPRUCE_TAIGA(DimensionId.OVERWORLD),
    TAIGA(DimensionId.OVERWORLD),
    SNOWY_TAIGA(DimensionId.OVERWORLD),
    SAVANNA(DimensionId.OVERWORLD),
    SAVANNA_PLATEAU(DimensionId.OVERWORLD),
    WINDSWEPT_HILLS(DimensionId.OVERWORLD),
    WINDSWEPT_GRAVELLY_HILLS(DimensionId.OVERWORLD),
    WINDSWEPT_FOREST(DimensionId.OVERWORLD),
    WINDSWEPT_SAVANNA(DimensionId.OVERWORLD),
    JUNGLE(DimensionId.OVERWORLD),
    SPARSE_JUNGLE(DimensionId.OVERWORLD),
    BAMBOO_JUNGLE(DimensionId.OVERWORLD),
    BADLANDS(DimensionId.OVERWORLD),
    ERODED_BADLANDS(DimensionId.OVERWORLD),
    WOODED_BADLANDS(DimensionId.OVERWORLD),
    MEADOW(DimensionId.OVERWORLD),
    CHERRY_GROVE(DimensionId.OVERWORLD),
    GROVE(DimensionId.OVERWORLD),
    SNOWY_SLOPES(DimensionId.OVERWORLD),
    FROZEN_PEAKS(DimensionId.OVERWORLD),
    JAGGED_PEAKS(DimensionId.OVERWORLD),
    STONY_PEAKS(DimensionId.OVERWORLD),
    RIVER(DimensionId.OVERWORLD),
    FROZEN_RIVER(DimensionId.OVERWORLD),
    BEACH(DimensionId.OVERWORLD),
    SNOWY_BEACH(DimensionId.OVERWORLD),
    STONY_SHORE(DimensionId.OVERWORLD),
    WARM_OCEAN(DimensionId.OVERWORLD),
    LUKEWARM_OCEAN(DimensionId.OVERWORLD),
    DEEP_LUKEWARM_OCEAN(DimensionId.OVERWORLD),
    OCEAN(DimensionId.OVERWORLD),
    DEEP_OCEAN(DimensionId.OVERWORLD),
    COLD_OCEAN(DimensionId.OVERWORLD),
    DEEP_COLD_OCEAN(DimensionId.OVERWORLD),
    FROZEN_OCEAN(DimensionId.OVERWORLD),
    DEEP_FROZEN_OCEAN(DimensionId.OVERWORLD),
    MUSHROOM_FIELDS(DimensionId.OVERWORLD),

    // --- 🕳️ Cave & Underground ---

    DRIPSTONE_CAVES(DimensionId.OVERWORLD),
    LUSH_CAVES(DimensionId.OVERWORLD),
    DEEP_DARK(DimensionId.OVERWORLD),

    // --- 👺 Nether Biomes ---

    NETHER_WASTES(DimensionId.NETHER),
    WARPED_FOREST(DimensionId.NETHER),
    CRIMSON_FOREST(DimensionId.NETHER),
    SOUL_SAND_VALLEY(DimensionId.NETHER),
    BASALT_DELTAS(DimensionId.NETHER),

    // --- 🌌 End Biomes ---

    THE_END(DimensionId.END),
    END_HIGHLANDS(DimensionId.END),
    END_MIDLANDS(DimensionId.END),
    SMALL_END_ISLANDS(DimensionId.END),
    END_BARRENS(DimensionId.END);

    // --- 📄 Fields ---

    /** * The resource namespace (e.g., "minecraft"). */
    private final String namespace;

    /** * The snake_case resource key identifier (e.g., "crimson_forest"). */
    private final String key;

    /** * The native {@link DimensionId} where this biome naturally generates. */
    private final DimensionId dimension;

    // --- 🏗️ Constructors ---

    /**
     * 🛠️ **Default Biome Constructor**
     * <p>Used for standard Minecraft biomes. Automatically assigns the default
     * namespace and converts the constant name to a lower-case resource key.</p>
     * * @param dimension The native {@link DimensionId} for this biome.
     */
    BiomeId(DimensionId dimension) {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.key = this.name().toLowerCase();
        this.dimension = Objects.requireNonNull(dimension, "Biome must be associated with a dimension.");
    }

    /**
     * 🏗️ **Custom Biome Constructor**
     * <p>Allows for custom namespaces and keys, typically used for external datapacks.</p>
     * * @param namespace The resource namespace.
     * @param key       The resource key.
     * @param dimension The native {@link DimensionId}.
     */
    BiomeId(String namespace, String key, DimensionId dimension) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null.");
        this.key = Objects.requireNonNull(key, "Key cannot be null.");
        this.dimension = Objects.requireNonNull(dimension, "Dimension cannot be null.");
    }

    // --- 🔍 Accessors ---

    /**
     * @return The {@link DimensionId} linked to this biome.
     */
    public DimensionId getDimension() {
        return dimension;
    }

    /**
     * @return The resource namespace part.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @return The snake_case resource key.
     */
    public String getKey() {
        return key;
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Generates the full Minecraft identifier.
     * @return Formatted string {@code "namespace:key"}.
     */
    @Override
    public String toString() {
        try {
            return namespace + ":" + key;
        } catch (Exception e) {
            return "minecraft:plains"; // Safe fallback
        }
    }
}