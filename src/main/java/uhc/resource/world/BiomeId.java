package uhc.resource.world;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🌳 **Biome Identifier**
 * <p>
 * Represents all valid Minecraft biomes. This enum serves as a bridge between
 * Java constants and Minecraft resource locations (namespace:key).
 * </p>
 * <p>
 * Base-game biomes utilize an empty constructor to automatically derive their properties
 * from the enum constant name, while custom entries allow for manual specification.
 * </p>
 */
public enum BiomeId {

    // --- 🌍 Overworld Biomes ---

    THE_VOID,
    PLAINS,
    SUNFLOWER_PLAINS,
    SNOWY_PLAINS,
    ICE_SPIKES,
    DESERT,
    SWAMP,
    MANGROVE_SWAMP,
    FOREST,
    FLOWER_FOREST,
    BIRCH_FOREST,
    DARK_FOREST,
    PALE_GARDEN,
    OLD_GROWTH_BIRCH_FOREST,
    OLD_GROWTH_PINE_TAIGA,
    OLD_GROWTH_SPRUCE_TAIGA,
    TAIGA,
    SNOWY_TAIGA,
    SAVANNA,
    SAVANNA_PLATEAU,
    WINDSWEPT_HILLS,
    WINDSWEPT_GRAVELLY_HILLS,
    WINDSWEPT_FOREST,
    WINDSWEPT_SAVANNA,
    JUNGLE,
    SPARSE_JUNGLE,
    BAMBOO_JUNGLE,
    BADLANDS,
    ERODED_BADLANDS,
    WOODED_BADLANDS,
    MEADOW,
    CHERRY_GROVE,
    GROVE,
    SNOWY_SLOPES,
    FROZEN_PEAKS,
    JAGGED_PEAKS,
    STONY_PEAKS,
    RIVER,
    FROZEN_RIVER,
    BEACH,
    SNOWY_BEACH,
    STONY_SHORE,
    WARM_OCEAN,
    LUKEWARM_OCEAN,
    DEEP_LUKEWARM_OCEAN,
    OCEAN,
    DEEP_OCEAN,
    COLD_OCEAN,
    DEEP_COLD_OCEAN,
    FROZEN_OCEAN,
    DEEP_FROZEN_OCEAN,
    MUSHROOM_FIELDS,

    // --- 🕳️ Cave & Underground ---

    DRIPSTONE_CAVES,
    LUSH_CAVES,
    DEEP_DARK,

    // --- 👺 Nether Biomes ---

    NETHER_WASTES,
    WARPED_FOREST,
    CRIMSON_FOREST,
    SOUL_SAND_VALLEY,
    BASALT_DELTAS,

    // --- 🌌 End Biomes ---

    THE_END,
    END_HIGHLANDS,
    END_MIDLANDS,
    SMALL_END_ISLANDS,
    END_BARRENS;

    // --- 📄 Fields ---

    /** * The resource namespace (e.g., "minecraft").
     * Primarily sourced from {@link DatapackConfig#MINECRAFT_NAMESPACE}.
     */
    private final String namespace;

    /** * The snake_case resource key identifier (e.g., "sunflower_plains").
     * Derived from the SCREAMING_SNAKE_CASE enum constant name.
     */
    private final String key;

    // --- 🏗️ Constructors ---

    /**
     * 🛠️ **Empty Default Constructor**
     * <p>Used for standard Minecraft biomes. Automatically assigns the default
     * namespace and transforms the constant name to lower-case.</p>
     */
    BiomeId() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
        this.key = this.name().toLowerCase();
    }

    /**
     * 🏗️ **Custom Biome Constructor**
     * <p>Allows for the definition of biomes with custom namespaces and keys,
     * useful for external datapacks or self-made biomes.</p>
     * * @param namespace The resource namespace (e.g., "uhc_core").
     * @param key       The resource key (e.g., "volcano_peaks").
     * @throws NullPointerException if namespace or key is null.
     */
    BiomeId(String namespace, String key) {
        this.namespace = Objects.requireNonNull(namespace, "Biome namespace cannot be null.");
        this.key = Objects.requireNonNull(key, "Biome key cannot be null.");
    }

    // --- 🛠️ Methods ---

    /**
     * Returns the full resource location string.
     * @return Formatted as {@code "namespace:key"}.
     */
    @Override
    public String toString() {
        return namespace + ":" + key;
    }

    /** * Retrieves the namespace part of the biome's resource location.
     * @return The string namespace.
     */
    public String getNamespace() {
        return namespace;
    }

    /** * Retrieves the key part of the biome's resource location.
     * @return The snake_case key.
     */
    public String getKey() {
        return key;
    }
}