package arguments.coordinate;

/**
 * Represents a coordinate that is **local** to the command executor's position and look direction,
 * prefixed with the caret symbol (^).
 * <p>
 * Local coordinates are used for movement relative to the executor's orientation:
 * <ul>
 * <li>^X: Forward/Backward</li>
 * <li>^Y: Up/Down</li>
 * <li>^Z: Left/Right (relative to look direction)</li>
 * </ul>
 * Examples: {@code ^5}, {@code ^0}, {@code ^-2}.
 */
public record LocalCoordinate(int offset) implements Coordinate {

    /**
     * Formats the local coordinate for use in a Minecraft command.
     * <p>
     * Local coordinates are always explicitly marked with the caret (^) followed by the offset value,
     * including a zero offset (e.g., {@code ^0}).
     *
     * @return The formatted coordinate string (e.g., "^5" or "^-1").
     */
    @Override
    public String format() {
        return "^" + offset;
    }

    /**
     * Creates a new LocalCoordinate instance with the specified offset.
     * This method is provided as a static factory for clearer intent.
     *
     * @param offset The integer distance relative to the executor's look vector (e.g., 5, 0, -2).
     * @return A new LocalCoordinate instance.
     */
    public static LocalCoordinate create(int offset) {
        return new LocalCoordinate(offset);
    }
}