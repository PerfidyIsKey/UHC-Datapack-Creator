package commands.data;

/**
 * Interface defining the contract for all data modification operations in the
 * "data modify <target> <path> <modification>" command structure.
 * * Implementing classes must represent the complete '<modification>' segment
 * (e.g., "set value 4b", "insert 0 from block ~ ~ ~").
 */
public interface DataModification {
    /**
     * Builds the complete modification string fragment of the command.
     * Example return value: "set value 4b"
     * * @return The modification command string.
     */
    String build();

    /**
     * Provides the modification string, typically by delegating to {@link #build()}.
     * This allows the modification object to be directly concatenated into the
     * command string using StringBuilder's append methods.
     * * @return The modification command string.
     */
    @Override
    String toString();
}