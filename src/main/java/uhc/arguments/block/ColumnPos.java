package uhc.arguments.block;

/**
 * Represents a 2D chunk coordinate argument (X, Z) for Minecraft commands,
 * typically used in commands like {@code /forceload}.
 * <p>
 * Supports absolute (e.g., 100) and relative (~, e.g., ~-5) coordinates.
 * The final output format is always "X Z" separated by a space.
 */
public class ColumnPos {

    private final String x;
    private final String z;

    // Regex for robust validation of Minecraft 2D coordinates.
    // Matches:
    // 1. Absolute integers: (-?\d+) -> 100, -5
    // 2. Relative integers: (~-?\d+) -> ~5, ~-10
    // 3. Relative zero offset: (~) -> ~
    private static final String COORD_REGEX = "^(~|~?-?\\d+)$";

    /**
     * Private constructor that enforces coordinate validity and checks for null inputs.
     *
     * @param x The X-coordinate string (e.g., "100", "~5").
     * @param z The Z-coordinate string.
     * @throws IllegalArgumentException if any coordinate is null or fails validation.
     */
    private ColumnPos(String x, String z) {
        if (x == null || z == null) {
            throw new IllegalArgumentException("ColumnPos coordinates (x, z) cannot be null.");
        }

        // Validate each coordinate string immediately
        if (!isValidCoordinate(x)) {
            throw new IllegalArgumentException("Invalid coordinate format for X: " + x);
        }
        if (!isValidCoordinate(z)) {
            throw new IllegalArgumentException("Invalid coordinate format for Z: " + z);
        }

        this.x = x;
        this.z = z;
    }

    /**
     * Creates a ColumnPos using absolute integer coordinates.
     *
     * @param x The absolute X coordinate.
     * @param z The absolute Z coordinate.
     * @return A new ColumnPos instance (e.g., "100 -50").
     */
    public static ColumnPos absolute(int x, int z)  {
        return new ColumnPos(String.valueOf(x), String.valueOf(z));
    }

    /**
     * Creates a ColumnPos using relative (tilde ~) integer coordinates.
     * Uses "~" alone if the offset is 0, following Minecraft convention.
     *
     * @param x The relative X offset.
     * @param z The relative Z offset.
     * @return A new ColumnPos instance (e.g., "~5 ~").
     */
    public static ColumnPos relative(int x, int z)  {
        // Use "~" for zero offset (instead of "~0")
        String relX = x == 0 ? "~" : "~" + x;
        String relZ = z == 0 ? "~" : "~" + z;
        return new ColumnPos(relX, relZ);
    }

    /**
     * Validates a single coordinate string based on Minecraft command syntax rules for chunk coordinates.
     *
     * @param value The coordinate string to validate.
     * @return true if the string is a valid Minecraft coordinate, false otherwise.
     */
    private boolean isValidCoordinate(String value) {
        return value.matches(COORD_REGEX);
    }

    /**
     * Returns the 2D coordinate arguments formatted for the Minecraft command,
     * separated by a space (e.g., "100 ~5").
     *
     * @return The space-separated coordinate string.
     */
    @Override
    public String toString() {
        return x + " " + z;
    }
}