package arguments.coordinate;

/**
 * Represents an **absolute, fixed coordinate** (X, Y, or Z) in the Minecraft world,
 * defined by a direct integer value.
 * <p>
 * Absolute coordinates are independent of the command executor's position or rotation.
 * Examples: {@code 100}, {@code 64}, {@code -50}.
 */
public record AbsoluteCoordinate(int value) implements Coordinate {

    /**
     * Formats the absolute coordinate for use in a Minecraft command.
     * <p>
     * Since absolute coordinates require no prefix, this returns the integer value as a string.
     *
     * @return The formatted coordinate string (e.g., "100" or "-50").
     */
    @Override
    public String format() {
        return String.valueOf(value);
    }

    /**
     * Creates a new AbsoluteCoordinate instance with the specified integer value.
     * This method is provided as a static factory for clearer intent.
     *
     * @param value The fixed integer coordinate value.
     * @return A new AbsoluteCoordinate instance.
     */
    public static AbsoluteCoordinate create(int value) {
        return new AbsoluteCoordinate(value);
    }
}