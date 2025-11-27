package arguments;

/**
 * Represents a block position argument used in Minecraft commands (e.g., /setblock).
 * This class supports three coordinate types: absolute (integers), relative (~) and local (^).
 * <p>
 * The final output format is always "X Y Z" separated by spaces.
 */
public class BlockPos {

    private final String x;
    private final String y;
    private final String z;

    /**
     * Private constructor that enforces coordinate validity and checks for null inputs.
     *
     * @param x The X-coordinate string (e.g., "100", "~5", "^-2").
     * @param y The Y-coordinate string.
     * @param z The Z-coordinate string.
     * @throws IllegalArgumentException if any coordinate is null or fails validation.
     */
    private BlockPos(String x, String y, String z) {
        if (x == null || y == null || z == null) {
            throw new IllegalArgumentException("BlockPos coordinates (x, y, z) cannot be null.");
        }

        // Validate each coordinate string immediately
        if (!isValidCoordinate(x)) {
            throw new IllegalArgumentException("Invalid coordinate format for X: " + x);
        }
        if (!isValidCoordinate(y)) {
            throw new IllegalArgumentException("Invalid coordinate format for Y: " + y);
        }
        if (!isValidCoordinate(z)) {
            throw new IllegalArgumentException("Invalid coordinate format for Z: " + z);
        }

        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a BlockPos using absolute, integer coordinates.
     *
     * @param x The absolute X-coordinate.
     * @param y The absolute Y-coordinate.
     * @param z The absolute Z-coordinate.
     * @return A new BlockPos instance.
     */
    public static BlockPos absolute(int x, int y, int z)  {
        return new BlockPos(String.valueOf(x), String.valueOf(y), String.valueOf(z));
    }

    /**
     * Creates a BlockPos using absolute, integer coordinates from an array.
     *
     * @param pos An array containing [X, Y, Z] integer coordinates.
     * @return A new BlockPos instance.
     * @throws IllegalArgumentException if the array is null or does not contain exactly 3 elements.
     */
    public static BlockPos absolute(int[] pos)  {
        if (pos == null || pos.length != 3) {
            throw new IllegalArgumentException("Absolute position array must be non-null and contain exactly 3 integers (X, Y, Z).");
        }
        return new BlockPos(String.valueOf(pos[0]), String.valueOf(pos[1]), String.valueOf(pos[2]));
    }

    /**
     * Creates a BlockPos using relative coordinates (relative to the command executor's position),
     * prefixed with the tilde (~).
     *
     * @param x The relative X offset.
     * @param y The relative Y offset.
     * @param z The relative Z offset.
     * @return A new BlockPos instance (e.g., "~5 ~0 ~-2").
     */
    public static BlockPos relative(int x, int y, int z)  {
        // For relative coordinates, use "~" for the relative symbol, using "" for zero offset
        String relX = x == 0 ? "~" : "~" + x;
        String relY = y == 0 ? "~" : "~" + y;
        String relZ = z == 0 ? "~" : "~" + z;
        return new BlockPos(relX, relY, relZ);
    }

    /**
     * Creates a BlockPos using local coordinates (relative to the command executor's look direction),
     * prefixed with the caret (^).
     *
     * @param x The local forward/backward offset.
     * @param y The local up/down offset.
     * @param z The local left/right offset.
     * @return A new BlockPos instance (e.g., "^5 ^1 ^0").
     */
    public static BlockPos local(int x, int y, int z)  {
        // Local coordinates must always be explicitly marked with '^' even for zero offset,
        // but since we rely on the input string validation, we can just prepend.
        return new BlockPos("^" + x, "^" + y, "^" + z);
    }

    /**
     * Validates a single coordinate string based on Minecraft command syntax rules.
     * <p>
     * Regex explanation:
     * <ul>
     * <li>^: Start of string.</li>
     * <li>(?:[~^])?: Optional non-capturing group for either '~' (relative) OR '^' (local).</li>
     * <li>-?: Optional minus sign for negative numbers.</li>
     * <li>\d+: One or more digits (for absolute or offset values).</li>
     * <li>|: OR</li>
     * <li>[~^]: Either just '~' or just '^' (representing zero offset, e.g., "~ ~ ~").</li>
     * <li>$: End of string.</li>
     * </ul>
     * This stricter regex prevents invalid combinations like `~^1` or empty strings.
     *
     * @param value The coordinate string to validate.
     * @return true if the string is a valid Minecraft coordinate, false otherwise.
     */
    private boolean isValidCoordinate(String value) {
        // Stricter regex: (optional ~ or ^, followed by optional -, followed by digits) OR (~ or ^ alone)
        return value.matches("^(?:[~^]?-?\\d+)|[~^]$");
    }

    /**
     * Returns the position arguments formatted for the Minecraft command,
     * separated by spaces (e.g., "100 64 200").
     *
     * @return The space-separated coordinate string.
     */
    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}