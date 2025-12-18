package uhc.resource.coordinate;

/**
 * 🏔️ **Heightmap Identifier**
 * <p>
 * Defines the different heightmap types used by Minecraft to determine the "top" of the world.
 * Used primarily in {@code /execute positioned over <heightmap>}.
 * </p>
 */
public enum HeightMap {
    /** The highest non-air block. */
    WORLD_SURFACE,

    /** The highest block that blocks motion (includes leaves, grass, and water). */
    MOTION_BLOCKING,

    /** The highest motion-blocking block, but ignores leaf blocks. */
    MOTION_BLOCKING_NO_LEAVES,

    /** The highest non-water block (the solid ground at the bottom of the sea). */
    OCEAN_FLOOR;

    /**
     * Safely retrieves a HeightMap from a string.
     * @param value The name of the heightmap (e.g., "world_surface").
     * @return The matching {@link HeightMap}, or {@link #WORLD_SURFACE} as a safe fallback.
     */
    public static HeightMap fromString(String value) {
        if (value == null) return WORLD_SURFACE;
        try {
            return valueOf(value.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Fallback to WORLD_SURFACE to ensure the command remains syntactically valid
            return WORLD_SURFACE;
        }
    }

    /**
     * Returns the lowercase name for use in Minecraft command syntax.
     * @return The heightmap name (e.g., "motion_blocking_no_leaves").
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}