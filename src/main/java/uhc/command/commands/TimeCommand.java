package uhc.command.commands;

import uhc.arguments.time.GameTime;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * ☀️ **Time Command Builder (TimeCommand)**
 * <p>
 * Provides a semantic API for constructing the Minecraft {@code /time} command.
 * This class facilitates the management of the server's day/night cycle,
 * administrative time settings, and world age queries.
 * </p>
 * <p>
 * <b>Syntax Patterns:</b>
 * <ul>
 * <li>{@code time add <time>} - Increments current world time.</li>
 * <li>{@code time query <daytime|gametime|day>} - Retrieves internal counters.</li>
 * <li>{@code time set <day|night|noon|midnight|time>} - Hard-sets world time.</li>
 * </ul>
 * </p>
 */
public class TimeCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The specific sub-action to be performed by the command.
     * This defines whether the command is an addition, a query, or a set operation.
     */
    private final TimeAction action;

    /** * The argument associated with the action.
     * Could represent a tick value, a keyword (noon/night), or a query target (daytime).
     */
    private final GameTime time;

    // --- 🏗️ Constructors & Static Entry Points ---

    /**
     * Private constructor to enforce controlled instantiation through static factory methods.
     * <p><b>No-Fallback Policy:</b> Will throw an exception immediately if components are missing.</p>
     * * @param action The non-null {@link TimeAction} operation.
     * @param time   The non-null {@link GameTime} argument.
     * @throws NullPointerException if action or time is null.
     */
    private TimeCommand(TimeAction action, GameTime time) {
        this.action = Objects.requireNonNull(action, "Time Configuration Error: TimeAction cannot be null.");
        this.time = Objects.requireNonNull(time, "Time Configuration Error: GameTime argument cannot be null.");
    }

    /**
     * Entry point for constructing a {@code /time add} command.
     * <p>Used to advance the world clock by a specific number of ticks.</p>
     * * @param time The amount of game time to add to the current world clock.
     * @return A new TimeCommand instance configured for addition.
     * @throws NullPointerException if the time argument is null.
     */
    public static TimeCommand add(GameTime time) {
        Objects.requireNonNull(time, "Time Command Error: Addition requires a non-null GameTime value.");
        return new TimeCommand(TimeAction.ADD, time);
    }

    /**
     * Entry point for constructing a {@code /time query} command.
     * <p>Used to check the status of specific time-related counters in the world.</p>
     * * @param type The query target (typically daytime, gametime, or day).
     * @return A new TimeCommand instance configured for querying.
     * @throws NullPointerException if the query type is null.
     */
    public static TimeCommand query(GameTime type) {
        Objects.requireNonNull(type, "Time Command Error: Query requires a non-null target type (e.g., daytime).");
        return new TimeCommand(TimeAction.QUERY, type);
    }

    /**
     * Entry point for constructing a {@code /time set} command.
     * <p>Used to instantly change the world time to a fixed point or keyword.</p>
     * * @param time The specific tick value or keyword (e.g., 'night') to set.
     * @return A new TimeCommand instance configured for setting.
     * @throws NullPointerException if the time argument is null.
     */
    public static TimeCommand set(GameTime time) {
        Objects.requireNonNull(time, "Time Command Error: Set requires a non-null time value or keyword.");
        return new TimeCommand(TimeAction.SET, time);
    }

    // --- ⚙️ Command Generation ---

    /**
     * Validates internal state and generates the final Minecraft command string.
     * <p><b>Example Output:</b> {@code "time set night"} or {@code "time add 24000"}</p>
     * * @return The fully formatted Minecraft command string.
     * @throws RuntimeException if an unexpected error occurs during string assembly.
     */
    @Override
    public String generate() {
        final StringBuilder command = new StringBuilder("time ");

        try {
            command.append(this.action.toString())
                    .append(" ")
                    .append(this.time.toString());

            return command.toString();
        } catch (Exception e) {
            // Strict Error Catching: We throw a detailed error rather than returning a partial command.
            throw new RuntimeException(String.format(
                    "CRITICAL: Failed to assemble TimeCommand string. [Action: %s, Argument: %s]",
                    this.action, this.time), e);
        }
    }

    /**
     * Standard string representation of the command.
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }

    // --- 🏷️ Internal Enums ---

    /**
     * Defines the valid operations for the Minecraft {@code /time} command.
     * Each constant maps to a specific keyword in the Minecraft command syntax.
     */
    public enum TimeAction {

        /** Increments the current world time. Map to: 'add' */
        ADD,

        /** Requests the value of world counters. Map to: 'query' */
        QUERY,

        /** Sets the world time to a specific value. Map to: 'set' */
        SET;

        /**
         * Returns the command-safe lowercase name of the action.
         * @return The action as a string (e.g., "add").
         */
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}