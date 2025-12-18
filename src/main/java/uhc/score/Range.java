package uhc.score;

import java.util.Objects;

/**
 * 🔢 **Integer Range Argument**
 * <p>
 * Represents a 32-bit integer range used in selectors, scoreboard checks, and predicates.
 * Supports exact matches, bounds (min/max), and inclusive spans.
 * </p>
 */
public class Range {
    private final String value;

    private Range(String value) {
        this.value = value;
    }

    /**
     * Creates an exact match range (e.g., "5").
     */
    public static Range exact(int value) {
        return new Range(String.valueOf(value));
    }

    /**
     * Creates a "less than or equal to" range (e.g., "..100").
     */
    public static Range max(int max) {
        return new Range(".." + max);
    }

    /**
     * Creates a "greater than or equal to" range (e.g., "0..").
     */
    public static Range min(int min) {
        return new Range(min + "..");
    }

    /**
     * Creates an inclusive span between two integers (e.g., "0..5").
     * @throws IllegalArgumentException if min is greater than max.
     */
    public static Range between(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than max value in a range.");
        }
        return new Range(min + ".." + max);
    }

    /**
     * Parses a string into a Range object.
     * Useful for loading values from external configs.
     */
    public static Range parse(String input) {
        Objects.requireNonNull(input, "Range input cannot be null.");
        // Basic validation for Minecraft range syntax
        if (!input.matches("^-?\\d*(\\.\\.-?\\d*)?$") || input.equals("..")) {
            throw new IllegalArgumentException("Invalid Minecraft range format: " + input);
        }
        return new Range(input);
    }

    @Override
    public String toString() {
        return value;
    }
}