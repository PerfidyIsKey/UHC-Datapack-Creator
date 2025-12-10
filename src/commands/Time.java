package commands;

import uhc.arguments.time.GameTime;
import commands.time.TimeAction;

/**
 * Represents the Minecraft 'time' command structure.
 * This command is used to check or modify the time of the world.
 * <p>
 * The class relies on concrete implementations of {@link GameTime}
 * (like StaticGameTime, VariableGameTime, or QueryTime) to provide the
 * correct argument format for the chosen {@link TimeAction}.
 * <p>
 * Command syntax: {@code /time <action> <value_or_target>}
 */
public class Time {

    // The required action (ADD, QUERY, or SET).
    private final TimeAction action;
    // The required time argument (e.g., tick count, 'day', 'daytime').
    private final GameTime time;

    /**
     * Private constructor to enforce instantiation via the static factory method.
     *
     * @param action The time action.
     * @param time The time value/target.
     */
    private Time(TimeAction action, GameTime time) {
        this.action = action;
        this.time = time;
    }

    /**
     * Static factory method to create a Time command instance.
     * Ensures both the action and time arguments are non-null.
     *
     * @param action The required time action (ADD, QUERY, or SET).
     * @param time The required time value or query target.
     * @return A new Time instance.
     * @throws IllegalArgumentException if {@code action} or {@code time} is null.
     */
    public static Time create(TimeAction action, GameTime time) {
        if (action == null) {
            throw new IllegalArgumentException("TimeAction cannot be null for the time command.");
        }
        if (time == null) {
            throw new IllegalArgumentException("GameTime argument cannot be null for the time command.");
        }
        return new Time(action, time);
    }

    /**
     * Constructs and returns the final string representation of the 'time' command.
     * The structure is {@code time <action> <time>}.
     *
     * @return The complete, formatted Minecraft command string.
     */
    public String build() {
        return "time " + action.toString() + " " + time.toString();
    }
}