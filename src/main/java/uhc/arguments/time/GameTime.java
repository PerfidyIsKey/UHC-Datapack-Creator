package uhc.arguments.time;

/**
 * Defines the contract for any object representing a time argument in the
 * Minecraft {@code /time} command.
 * <p>
 * This interface mandates the ability to return a time value string, which
 * could be a raw tick count, a time abbreviation (like "day" or "midnight"),
 * or a combined value with a unit (like "10d").
 */
public interface GameTime {

    /**
     * Returns the raw string representation of the time value in the exact format
     * required by the Minecraft command parser.
     * <p>
     * Example outputs:
     * <ul>
     * <li>{@code 6000} (Raw tick count)</li>
     * <li>{@code day} (Time abbreviation)</li>
     * <li>{@code 10d} (Value with unit)</li>
     * </ul>
     *
     * @return The fully formatted time argument string.
     */
    String getTime();

    /**
     * Overrides the default {@code toString()} method to simply return the
     * result of {@code getTime()}. This ensures that a {@code GameTime} object
     * is seamlessly concatenated into the final command string.
     * * @return The fully formatted time argument string, identical to {@code getTime()}.
     */
    @Override
    String toString();
}