package uhc.resource.block;

/**
 * An enumeration representing all standard and nether wood types available
 * in Minecraft Java Edition.
 * <p>
 * This class is primarily used in conjunction with {@code WoodBlockId}
 * to construct a fully qualified block resource location (e.g., {@code oak_planks}).
 */
public enum WoodType {
    OAK,
    SPRUCE,
    BIRCH,
    JUNGLE,
    ACACIA,
    DARK_OAK,
    MANGROVE,
    CHERRY,
    PALE_OAK, // Assuming this is used for blocks like stripped logs if needed
    BAMBOO,
    CRIMSON, // Nether wood type
    WARPED;  // Nether wood type

    /**
     * Overrides the default {@code toString()} method to return the lowercase
     * name of the wood type.
     * <p>
     * This is required for constructing the correct Minecraft resource location path
     * (e.g., converts {@code OAK} to the command string {@code "oak"}).
     *
     * @return The lowercase wood type name.
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}