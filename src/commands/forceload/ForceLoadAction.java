package commands.forceload;

/**
 * Defines the possible actions for the Minecraft {@code /forceload} command,
 * which controls which chunks remain active (loaded) regardless of nearby players.
 * <p>
 * The output for command arguments must be the lowercase version of the constant name.
 */
public enum ForceLoadAction {

    /** * The action to permanently load a specified chunk or range of chunks.
     * Converts to the command argument "add".
     */
    ADD,

    /** * The action to stop permanently loading a specified chunk, range, or all chunks.
     * Converts to the command argument "remove".
     */
    REMOVE,

    /** * The action to check which chunks are currently force-loaded, optionally checking a specific chunk.
     * Converts to the command argument "query".
     */
    QUERY;

    /**
     * Overrides the default {@code toString()} method to return the lowercase
     * name of the action, as required by the Minecraft command syntax.
     *
     * @return The lowercase action name (e.g., "add", "remove", or "query").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}