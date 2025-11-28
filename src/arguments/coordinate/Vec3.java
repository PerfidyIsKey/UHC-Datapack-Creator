package arguments.coordinate;

import java.util.Objects;

/**
 * Represents a 3D vector or position argument for Minecraft commands, supporting
 * absolute (e.g., 10.5), relative (~, e.g., ~-5.2), and local (^, e.g., ^1) coordinates.
 * <p>
 * The final output format is always "X Y Z" separated by spaces.
 */
public class Vec3 {

    private final String x;
    private final String y;
    private final String z;

    // Regex for robust validation of Minecraft coordinates, supporting floats.
    // It must match:
    // 1. Absolute float/int: (-?)(\d+(\.\d+)?) -> 100, -100.5
    // 2. Relative float/int: (~)(?:-?)(\d+(\.\d+)?)? -> ~ or ~5.2 or ~-3
    // 3. Local float/int: (^)(?:-?)(\d+(\.\d+)?)? -> ^ or ^1.0 or ^-0.5
    // Note: Minecraft does not allow mixing ~ and ^ on the same coordinate.
    private static final String COORD_REGEX =
            "^" + // Start of string
                    "(?:[~^])?" + // Optional prefix (~ or ^)
                    "-?" + // Optional minus sign
                    "\\d+" + // One or more digits (required before/after decimal)
                    "(?:\\.\\d+)?" + // Optional decimal part
                    "$" + // End of string
                    "|^[~^]$"; // OR just ~ or ^ (for zero offset)

    /**
     * Private constructor that enforces coordinate validity and checks for null inputs.
     *
     * @param x The X-coordinate string (e.g., "100.5", "~5", "^-2.0").
     * @param y The Y-coordinate string.
     * @param z The Z-coordinate string.
     * @throws IllegalArgumentException if any coordinate is null or fails validation.
     */
    private Vec3(String x, String y, String z) {
        if (x == null || y == null || z == null) {
            throw new IllegalArgumentException("Vec3 coordinates (x, y, z) cannot be null.");
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

    // --- Factory Methods for Absolute Coordinates ---

    /**
     * Creates a Vec3 using absolute, double-precision coordinates.
     */
    public static Vec3 absolute(double x, double y, double z)  {
        return new Vec3(String.valueOf(x), String.valueOf(y), String.valueOf(z));
    }

    /**
     * Creates a Vec3 using absolute, integer coordinates.
     */
    public static Vec3 absolute(int x, int y, int z)  {
        return absolute((double)x, (double)y, (double)z);
    }

    /**
     * Creates a Vec3 using absolute coordinates from a double array.
     * @throws IllegalArgumentException if the array is null or not length 3.
     */
    public static Vec3 absolute(double[] pos)  {
        if (pos == null || pos.length != 3) {
            throw new IllegalArgumentException("Absolute position array must be non-null and contain exactly 3 doubles (X, Y, Z).");
        }
        return absolute(pos[0], pos[1], pos[2]);
    }

    /**
     * Creates a Vec3 using absolute coordinates from an int array.
     * @throws IllegalArgumentException if the array is null or not length 3.
     */
    public static Vec3 absolute(int[] pos)  {
        if (pos == null || pos.length != 3) {
            throw new IllegalArgumentException("Absolute position array must be non-null and contain exactly 3 doubles (X, Y, Z).");
        }
        return absolute(pos[0], pos[1], pos[2]);
    }

    // --- Factory Methods for Relative Coordinates (Tilde ~) ---

    /**
     * Creates a Vec3 using relative (tilde ~) coordinates.
     * Uses "~" alone if the offset is 0.0, following Minecraft convention.
     */
    public static Vec3 relative(double x, double y, double z)  {
        String relX = x == 0.0 ? "~" : "~" + x;
        String relY = y == 0.0 ? "~" : "~" + y;
        String relZ = z == 0.0 ? "~" : "~" + z;
        return new Vec3(relX, relY, relZ);
    }

    /**
     * Creates a Vec3 using relative (tilde ~) integer coordinates.
     */
    public static Vec3 relative(int x, int y, int z)  {
        return relative((double)x, (double)y, (double)z);
    }

    // --- Factory Methods for Local Coordinates (Caret ^) ---

    /**
     * Creates a Vec3 using local (caret ^) coordinates (relative to entity facing).
     * Uses "^" alone if the offset is 0.0.
     */
    public static Vec3 local(double x, double y, double z)  {
        String locX = x == 0.0 ? "^" : "^" + x;
        String locY = y == 0.0 ? "^" : "^" + y;
        String locZ = z == 0.0 ? "^" : "^" + z;
        return new Vec3(locX, locY, locZ);
    }

    /**
     * Creates a Vec3 using local (caret ^) integer coordinates.
     */
    public static Vec3 local(int x, int y, int z)  {
        return local((double)x, (double)y, (double)z);
    }

    /**
     * Validates a single coordinate string based on Minecraft command syntax rules.
     *
     * @param value The coordinate string to validate.
     * @return true if the string is a valid Minecraft coordinate, false otherwise.
     */
    private boolean isValidCoordinate(String value) {
        return value.matches(COORD_REGEX);
    }

    /**
     * Returns the vector arguments formatted for the Minecraft command,
     * separated by spaces (e.g., "100.5 ~5.2 ^-2").
     *
     * @return The space-separated coordinate string.
     */
    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}