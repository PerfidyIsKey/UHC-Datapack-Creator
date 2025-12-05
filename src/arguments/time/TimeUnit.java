package arguments.time;

/**
 * Defines the units of time that can be used as arguments in the Minecraft
 * {@code /time} command (e.g., in {@code /time add 10d}).
 */
public enum TimeUnit {

    /** * Represents time in terms of days. The command abbreviation is 'd'. */
    DAY("d"),

    /** * Represents time in terms of seconds. The command abbreviation is 's'. */
    SECOND("s"),

    /** * Represents time in terms of game ticks (20 ticks per second). The command abbreviation is 't'. */
    TICK("t");

    // The single-letter abbreviation used in the Minecraft command syntax.
    private final String unit;

    /**
     * Private constructor that assigns the command-line abbreviation to the enum constant.
     *
     * @param unit The single-letter abbreviation required by the Minecraft command.
     */
    TimeUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Returns the single-letter abbreviation for the time unit.
     *
     * @return The unit abbreviation (e.g., "d", "s", or "t").
     */
    @Override
    public String toString() {
        return unit;
    }
}