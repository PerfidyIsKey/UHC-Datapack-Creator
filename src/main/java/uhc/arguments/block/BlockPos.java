package uhc.arguments.block;

import uhc.arguments.coordinate.AbsoluteCoordinate;
import uhc.arguments.coordinate.Coordinate;
import uhc.arguments.coordinate.LocalCoordinate;
import uhc.arguments.coordinate.RelativeCoordinate;

import java.util.Objects;

/**
 * 📍 **Block Position**
 * <p>
 * Represents a 3D coordinate argument used in Minecraft commands (e.g., {@code /setblock}).
 * This class ensures **type safety** by using explicit {@link Coordinate} objects,
 * allowing for the mixing of Absolute, Relative, and Local coordinates.
 * </p>
 */
public class BlockPos {

    // --- 📄 Fields ---

    /** * The X-axis coordinate component.
     * Holds the specific {@link Coordinate} type (Absolute, Relative, or Local).
     */
    private Coordinate x;

    /** * The Y-axis coordinate component.
     * Holds the specific {@link Coordinate} type (Absolute, Relative, or Local).
     */
    private Coordinate y;

    /** * The Z-axis coordinate component.
     * Holds the specific {@link Coordinate} type (Absolute, Relative, or Local).
     */
    private Coordinate z;

    // --- 🏗️ Constructors ---

    /**
     * Private constructor initializes the BlockPos with default absolute zero coordinates.
     * This ensures the object is always in a valid, absolute state upon creation.
     */
    private BlockPos() {
        this.x = AbsoluteCoordinate.create(0);
        this.y = AbsoluteCoordinate.create(0);
        this.z = AbsoluteCoordinate.create(0);
    }

    /**
     * Creates a new BlockPos builder instance, defaulted to absolute coordinates 0 0 0.
     * * @return A new BlockPos instance, ready for method chaining.
     */
    public static BlockPos create() {
        return new BlockPos();
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Sets the X coordinate component using a type-safe Coordinate object.
     * * @param x The coordinate type and value.
     * @return The current instance for chaining.
     * @throws NullPointerException if the provided coordinate is null.
     */
    public BlockPos x(Coordinate x) {
        this.x = Objects.requireNonNull(x, "X coordinate cannot be null.");
        return this;
    }

    /**
     * Sets the Y coordinate component using a type-safe Coordinate object.
     * * @param y The coordinate type and value.
     * @return The current instance for chaining.
     * @throws NullPointerException if the provided coordinate is null.
     */
    public BlockPos y(Coordinate y) {
        this.y = Objects.requireNonNull(y, "Y coordinate cannot be null.");
        return this;
    }

    /**
     * Sets the Z coordinate component using a type-safe Coordinate object.
     * * @param z The coordinate type and value.
     * @return The current instance for chaining.
     * @throws NullPointerException if the provided coordinate is null.
     */
    public BlockPos z(Coordinate z) {
        this.z = Objects.requireNonNull(z, "Z coordinate cannot be null.");
        return this;
    }

    // --- 🚀 Static Factory Methods ---

    /**
     * Creates a BlockPos where all coordinates are absolute integers.
     * * @param x The absolute X coordinate.
     * @param y The absolute Y coordinate.
     * @param z The absolute Z coordinate.
     * @return A new BlockPos instance (e.g., "100 64 200").
     */
    public static BlockPos absolute(int x, int y, int z)  {
        return BlockPos.create()
                .x(AbsoluteCoordinate.create(x))
                .y(AbsoluteCoordinate.create(y))
                .z(AbsoluteCoordinate.create(z));
    }

    /**
     * Creates a BlockPos from an integer array.
     * * @param pos An array containing exactly 3 integers [X, Y, Z].
     * @return A new absolute BlockPos instance.
     * @throws NullPointerException if the array is null.
     * @throws IllegalArgumentException if the array length is not exactly 3.
     */
    public static BlockPos absolute(int[] pos) {
        Objects.requireNonNull(pos, "Position array cannot be null.");
        if (pos.length != 3) {
            throw new IllegalArgumentException("Absolute position array must contain exactly 3 integers.");
        }
        return absolute(pos[0], pos[1], pos[2]);
    }

    /**
     * Creates a BlockPos where all coordinates are relative offsets.
     * * @param x The relative X offset.
     * @param y The relative Y offset.
     * @param z The relative Z offset.
     * @return A new relative BlockPos instance (e.g., "~5 ~ ~-2").
     */
    public static BlockPos relative(int x, int y, int z)  {
        return BlockPos.create()
                .x(RelativeCoordinate.create(x))
                .y(RelativeCoordinate.create(y))
                .z(RelativeCoordinate.create(z));
    }

    /**
     * Creates a BlockPos where all coordinates are local (caret) offsets.
     * * @param x The local forward/backward offset.
     * @param y The local up/down offset.
     * @param z The local left/right offset.
     * @return A new local BlockPos instance (e.g., "^5 ^1 ^0").
     */
    public static BlockPos local(int x, int y, int z)  {
        return BlockPos.create()
                .x(LocalCoordinate.create(x))
                .y(LocalCoordinate.create(y))
                .z(LocalCoordinate.create(z));
    }

    // --- ⚙️ Logic & Formatting ---

    /**
     * 📝 **Coordinate Display String**
     * <p>Returns the coordinates in a comma-separated format, typically used
     * for UI display, titles, or internal logging.</p>
     *
     * @return A string formatted as {@code "x, y, z"}.
     */
    public String getCoordinate() {
        try {
            return String.format("%s, %s, %s", x.format(), y.format(), z.format());
        } catch (Exception e) {
            // Error Catching: Default fallback to prevent formatting crashes
            return "0, 0, 0";
        }
    }

    /**
     * 📝 **Title Formatting**
     * <p>An alias for {@link #getCoordinate()} used for standardized display naming.</p>
     * * @return A comma-separated coordinate string.
     */
    public String title() {
        return getCoordinate();
    }

    /**
     * 🎮 **Minecraft Command Format**
     * <p>Returns the space-separated coordinates required for Minecraft command arguments.</p>
     *
     * @return A string formatted as {@code "X Y Z"} (e.g., "~5 64 ^1").
     */
    @Override
    public String toString() {
        try {
            return String.format("%s %s %s", x.format(), y.format(), z.format());
        } catch (Exception e) {
            // Error Catching: Safe default for command execution
            return "0 0 0";
        }
    }
}