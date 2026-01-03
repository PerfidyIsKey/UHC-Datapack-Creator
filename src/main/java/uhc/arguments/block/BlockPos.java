package uhc.arguments.block;

import uhc.arguments.coordinate.AbsoluteCoordinate;
import uhc.arguments.coordinate.Coordinate;
import uhc.arguments.coordinate.LocalCoordinate;
import uhc.arguments.coordinate.RelativeCoordinate;

import java.util.Objects;

/**
 * 📍 **Block Position**
 * <p>
 * Represents a 3D coordinate argument used in Minecraft commands.
 * This class ensures **type safety** by using explicit {@link Coordinate} objects.
 * </p>
 */
public class BlockPos {

    // --- 📄 Fields ---

    /** * The X-axis coordinate component. */
    private Coordinate x;

    /** * The Y-axis coordinate component. */
    private Coordinate y;

    /** * The Z-axis coordinate component. */
    private Coordinate z;

    // --- 🏗️ Constructors & Factories ---

    private BlockPos() {
        this.x = AbsoluteCoordinate.create(0);
        this.y = AbsoluteCoordinate.create(0);
        this.z = AbsoluteCoordinate.create(0);
    }

    /**
     * Creates a new BlockPos builder instance, defaulted to absolute coordinates 0 0 0.
     * @return A new BlockPos instance.
     */
    public static BlockPos create() {
        return new BlockPos();
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Sets the X coordinate component.
     * @throws NullPointerException if x is null.
     */
    public BlockPos x(Coordinate x) {
        this.x = Objects.requireNonNull(x, "X coordinate cannot be null.");
        return this;
    }

    /**
     * Sets the Y coordinate component.
     * @throws NullPointerException if y is null.
     */
    public BlockPos y(Coordinate y) {
        this.y = Objects.requireNonNull(y, "Y coordinate cannot be null.");
        return this;
    }

    /**
     * Sets the Z coordinate component.
     * @throws NullPointerException if z is null.
     */
    public BlockPos z(Coordinate z) {
        this.z = Objects.requireNonNull(z, "Z coordinate cannot be null.");
        return this;
    }

    // --- 🔍 Accessors ---

    /** @return The X {@link Coordinate}. */
    public Coordinate getX() { return x; }

    /** @return The Y {@link Coordinate}. */
    public Coordinate getY() { return y; }

    /** @return The Z {@link Coordinate}. */
    public Coordinate getZ() { return z; }

    /**
     * Calculates a new Absolute BlockPos by adding offsets to the current absolute coordinates.
     * <p>
     * <b>Constraint:</b> This method can only be called if all existing components (X, Y, Z)
     * are of type {@link AbsoluteCoordinate}.
     * </p>
     * * @param offsetX The integer amount to add to X.
     * @param offsetY The integer amount to add to Y.
     * @param offsetZ The integer amount to add to Z.
     * @return A new {@link BlockPos} with the calculated absolute values.
     * @throws IllegalStateException if any current coordinate is relative (~) or local (^).
     */
    public BlockPos getPosRelative(int offsetX, int offsetY, int offsetZ) {
        if (!(x instanceof AbsoluteCoordinate &&
                y instanceof AbsoluteCoordinate &&
                z instanceof AbsoluteCoordinate)) {
            throw new IllegalStateException("Relative Calculation Error: Cannot calculate absolute relative position because one or more axes are not AbsoluteCoordinates.");
        }

        // Cast safely because the instance check passed
        int newX = ((AbsoluteCoordinate) x).value() + offsetX;
        int newY = ((AbsoluteCoordinate) y).value() + offsetY;
        int newZ = ((AbsoluteCoordinate) z).value() + offsetZ;

        return BlockPos.absolute(newX, newY, newZ);
    }

    // --- 🚀 Static Factory Methods ---

    public static BlockPos absolute(int x, int y, int z)  {
        return BlockPos.create()
                .x(AbsoluteCoordinate.create(x))
                .y(AbsoluteCoordinate.create(y))
                .z(AbsoluteCoordinate.create(z));
    }

    public static BlockPos absolute(int[] pos) {
        Objects.requireNonNull(pos, "Position array cannot be null.");
        if (pos.length != 3) {
            throw new IllegalArgumentException("Absolute position array must contain exactly 3 integers.");
        }
        return absolute(pos[0], pos[1], pos[2]);
    }

    public static BlockPos relative(int x, int y, int z)  {
        return BlockPos.create()
                .x(RelativeCoordinate.create(x))
                .y(RelativeCoordinate.create(y))
                .z(RelativeCoordinate.create(z));
    }

    public static BlockPos local(int x, int y, int z)  {
        return BlockPos.create()
                .x(LocalCoordinate.create(x))
                .y(LocalCoordinate.create(y))
                .z(LocalCoordinate.create(z));
    }

    // --- ⚙️ Logic & Formatting ---

    public String getCoordinate() {
        try {
            return String.format("%s, %s, %s", x.format(), y.format(), z.format());
        } catch (Exception e) {
            return "0, 0, 0";
        }
    }

    public String title() {
        return getCoordinate();
    }

    @Override
    public String toString() {
        try {
            return String.format("%s %s %s", x.format(), y.format(), z.format());
        } catch (Exception e) {
            return "0 0 0";
        }
    }
}