package uhc.arguments.coordinate;

/**
 * Defines the contract for a single, unit-agnostic Minecraft coordinate (X, Y, or Z).
 * <p>
 * Implementing classes represent the three command coordinate types:
 * <ul>
 * <li>Absolute (e.g., 100)</li>
 * <li>Relative (~, e.g., ~5)</li>
 * <li>Local (^, e.g., ^-2)</li>
 * </ul>
 * This contract enforces a unified {@code format()} method, ensuring type safety when constructing
 * the final coordinate triplet (X Y Z).
 */
public interface Coordinate {

    /**
     * Returns the formatted string representation of the coordinate ready for use
     * directly within a Minecraft command.
     * * @return The formatted coordinate string, including necessary prefixes (e.g., "100", "~5", "^-2").
     */
    String format();
}