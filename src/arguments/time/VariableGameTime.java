package arguments.time;

import arguments.GameTime;

/**
 * Represents a numerical time value argument for the Minecraft {@code /time} command
 * (used primarily with the {@code ADD} action).
 * <p>
 * The time value can be a raw number of ticks, or combined with a unit
 * suffix ('t' for ticks, 's' for seconds, 'd' for days).
 * This class implements the {@link GameTime} interface.
 */
public class VariableGameTime implements GameTime {

    // The numerical time value. Uses double to accommodate potential fractional values for seconds/days.
    private final double time;
    // The unit of time (TICK, SECOND, or DAY).
    private final TimeUnit unit;

    /**
     * Private constructor to enforce object creation via static factory methods.
     *
     * @param time The numerical time value.
     * @param unit The unit of time to apply.
     * @throws IllegalArgumentException if the provided {@code unit} is null.
     */
    private VariableGameTime(double time, TimeUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("TimeUnit cannot be null for VariableGameTime.");
        }
        this.time = time;
        this.unit = unit;
    }

    /**
     * Creates a VariableGameTime instance with a double value and a specified unit.
     *
     * @param time The time value (e.g., 5.5).
     * @param unit The unit (e.g., TimeUnit.SECOND).
     * @return A new VariableGameTime instance.
     */
    public static VariableGameTime create(double time, TimeUnit unit) {
        return new VariableGameTime(time, unit);
    }

    /**
     * Creates a VariableGameTime instance with a double value, defaulting the unit to TICK.
     *
     * @param time The time value (e.g., 5.0).
     * @return A new VariableGameTime instance (e.g., "5.0t").
     */
    public static VariableGameTime create(double time) {
        return new VariableGameTime(time, TimeUnit.TICK);
    }

    /**
     * Creates a VariableGameTime instance with an integer value and a specified unit.
     *
     * @param time The time value (e.g., 10).
     * @param unit The unit (e.g., TimeUnit.DAY).
     * @return A new VariableGameTime instance.
     */
    public static VariableGameTime create(int time, TimeUnit unit) {
        // Cast int to double for storage
        return new VariableGameTime((double)time, unit);
    }

    /**
     * Creates a VariableGameTime instance with an integer value, defaulting the unit to TICK.
     *
     * @param time The time value (e.g., 24000).
     * @return A new VariableGameTime instance (e.g., "24000t").
     */
    public static VariableGameTime create(int time) {
        // Cast int to double for storage
        return new VariableGameTime((double)time, TimeUnit.TICK);
    }

    /**
     * Implements the {@link GameTime} contract. Constructs the final time string
     * by concatenating the time value and the unit abbreviation.
     *
     * @return The formatted time string (e.g., "10d", "24000t").
     */
    @Override
    public String getTime() {
        return time + unit.toString();
    }

    /**
     * Returns the formatted time string, suitable for direct insertion into a command.
     *
     * @return The formatted time string.
     */
    @Override
    public String toString() {
        return getTime();
    }
}