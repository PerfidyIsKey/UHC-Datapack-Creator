package commands.time;

/**
 * Defines the three possible actions for the Minecraft {@code /time} command:
 * modifying, querying, or setting the world time.
 * <p>
 * The output for command arguments must be the lowercase version of the constant name.
 */
public enum TimeAction {

    /** * The action to add time to the current world time.
     * Converts to the command argument "add".
     */
    ADD,

    /** * The action to query the current world time (e.g., /time query day).
     * Converts to the command argument "query".
     */
    QUERY,

    /** * The action to set the exact world time or a specified time of day (e.g., /time set day).
     * Converts to the command argument "set".
     */
    SET;

    /**
     * Overrides the default {@code toString()} method to return the lowercase
     * name of the action, as required by the Minecraft command syntax.
     *
     * @return The lowercase action name (e.g., "add", "query", or "set").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}