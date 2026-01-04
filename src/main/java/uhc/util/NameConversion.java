package uhc.util;

import java.util.Objects;

/**
 * 🛠️ **Name Conversion Utility**
 * <p>
 * Provides static methods for transforming string casing formats common in Minecraft
 * development (e.g., converting Enum names to user-friendly titles).
 * </p>
 */
public final class NameConversion {

    /**
     * Private constructor to prevent instantiation of a utility class.
     * @throws UnsupportedOperationException if an attempt is made to instantiate.
     */
    private NameConversion() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * Converts a string from {@code SCREAMING_SNAKE_CASE} to {@code PascalCase}.
     * <p>
     * <b>Example:</b> {@code "FIRE_RESISTANCE"} becomes {@code "FireResistance"}.
     * </p>
     * <p>
     * <b>Strict Validation:</b>
     * <ul>
     * <li>Input must not be null.</li>
     * <li>Input must not be empty or blank.</li>
     * </ul>
     * </p>
     *
     * @param input The snake_case string to convert (e.g., an Enum name).
     * @return The resulting PascalCase string.
     * @throws NullPointerException if {@code input} is null.
     * @throws IllegalArgumentException if {@code input} is empty or contains only whitespace.
     */
    public static String sssToPascal(String input) {
        // 1. Strict Validation
        Objects.requireNonNull(input, "Conversion Error: Input string cannot be null.");

        if (input.isBlank()) {
            throw new IllegalArgumentException("Conversion Error: Input string cannot be empty or blank.");
        }

        // 2. Logic Implementation
        final StringBuilder result = new StringBuilder();
        final String[] parts = input.split("_");

        for (String part : parts) {
            if (part.isEmpty()) {
                // Skips consecutive underscores (e.g., "HEART__PIECE")
                continue;
            }

            // Capitalize the first letter, lowercase the rest
            result.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1).toLowerCase());
        }

        // 3. Output Validation
        final String finalized = result.toString();
        if (finalized.isEmpty()) {
            throw new RuntimeException("Conversion Error: Resulting PascalCase string for '" + input + "' is empty.");
        }

        return finalized;
    }
}