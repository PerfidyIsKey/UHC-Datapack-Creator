package uhc.arguments.time;

/**
 * Defines the specific targets that can be queried using the
 * Minecraft {@code /time query} command.
 * <p>
 * This class implements the {@link GameTime} interface to be compatible
 * with the main {@code Time} command builder.
 */
public enum QueryTime implements GameTime {

    /** * Queries the time of day, which cycles between 0 and 24000.
     * Converts to the command argument "daytime".
     */
    DAYTIME,

    /** * Queries the total number of game ticks elapsed since the world was created.
     * This value only increases. Converts to the command argument "gametime".
     */
    GAMETIME,

    /** * Queries the number of days elapsed since the world was created.
     * Converts to the command argument "day".
     */
    DAY;

    /**
     * Implements the {@link GameTime} contract, returning the lowercase
     * name of the query target.
     *
     * @return The lowercase query target string (e.g., "daytime").
     */
    @Override
    public String getTime() {
        return this.name().toLowerCase();
    }

    /**
     * Returns the lowercase query target string, suitable for direct insertion into a command.
     *
     * @return The lowercase query target string.
     */
    @Override
    public String toString() {
        return getTime();
    }
}