package uhc.arguments.block;

import uhc.arguments.coordinate.*;

import java.util.Objects;

/**
 * 📍 **Block Position (BlockPos)**
 * <p>
 * Represents a discrete 3D coordinate point within the Minecraft world.
 * This class serves as a type-safe wrapper for three {@link Coordinate} components,
 * ensuring that Absolute, Relative (~), and Local (^) coordinates are handled
 * without loss of context or structural integrity.
 * </p>
 */
public class BlockPos {

    // --- 📄 Fields ---

    /** * The X-axis coordinate component (East/West). */
    private Coordinate x;

    /** * The Y-axis coordinate component (Up/Down). */
    private Coordinate y;

    /** * The Z-axis coordinate component (South/North). */
    private Coordinate z;

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private default constructor.
     * Initialized to absolute zero (0, 0, 0) to ensure the object is never in a null state.
     */
    private BlockPos() {
        this.x = AbsoluteCoordinate.create(0);
        this.y = AbsoluteCoordinate.create(0);
        this.z = AbsoluteCoordinate.create(0);
    }

    /**
     * Static factory to initialize a builder-ready BlockPos instance.
     * @return A new BlockPos defaulted to absolute 0 0 0.
     */
    public static BlockPos create() {
        return new BlockPos();
    }

    // --- 🚀 Static Factory Methods (Primary) ---

    /**
     * Creates a BlockPos from three absolute integer values.
     * @param x The fixed X coordinate.
     * @param y The fixed Y coordinate.
     * @param z The fixed Z coordinate.
     * @return A new BlockPos with {@link AbsoluteCoordinate} components.
     */
    public static BlockPos absolute(int x, int y, int z) {
        return BlockPos.create()
                .x(AbsoluteCoordinate.create(x))
                .y(AbsoluteCoordinate.create(y))
                .z(AbsoluteCoordinate.create(z));
    }

    /**
     * Creates an absolute BlockPos from an integer array.
     * @param pos An array of exactly 3 integers.
     * @return A new absolute BlockPos.
     * @throws NullPointerException if the array is null.
     * @throws IllegalArgumentException if the array length is not exactly 3.
     */
    public static BlockPos absolute(int[] pos) {
        Objects.requireNonNull(pos, "BlockPos Error: Position array cannot be null.");
        if (pos.length != 3) {
            throw new IllegalArgumentException("BlockPos Error: Absolute position array must contain exactly 3 integers (X, Y, Z). Found: " + pos.length);
        }
        return absolute(pos[0], pos[1], pos[2]);
    }

    /**
     * Creates a BlockPos relative (~) to the command executor.
     * @param x The offset from the executor's X position.
     * @param y The offset from the executor's Y position.
     * @param z The offset from the executor's Z position.
     * @return A new BlockPos with {@link RelativeCoordinate} components.
     */
    public static BlockPos relative(int x, int y, int z) {
        return BlockPos.create()
                .x(RelativeCoordinate.create(x))
                .y(RelativeCoordinate.create(y))
                .z(RelativeCoordinate.create(z));
    }

    /**
     * Creates a BlockPos local (^) to the executor's look direction.
     * @param x Offset left/right.
     * @param y Offset up/down.
     * @param z Offset forward/backward.
     * @return A new BlockPos with {@link LocalCoordinate} components.
     */
    public static BlockPos local(int x, int y, int z) {
        return BlockPos.create()
                .x(LocalCoordinate.create(x))
                .y(LocalCoordinate.create(y))
                .z(LocalCoordinate.create(z));
    }

    // --- 🧪 Bridging Logic ---

    /**
     * Bridges the gap between Vec3 and BlockPos using shared Coordinate types.
     * <p><b>Strict Validation:</b> Ensures the source vector is not null before assembly.</p>
     * @param vec The source {@link Vec3} instance.
     * @return A new BlockPos reflecting the vector's coordinates.
     * @throws NullPointerException if vec is null.
     */
    public static BlockPos fromVec3(Vec3 vec) {
        Objects.requireNonNull(vec, "Conversion Error: Source Vec3 cannot be null.");
        return BlockPos.create()
                .x(vec.getX())
                .y(vec.getY())
                .z(vec.getZ());
    }

    /**
     * Converts this discrete BlockPos into a high-precision Vec3.
     * @return A new {@link Vec3} instance managed by the Vec3 static factory.
     * @throws RuntimeException if the internal coordinates are in an illegal state for vectorization.
     */
    public Vec3 toVec3() {
        return Vec3.fromBlockPos(this);
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Sets the X coordinate component.
     * @param x The new X coordinate.
     * @return This builder instance.
     * @throws NullPointerException if the provided coordinate is null.
     */
    public BlockPos x(Coordinate x) {
        this.x = Objects.requireNonNull(x, "BlockPos Configuration Error: X coordinate cannot be null.");
        return this;
    }

    /**
     * Sets the Y coordinate component.
     * @param y The new Y coordinate.
     * @return This builder instance.
     * @throws NullPointerException if the provided coordinate is null.
     */
    public BlockPos y(Coordinate y) {
        this.y = Objects.requireNonNull(y, "BlockPos Configuration Error: Y coordinate cannot be null.");
        return this;
    }

    /**
     * Sets the Z coordinate component.
     * @param z The new Z coordinate.
     * @return This builder instance.
     * @throws NullPointerException if the provided coordinate is null.
     */
    public BlockPos z(Coordinate z) {
        this.z = Objects.requireNonNull(z, "BlockPos Configuration Error: Z coordinate cannot be null.");
        return this;
    }

    // --- 🔍 Accessors ---

    /** @return The internal X coordinate component. */
    public Coordinate getX() { return x; }

    /** @return The internal Y coordinate component. */
    public Coordinate getY() { return y; }

    /** @return The internal Z coordinate component. */
    public Coordinate getZ() { return z; }

    // --- ⚙️ Calculation & Logic ---

    /**
     * Calculates a new Absolute BlockPos by adding integer offsets to existing absolute coordinates.
     * <p>
     * <b>Strict Requirement:</b> All current axes (X, Y, Z) must be instances of {@link AbsoluteCoordinate}.
     * This prevents nonsensical operations like adding 5 to a relative "~" coordinate via this method.
     * </p>
     * @param offsetX Value to add to the current X.
     * @param offsetY Value to add to the current Y.
     * @param offsetZ Value to add to the current Z.
     * @return A new Absolute BlockPos representing the calculated position.
     * @throws IllegalStateException if any axis is Relative (~) or Local (^).
     */
    public BlockPos getPosRelative(int offsetX, int offsetY, int offsetZ) {
        if (!(x instanceof AbsoluteCoordinate && y instanceof AbsoluteCoordinate && z instanceof AbsoluteCoordinate)) {
            throw new IllegalStateException("Calculation Failure: getPosRelative requires all components to be AbsoluteCoordinate. " +
                    "Current types: [X:" + x.getClass().getSimpleName() + ", Y:" + y.getClass().getSimpleName() + ", Z:" + z.getClass().getSimpleName() + "]");
        }

        final int newX = ((AbsoluteCoordinate) x).value() + offsetX;
        final int newY = ((AbsoluteCoordinate) y).value() + offsetY;
        final int newZ = ((AbsoluteCoordinate) z).value() + offsetZ;

        return BlockPos.absolute(newX, newY, newZ);
    }

    // --- 📄 Formatting & Documentation ---

    /**
     * Provides a comma-separated string representation of the coordinates.
     * @return A string in the format "X, Y, Z".
     * @throws RuntimeException if coordinate formatting fails.
     */
    public String getCoordinate() {
        final String formatted = String.format("%s, %s, %s", x.format(), y.format(), z.format());
        if (formatted.contains("null")) {
            throw new RuntimeException("Formatting Error: BlockPos contains null components during string generation.");
        }
        return formatted;
    }

    /**
     * Alias for {@link #getCoordinate()} used for UI titles or display purposes.
     * @return The formatted coordinate string.
     */
    public String title() {
        return this.getCoordinate();
    }

    /**
     * Formats the position for use directly in a Minecraft command.
     * <p><b>Example:</b> {@code "100 64 -250"} or {@code "~ ~5 ~-1"}</p>
     * @return A space-separated coordinate string.
     * @throws RuntimeException if formatting logic encounters an invalid state.
     */
    @Override
    public String toString() {
        try {
            return String.format("%s %s %s", x.format(), y.format(), z.format());
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to stringify BlockPos. One or more Coordinate components are malformed.", e);
        }
    }
}