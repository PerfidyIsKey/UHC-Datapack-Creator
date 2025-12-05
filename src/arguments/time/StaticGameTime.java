package arguments.time;

import arguments.GameTime;

/**
 * An enumeration representing the fixed, named time values (abbreviations)
 * that can be used with the {@code /time set} command.
 * <p>
 * This class implements the {@link GameTime} interface.
 */
public enum StaticGameTime implements GameTime {

    /** Represents the start of day (time 1000). Converts to "day". */
    DAY,

    /** Represents the start of night (time 13000). Converts to "night". */
    NIGHT,

    /** Represents high noon (time 6000). Converts to "noon". */
    NOON,

    /** Represents midnight (time 18000). Converts to "midnight". */
    MIDNIGHT;

    /**
     * Implements the {@link GameTime} contract, returning the lowercase
     * name of the time abbreviation.
     *
     * @return The lowercase time abbreviation (e.g., "day", "midnight").
     */
    @Override
    public String getTime() {
        return this.name().toLowerCase();
    }

    /**
     * Returns the time abbreviation string, suitable for direct insertion into a command.
     *
     * @return The lowercase time abbreviation string.
     */
    @Override
    public String toString() {
        return getTime();
    }
}