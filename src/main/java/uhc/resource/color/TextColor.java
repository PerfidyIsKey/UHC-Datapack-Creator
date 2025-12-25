package uhc.resource.color;

import java.util.Locale;

/**
 * 🎨 **Standard Minecraft Text Colors**
 * <p>
 * Defines the 16 legacy colors used in Minecraft's chat and JSON text components.
 * These identifiers are compatible with the {@code "color"} field in text JSON
 * and standard command arguments.
 * </p>
 */
public enum TextColor implements ColorType {

    // --- 🔴 Red & Warm Tones ---

    /** Dark Red color. Usually associated with danger or high-level errors. */
    DARK_RED,
    /** Standard Red color. Used for generic errors or negative feedback. */
    RED,
    /** Gold color. Frequently used for player names or special items. */
    GOLD,
    /** Standard Yellow color. Commonly used for generic highlights. */
    YELLOW,

    // --- 🟢 Green & Nature Tones ---

    /** Dark Green color. Used for environmental messages. */
    DARK_GREEN,
    /** Standard Green color. Often used for success messages. */
    GREEN,

    // --- 🔵 Blue & Aquatic Tones ---

    /** Aqua color. A bright, light blue/cyan. */
    AQUA,
    /** Dark Aqua color. Used for technical info or team names. */
    DARK_AQUA,
    /** Dark Blue color. Can be difficult to read on dark backgrounds. */
    DARK_BLUE,
    /** Standard Blue color. Used for informational messages. */
    BLUE,

    // --- 🟣 Purple & Special Tones ---

    /** Light Purple color (Pink). Used for rare items and achievements. */
    LIGHT_PURPLE,
    /** Dark Purple color. Used for mystical or ender-related themes. */
    DARK_PURPLE,

    // --- ⚪ Grayscale & Neutral Tones ---

    /** Pure White color. The default color for chat. */
    WHITE,
    /** Gray color. Used for secondary information or "lore" text. */
    GRAY,
    /** Dark Gray color. Used for technical headers or disabled states. */
    DARK_GRAY,
    /** Pure Black color. Typically only used for high-contrast UI elements. */
    BLACK;

    // --- 🛰️ ColorType Implementation ---

    /**
     * Retrieves the raw string representation of the color.
     * <p><b>Error Catching:</b> Uses {@code Locale.ROOT} to ensure that case conversion
     * does not behave unexpectedly on systems with different language rules
     * (e.g., the Turkish 'I' bug).</p>
     * @return The lowercase Minecraft color name (e.g., "dark_red").
     */
    @Override
    public String getColor() {
        // Safe conversion of enum name to minecraft-readable path
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Performs a syntax check on the color identifier.
     * <p><b>Error Catching:</b> Ensures the generated name matches the required
     * regex pattern for Minecraft identifiers.</p>
     * @throws IllegalStateException if the generated color name is invalid.
     */
    @Override
    public void validate() throws IllegalStateException {
        String color = getColor();
        if (!VALID_COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalStateException("Internal TextColor mapping failed for: " + color);
        }
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a TextColor from a raw string identifier.
     * <p><b>Error Catching:</b> Performs a case-insensitive lookup. If the input is null,
     * blank, or unrecognized, it defaults to {@link #WHITE} to ensure text
     * remains readable.</p>
     * @param input The raw color name (e.g., "dark_red" or "RED").
     * @return The matching {@link TextColor}, or {@link #WHITE} as a fallback.
     */
    public static TextColor fromString(String input) {
        if (input == null || input.isBlank()) {
            return WHITE;
        }

        String target = input.toUpperCase(Locale.ROOT).trim();
        try {
            return TextColor.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch mistyped colors and return the default chat color
            return WHITE;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the official Minecraft color name for use in command concatenation.
     * @return The result of {@link #getColor()}.
     */
    @Override
    public String toString() {
        return getColor();
    }
}