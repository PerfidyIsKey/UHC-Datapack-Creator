package uhc.arguments.coordinate;

import uhc.arguments.block.BlockPos;

import java.util.Objects;

/**
 * 📐 **3D Vector Argument (Vec3)**
 * <p>
 * Represents a high-precision position argument for Minecraft commands.
 * This class ensures <b>Type Safety</b> by storing explicit {@link Coordinate}
 * objects rather than raw strings, maintaining the distinction between
 * Absolute, Relative (~), and Local (^) systems.
 * </p>
 */
public class Vec3 {

    // --- 📄 Fields ---

    /** * The X-axis coordinate component (East/West). */
    private final Coordinate x;

    /** * The Y-axis coordinate component (Up/Down). */
    private final Coordinate y;

    /** * The Z-axis coordinate component (South/North). */
    private final Coordinate z;

    // --- 🏗️ Private Constructor ---

    /**
     * Constructs a Vec3 with strict validation against mixed coordinate types.
     * <p><b>No-Fallback Policy:</b> If coordinates are null or inconsistent,
     * a clear {@link IllegalArgumentException} is thrown.</p>
     * * @param x The non-null X coordinate component.
     * @param y The non-null Y coordinate component.
     * @param z The non-null Z coordinate component.
     * @throws NullPointerException if any coordinate is null.
     * @throws IllegalArgumentException if the coordinate types are inconsistent
     * (e.g., mixing relative and local).
     */
    private Vec3(Coordinate x, Coordinate y, Coordinate z) {
        this.x = Objects.requireNonNull(x, "Vec3 Assembly Error: X coordinate cannot be null.");
        this.y = Objects.requireNonNull(y, "Vec3 Assembly Error: Y coordinate cannot be null.");
        this.z = Objects.requireNonNull(z, "Vec3 Assembly Error: Z coordinate cannot be null.");

        validateCoordinateConsistency();
    }

    // --- 🚀 Static Factory Methods (General) ---

    /**
     * Bridges the gap between BlockPos and Vec3 using shared Coordinate types.
     * This provides a type-safe way for {@link BlockPos} to convert itself without
     * needing access to the private constructor.
     *
     * @param pos The source BlockPos instance to convert.
     * @return A new Vec3 instance reflecting the source coordinates.
     * @throws NullPointerException if the input position is null.
     */
    public static Vec3 fromBlockPos(BlockPos pos) {
        Objects.requireNonNull(pos, "Vec3 Conversion Error: Source BlockPos cannot be null.");
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    // --- 🚀 Static Factory Methods (Absolute) ---

    /**
     * Creates a Vec3 using absolute integer coordinates.
     * * @param x Fixed X coordinate.
     * @param y Fixed Y coordinate.
     * @param z Fixed Z coordinate.
     * @return A new Vec3 using {@link AbsoluteCoordinate}.
     */
    public static Vec3 absolute(int x, int y, int z) {
        return new Vec3(
                AbsoluteCoordinate.create(x),
                AbsoluteCoordinate.create(y),
                AbsoluteCoordinate.create(z)
        );
    }

    /**
     * Creates a Vec3 using an absolute integer array.
     * * @param pos An array containing exactly 3 integers (X, Y, Z).
     * @return A new Vec3 using {@link AbsoluteCoordinate}.
     * @throws NullPointerException if the array is null.
     * @throws IllegalArgumentException if the array length is not exactly 3.
     */
    public static Vec3 absolute(int[] pos) {
        Objects.requireNonNull(pos, "Vec3 Array Error: Input array cannot be null.");
        if (pos.length != 3) {
            throw new IllegalArgumentException("Vec3 Array Error: Position array must contain exactly 3 integers. Length found: " + pos.length);
        }
        return absolute(pos[0], pos[1], pos[2]);
    }

    // --- 🚀 Static Factory Methods (Relative) ---

    /**
     * Creates a Vec3 using relative (tilde ~) integer coordinates.
     * * @param x Relative X offset.
     * @param y Relative Y offset.
     * @param z Relative Z offset.
     * @return A new Vec3 using {@link RelativeCoordinate}.
     */
    public static Vec3 relative(int x, int y, int z) {
        return new Vec3(
                RelativeCoordinate.create(x),
                RelativeCoordinate.create(y),
                RelativeCoordinate.create(z)
        );
    }

    // --- 🚀 Static Factory Methods (Local) ---

    /**
     * Creates a Vec3 using local (caret ^) integer coordinates.
     * * @param x Local X offset (left/right).
     * @param y Local Y offset (up/down).
     * @param z Local Z offset (forward/backward).
     * @return A new Vec3 using {@link LocalCoordinate}.
     */
    public static Vec3 local(int x, int y, int z) {
        return new Vec3(
                LocalCoordinate.create(x),
                LocalCoordinate.create(y),
                LocalCoordinate.create(z)
        );
    }

    // --- 🛠️ Logic & Validation ---

    /**
     * Enforces Minecraft's rule that local (^) and relative (~) coordinates
     * cannot be mixed in a single vector argument.
     * * @throws IllegalArgumentException if mixed coordinate types are detected.
     */
    private void validateCoordinateConsistency() {
        boolean hasRelative = (x instanceof RelativeCoordinate || y instanceof RelativeCoordinate || z instanceof RelativeCoordinate);
        boolean hasLocal = (x instanceof LocalCoordinate || y instanceof LocalCoordinate || z instanceof LocalCoordinate);

        if (hasRelative && hasLocal) {
            throw new IllegalArgumentException("Vec3 Logic Error: Local (^) and Relative (~) coordinates cannot be mixed in the same vector.");
        }
    }

    /**
     * Converts this high-precision Vec3 into an integer-based BlockPos.
     * <p>
     * <b>Logic:</b> This method delegates the conversion to the {@link BlockPos}
     * static factory, ensuring that the coordinate types (Absolute, Relative, Local)
     * are preserved during the transition.
     * </p>
     * @return A new {@link BlockPos} instance.
     */
    public BlockPos toBlockPos() {
        return BlockPos.fromVec3(this);
    }

    // --- 🔍 Accessors ---

    /** @return The internal X-axis {@link Coordinate}. */
    public Coordinate getX() { return x; }

    /** @return The internal Y-axis {@link Coordinate}. */
    public Coordinate getY() { return y; }

    /** @return The internal Z-axis {@link Coordinate}. */
    public Coordinate getZ() { return z; }

    // --- ⚙️ Formatting ---

    /**
     * Returns the formatted vector for use in a Minecraft command.
     * <p>Example output: {@code "10 64 -50"} or {@code "~ ~5 ~-2"}</p>
     * * @return A space-separated coordinate string.
     * @throws RuntimeException if an unexpected error occurs during component formatting.
     */
    @Override
    public String toString() {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(x.format()).append(" ")
                    .append(y.format()).append(" ")
                    .append(z.format());
            return sb.toString();
        } catch (Exception e) {
            // Strict Error Catching: We throw a RuntimeException to avoid returning partial or incorrect data.
            throw new RuntimeException("CRITICAL: Failed to generate Vec3 command string. Details: " + e.getMessage(), e);
        }
    }
}