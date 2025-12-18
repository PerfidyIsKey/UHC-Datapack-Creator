package uhc.arguments.coordinate;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 📐 **Swizzle Argument**
 * <p>
 * Represents a combination of the x, y, and z axes used for alignment.
 * Valid combinations include subsets of "xyz" such as "x", "xy", or "yxz".
 * </p>
 */
public class Swizzle {
    private static final Pattern VALID_AXES = Pattern.compile("^[xyz]{1,3}$");

    // Pre-defined constants for common use cases
    public static final Swizzle X = new Swizzle("x");
    public static final Swizzle Y = new Swizzle("y");
    public static final Swizzle Z = new Swizzle("z");
    public static final Swizzle XY = new Swizzle("xy");
    public static final Swizzle XZ = new Swizzle("xz");
    public static final Swizzle YZ = new Swizzle("yz");
    public static final Swizzle XYZ = new Swizzle("xyz");

    private final String axes;

    private Swizzle(String axes) {
        this.axes = axes;
    }

    /**
     * Creates a custom Swizzle from a string.
     * @param axes A string containing 'x', 'y', or 'z'.
     * @return A validated Swizzle instance.
     * @throws IllegalArgumentException if the axes are invalid or empty.
     */
    public static Swizzle of(String axes) {
        Objects.requireNonNull(axes, "Axes string cannot be null.");
        String cleaned = axes.toLowerCase().trim();
        if (!VALID_AXES.matcher(cleaned).matches()) {
            throw new IllegalArgumentException("Invalid swizzle axes: " + axes);
        }
        return new Swizzle(cleaned);
    }

    @Override
    public String toString() {
        return axes;
    }
}