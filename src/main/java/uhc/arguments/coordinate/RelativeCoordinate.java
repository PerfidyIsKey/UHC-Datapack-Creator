package uhc.arguments.coordinate;

/**
 * Represents a coordinate that is **relative** to the command executor's position,
 * prefixed with the tilde symbol (~).
 * <p>
 * Examples: {@code ~}, {@code ~5}, {@code ~-10}.
 * This class ensures that a zero offset renders as only {@code ~}.
 */
public record RelativeCoordinate(int offset) implements Coordinate {

    /**
     * Creates a new RelativeCoordinate instance with the specified offset.
     * This method is provided as a static factory for clearer intent.
     *
     * @param offset The integer distance relative to the executor's current position (e.g., 5, 0, -2).
     * @return A new RelativeCoordinate instance.
     */
    public static RelativeCoordinate create(int offset) {
        return new RelativeCoordinate(offset);
    }

    /**
     * Formats the relative coordinate for use in a Minecraft command.
     * <p>
     * Renders as {@code ~} if the offset is zero, or {@code ~[offset]} otherwise.
     *
     * @return The formatted coordinate string (e.g., "~", "~5", or "~-2").
     */
    @Override
    public String format() {
        // According to Minecraft syntax, a zero offset should be represented by the tilde alone (~).
        if (offset == 0) {
            return "~";
        }
        // For non-zero offsets, the value is appended to the tilde.
        return "~" + offset;
    }
}