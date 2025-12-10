package uhc.resource.block;

import uhc.resource.BlockId;
import uhc.text.DyeColor;

/**
 * Defines a set of Minecraft blocks whose final resource location (ID) is dynamically
 * determined by prefixing the block name with a {@link DyeColor}.
 * <p>
 * Example: CONCRETE_POWDER + DyeColor.BLUE results in 'minecraft:blue_concrete_powder'.
 */
public enum ColorableBlockId implements BlockId {

    /** Represents blocks like 'white_stained_glass'. */
    STAINED_GLASS,

    /** Represents blocks like 'red_concrete_powder'. */
    CONCRETE_POWDER;

    private String resourceLocation;
    private final String namespace;
    private static final String DEFAULT_NAMESPACE = "minecraft";

    /**
     * Enum constructor for cases where a specific namespace is needed.
     * @param namespace The namespace for the block ID.
     */
    ColorableBlockId(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Default enum constructor, using the standard Minecraft namespace.
     */
    ColorableBlockId() {
        this.namespace = DEFAULT_NAMESPACE;
    }

    /**
     * Constructs the full, color-specific resource location string for the block.
     * This is the primary method for using a ColorableBlockId.
     * <p>
     * Error Handling: This method assumes {@code color} is not null.
     *
     * @param color The required color to prefix the block name (e.g., BLUE).
     * @return The complete resource location string (e.g., 'minecraft:blue_stained_glass').
     * @throws IllegalArgumentException if the provided {@code color} is null.
     */
    public ColorableBlockId withColor(DyeColor color) {
        if (color == null) {
            throw new IllegalArgumentException("DyeColor cannot be null for a ColorableBlockId.");
        }

        // 1. Convert the enum constant name to lowercase
        // This is necessary if the enum names do not exactly match the block name component
        String blockNameComponent = this.name().toLowerCase();

        // 2. Build the final resource string: namespace:color_blockName
        // Assumes DyeColor.toString() returns the lowercase color name (e.g., "blue")
        this.resourceLocation = namespace + ":" + color.toString().toLowerCase() + "_" + blockNameComponent;
        return this;
    }

    /**
     * **ERROR/WARNING:** This implementation of getResourceLocation() is not meaningful
     * for a ColorableBlockId as the block ID is incomplete without a color.
     * <p>
     * It is included to satisfy the {@link BlockId} contract, but calling it will throw an exception
     * to force the user to use the required {@code withColor(DyeColor color)} method.
     *
     * @throws UnsupportedOperationException Always, as a color is required.
     */
    @Override
    public String getResourceLocation() {
        if (resourceLocation == null) {
            throw new UnsupportedOperationException("A colorable block ID is incomplete without specifying a color. Use the withColor(DyeColor color) method instead.");
        }
        return resourceLocation;
    }

    /**
     * @throws UnsupportedOperationException Always, as a color is required.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}