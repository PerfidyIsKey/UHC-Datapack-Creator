package uhc.command.commands;

import uhc.arguments.time.GameTime;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * ☀️ **Time Command Builder**
 * <p>
 * Provides a fluent API for the {@code /time} command.
 * This command manages the server's day/night cycle and game time.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /time <add|query|set> <value>}
 * </p>
 */
public class TimeCommand implements MinecraftCommand {

    // The required action (ADD, QUERY, or SET).
    private final TimeAction action;

    // The required time argument (e.g., 24000, 'day', 'gametime').
    private final GameTime time;

    private TimeCommand(TimeAction action, GameTime time) {
        // Enforce non-nullability for mandatory command components
        this.action = Objects.requireNonNull(action, "TimeAction cannot be null.");
        this.time = Objects.requireNonNull(time, "GameTime argument cannot be null.");
    }

    /**
     * Static factory method to create a Time command instance.
     *
     * @param action The operation to perform: ADD, QUERY, or SET.
     * @param time The time value (tick count/keyword) or query target (daytime/gametime/day).
     * @return A new Time command instance.
     */
    public static TimeCommand create(TimeAction action, GameTime time) {
        return new TimeCommand(action, time);
    }

    /**
     * Constructs the final Minecraft command string.
     * @return The formatted command (e.g., "time set day" or "time query gametime").
     */
    @Override
    public String generate() {
        // time <action> <time>
        return "time " + action + " " + time;
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Defines the three possible operations for the Minecraft /time command.
     */
    public enum TimeAction {

        /** Increments the current world time by the specified amount. */
        ADD,

        /** Returns the value of a specific time counter (daytime, gametime, or day). */
        QUERY,

        /** Sets the world time to a specific value or keyword (e.g., noon, night). */
        SET;

        /**
         * Returns the lowercase name as required by Minecraft command syntax.
         */
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}