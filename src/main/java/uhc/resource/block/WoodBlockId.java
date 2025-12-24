package uhc.resource.block;

/**
 * Defines a set of Minecraft blocks whose final resource location (ID) is dynamically
 * determined by prefixing the block name with a {@link WoodType}.
 * <p>
 * Example: PLANKS + WoodType.OAK results in 'minecraft:oak_planks'.
 */
public enum WoodBlockId implements BlockId {

    /** Represents blocks like 'oak_planks', 'spruce_planks', etc. */
    PLANKS,

    /** Represents blocks like 'birch_wall_sign', 'jungle_wall_sign', etc. */
    WALL_SIGN;

    private String resourceLocation;
    private final String namespace;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Enum constructor for cases where a specific namespace is needed.
     * @param namespace The namespace for the block ID.
     */
    WoodBlockId(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Default enum constructor, using the standard Minecraft namespace.
     */
    WoodBlockId() {
        this.namespace = DEFAULT_NAMESPACE;
    }

    /**
     * Constructs the full, wood-type-specific resource location string for the block.
     * This is the primary method for using a WoodBlockId.
     *
     * @param type The required wood type to prefix the block name (e.g., OAK).
     * @return The complete resource location string (e.g., 'minecraft:oak_planks').
     * @throws IllegalArgumentException if the provided {@code type} is null.
     */
    public WoodBlockId withWoodType(WoodType type) {
        if (type == null) {
            throw new IllegalArgumentException("WoodType cannot be null for a WoodBlockId.");
        }

        // 1. Convert the enum constant name to lowercase
        String blockNameComponent = this.name().toLowerCase();

        // 2. Build the final resource string: namespace:woodType_blockName
        // Assumes WoodType.toString() returns the lowercase type name (e.g., "oak")
        this.resourceLocation = namespace + ":" + type.toString().toLowerCase() + "_" + blockNameComponent;
        return this;
    }

    /**
     * **Unsupported Operation:** A wood block ID is meaningless without specifying a wood type.
     * This method is included only to satisfy the {@link BlockId} contract.
     *
     * @throws UnsupportedOperationException Always, to force the user to use {@code withWoodType(WoodType type)}.
     */
    @Override
    public String getResourceLocation() {
        if (resourceLocation == null) {
            throw new UnsupportedOperationException("A WoodBlockId is incomplete without a specified wood type. Use the withWoodType(WoodType type) method instead.");
        }

        return resourceLocation;
    }

    /**
     * @throws UnsupportedOperationException Always, as a wood type is required.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}