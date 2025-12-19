package uhc.command.commands;

import uhc.command.MinecraftCommand;
import uhc.score.Range;

import java.util.Objects;

/**
 * 🎲 **Random Command Builder**
 * <p>
 * Provides a fluent API for the {@code /random} command.
 * This version uses the {@link Range} class to handle numeric spans.
 * </p>
 */
public class RandomCommand implements MinecraftCommand {
    private final RandomAction action;
    private Range range;

    private RandomCommand(RandomAction action) {
        this.action = Objects.requireNonNull(action, "Random action cannot be null.");
    }

    /**
     * Initializes a new RandomCommand builder.
     * @param action The operation mode (VALUE, ROLL, or RESET).
     */
    public static RandomCommand create(RandomAction action) {
        return new RandomCommand(action);
    }

    /**
     * Sets the range for the random number generation using a {@link Range} object.
     * <p>Note: Minecraft's /random command requires a <b>full span</b> (min and max).
     * Open-ended ranges (e.g., "5.." or "..10") will cause a command error.</p>
     * * @param range The inclusive range (e.g., Range.between(1, 10)).
     * @return The current builder instance.
     * @throws IllegalStateException if called on a RESET action.
     * @throws IllegalArgumentException if the range is open-ended.
     */
    public RandomCommand range(Range range) {
        if (action == RandomAction.RESET) {
            throw new IllegalStateException("The 'reset' action does not accept a range.");
        }

        if (range != null) {
            String val = range.toString();
            // Validation: Ensure the range contains ".." and has numbers on both sides.
            // Open-ended ranges like "1.." or "..10" are invalid for /random.
            if (!val.contains("..") || val.startsWith("..") || val.endsWith("..")) {
                throw new IllegalArgumentException("The /random command requires a full range (min..max). " +
                        "Open-ended ranges or exact matches are not supported.");
            }
        }

        this.range = range;
        return this;
    }

    /**
     * Legacy support for integer inputs, internally converting to a Range object.
     */
    public RandomCommand range(int min, int max) {
        return range(Range.between(min, max));
    }

    /**
     * Generates the final Minecraft command string.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("random ");

        sb.append(action);

        if (action == RandomAction.VALUE || action == RandomAction.ROLL) {
            if (range == null) {
                throw new IllegalStateException("A range must be set for the action '" + action + "'.");
            }
            sb.append(" ").append(range);
        } else if (action == RandomAction.RESET) {
            // Wildcard to reset all sequences
            sb.append(" *");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    public enum RandomAction {
        RESET, ROLL, VALUE;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}