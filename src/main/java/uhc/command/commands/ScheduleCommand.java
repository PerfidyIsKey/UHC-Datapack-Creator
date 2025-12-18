package uhc.command.commands;

import uhc.arguments.time.VariableGameTime;
import uhc.command.MinecraftCommand;
import uhc.components.FunctionName;

/**
 * ⏰ **Schedule Command Builder**
 * <p>
 * Generates the {@code /schedule} command used to delay the execution of functions.
 * Supports:
 * <ul>
 * <li>{@code /schedule function <function> <time> [append|replace]}</li>
 * <li>{@code /schedule clear <function>}</li>
 * </ul>
 * </p>
 */
public class ScheduleCommand implements MinecraftCommand {
    private final ScheduleAction action;
    private final FunctionName function;
    private VariableGameTime time;
    private ScheduleMode mode;

    /**
     * Private constructor used by static factory methods.
     */
    private ScheduleCommand(ScheduleAction action, FunctionName function) {
        this.action = action;
        this.function = function;
    }

    /**
     * Starts a command to schedule a function execution.
     * <p>Syntax: {@code /schedule function <function> <time> [append|replace]}</p>
     */
    public static ScheduleCommand function(FunctionName function, VariableGameTime time) {
        ScheduleCommand cmd = new ScheduleCommand(ScheduleAction.FUNCTION, function);
        cmd.time = time;
        return cmd;
    }

    /**
     * Starts a command to clear a scheduled function.
     * <p>Syntax: {@code /schedule clear <function>}</p>
     */
    public static ScheduleCommand clear(FunctionName function) {
        return new ScheduleCommand(ScheduleAction.CLEAR, function);
    }

    /**
     * Sets the scheduling mode (APPEND or REPLACE).
     * <p>Only applicable when using {@link #function(FunctionName, VariableGameTime)}.</p>
     */
    public ScheduleCommand mode(ScheduleMode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public String generate() {
        if (function == null) {
            throw new IllegalStateException("Function name is required for /schedule");
        }

        StringBuilder sb = new StringBuilder("schedule ");
        sb.append(action.toString()).append(" ");

        // Both 'function' and 'clear' subcommands take the function name next
        sb.append(function.getIdentifier());

        if (action == ScheduleAction.FUNCTION) {
            if (time == null) {
                throw new IllegalStateException("Time is required when scheduling a function.");
            }

            sb.append(" ").append(time.toString());

            if (mode != null) {
                sb.append(" ").append(mode.toString());
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Internal actions for the schedule command.
     */
    private enum ScheduleAction {
        FUNCTION, CLEAR;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    /**
     * Modes for scheduling a function.
     */
    public enum ScheduleMode {
        /** Adds a new schedule even if one already exists for this function. */
        APPEND,
        /** Overwrites any existing schedule for this function. (Default behavior) */
        REPLACE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}