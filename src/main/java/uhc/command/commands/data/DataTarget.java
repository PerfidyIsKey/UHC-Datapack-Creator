package uhc.command.commands.data;

/**
 * 🎯 **Data Target Interface**
 * <p>
 * This interface defines the contract for any object that can be the target
 * of a Minecraft {@code /data} command.
 * </p>
 * <p>
 * Minecraft's data command requires targets to be prefixed with their type
 * (e.g., {@code entity}, {@code block}, or {@code storage}). Implementations
 * of this interface must handle both the raw identification and the
 * command-ready string representation.
 * </p>
 */
public interface DataTarget {

    // --- 🔍 Accessors ---

    /**
     * Retrieves the raw identification string of the target.
     * <p>
     * <b>Examples:</b>
     * <ul>
     * <li>For an Entity: {@code "@s"} or {@code "0-0-0-0-1"}</li>
     * <li>For a Block: {@code "10 64 -20"}</li>
     * <li>For Storage: {@code "uhc:global_data"}</li>
     * </ul>
     * </p>
     * @return The raw selector, coordinate, or resource location string.
     */
    String getTarget();

    /**
     * Builds the complete target segment as it appears in a command.
     * <p>
     * <b>Implementation Note:</b> This must include the target type prefix.
     * The resulting string is typically formatted as {@code <type> <identifier>}.
     * </p>
     * <p>
     * <b>Expected Output:</b>
     * <ul>
     * <li>{@code "entity @p"}</li>
     * <li>{@code "block ~ ~ ~"}</li>
     * <li>{@code "storage custom:vault"}</li>
     * </ul>
     * </p>
     * <b>Error Catching:</b> Implementations should ensure that if the internal
     * identifier is null or malformed, an {@link IllegalStateException} is
     * thrown during the build process to prevent executing broken commands.
     * * @return The full command fragment ready for the {@code /data} syntax.
     */
    @Override
    String toString();

    // --- ⚙️ Contract Logic ---

    /**
     * Validates the integrity of the data target components.
     * <p>
     * This method can be called to ensure the target is syntactically valid
     * before the command is generated or sent to the server.
     * </p>
     * @throws IllegalStateException if the target identification is missing or invalid.
     */
    default void validate() throws IllegalStateException {
        if (getTarget() == null || getTarget().trim().isEmpty()) {
            throw new IllegalStateException("DataTarget identification cannot be null or empty.");
        }
    }
}