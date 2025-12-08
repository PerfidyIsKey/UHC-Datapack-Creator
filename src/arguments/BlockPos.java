package arguments;

import arguments.coordinate.AbsoluteCoordinate;
import arguments.coordinate.Coordinate;
import arguments.coordinate.LocalCoordinate;
import arguments.coordinate.RelativeCoordinate;

/**
 * Represents a block position argument used in Minecraft commands (e.g., /setblock).
 * This class ensures **type safety** by accepting explicit {@code Coordinate} objects (Absolute, Relative, Local) for X, Y, and Z.
 * <p>
 * It uses the Builder Pattern to allow mixing and matching coordinate types (e.g., Absolute X, Relative Y, Local Z).
 * The final output format is always the space-separated string: "X Y Z".
 */
public class BlockPos {

    private Coordinate x;
    private Coordinate y;
    private Coordinate z;

    /**
     * Private constructor initializes the BlockPos with default absolute zero coordinates.
     * This ensures the object is always in a valid, absolute state upon creation.
     */
    private BlockPos() {
        // Initialize to safe defaults (Absolute 0 0 0)
        this.x = AbsoluteCoordinate.create(0);
        this.y = AbsoluteCoordinate.create(0);
        this.z = AbsoluteCoordinate.create(0);
    }

    /**
     * Creates a new BlockPos builder instance, defaulted to absolute coordinates 0 0 0.
     *
     * @return A new BlockPos instance, ready for method chaining.
     */
    public static BlockPos create() {
        return new BlockPos();
    }

    // --- Type-Safe Builder Methods ---

    /**
     * Sets the X coordinate using a type-safe {@code Coordinate} object (Absolute, Relative, or Local).
     *
     * @param x The object representing the X coordinate type and value.
     * @return The current builder instance for chaining.
     * @throws IllegalArgumentException if the provided coordinate is {@code null}.
     */
    public BlockPos x(Coordinate x) {
        if (x == null) throw new IllegalArgumentException("X coordinate cannot be null.");
        this.x = x;
        return this;
    }

    /**
     * Sets the Y coordinate using a type-safe {@code Coordinate} object (Absolute, Relative, or Local).
     *
     * @param y The object representing the Y coordinate type and value.
     * @return The current builder instance for chaining.
     * @throws IllegalArgumentException if the provided coordinate is {@code null}.
     */
    public BlockPos y(Coordinate y) {
        if (y == null) throw new IllegalArgumentException("Y coordinate cannot be null.");
        this.y = y;
        return this;
    }

    /**
     * Sets the Z coordinate using a type-safe {@code Coordinate} object (Absolute, Relative, or Local).
     *
     * @param z The object representing the Z coordinate type and value.
     * @return The current builder instance for chaining.
     * @throws IllegalArgumentException if the provided coordinate is {@code null}.
     */
    public BlockPos z(Coordinate z) {
        if (z == null) throw new IllegalArgumentException("Z coordinate cannot be null.");
        this.z = z;
        return this;
    }

    // --- Convenience Static Builders (for simple, single-type positions) ---

    /**
     * Creates a BlockPos where all coordinates are **absolute** integers (e.g., "100 64 200").
     *
     * @param x The absolute X-coordinate.
     * @param y The absolute Y-coordinate.
     * @param z The absolute Z-coordinate.
     * @return A new BlockPos instance.
     */
    public static BlockPos absolute(int x, int y, int z)  {
        return BlockPos.create()
                .x(AbsoluteCoordinate.create(x))
                .y(AbsoluteCoordinate.create(y))
                .z(AbsoluteCoordinate.create(z));
    }

    /**
     * Creates a BlockPos using **absolute** integer coordinates from an array.
     *
     * @param pos An array containing exactly 3 integer coordinates [X, Y, Z].
     * @return A new BlockPos instance.
     * @throws IllegalArgumentException if the array is {@code null} or does not contain exactly 3 elements.
     */
    public static BlockPos absolute(int[] pos) {
        if (pos == null || pos.length != 3) {
            throw new IllegalArgumentException("Absolute position array must be non-null and contain exactly 3 integers (X, Y, Z).");
        }
        return BlockPos.create()
                .x(AbsoluteCoordinate.create(pos[0]))
                .y(AbsoluteCoordinate.create(pos[1]))
                .z(AbsoluteCoordinate.create(pos[2]));
    }

    /**
     * Creates a BlockPos where all coordinates are **relative** offsets.
     * <p>
     * Output examples: {@code ~5 ~0 ~-2}. A zero offset renders as {@code ~} alone.
     *
     * @param x The relative X offset.
     * @param y The relative Y offset.
     * @param z The relative Z offset.
     * @return A new BlockPos instance.
     */
    public static BlockPos relative(int x, int y, int z)  {
        return BlockPos.create()
                .x(RelativeCoordinate.create(x))
                .y(RelativeCoordinate.create(y))
                .z(RelativeCoordinate.create(z));
    }

    /**
     * Creates a BlockPos where all coordinates are **local** offsets.
     * <p>
     * Output examples: {@code ^5 ^1 ^0}. Local coordinates always explicitly include the offset, even if zero.
     *
     * @param x The local forward/backward offset.
     * @param y The local up/down offset.
     * @param z The local left/right offset.
     * @return A new BlockPos instance.
     */
    public static BlockPos local(int x, int y, int z)  {
        return BlockPos.create()
                .x(LocalCoordinate.create(x))
                .y(LocalCoordinate.create(y))
                .z(LocalCoordinate.create(z));
    }


    /**
     * Returns the position arguments formatted for the Minecraft command,
     * separated by spaces (e.g., "100 64 200" or "~5 64 ^1").
     *
     * @return The final space-separated coordinate string.
     */
    @Override
    public String toString() {
        return x.format() + " " + y.format() + " " + z.format();
    }
}