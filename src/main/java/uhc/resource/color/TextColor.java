package uhc.resource.color;

import uhc.text.color.ColorType;
import uhc.util.NameConversion;

import java.util.Locale;
import java.util.Objects;

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
    DARK_RED,
    RED,
    GOLD,
    YELLOW,

    // --- 🟢 Green & Nature Tones ---
    DARK_GREEN,
    GREEN,

    // --- 🔵 Blue & Aquatic Tones ---
    AQUA,
    DARK_AQUA,
    DARK_BLUE,
    BLUE,

    // --- 🟣 Purple & Special Tones ---
    LIGHT_PURPLE,
    DARK_PURPLE,

    // --- ⚪ Grayscale & Neutral Tones ---
    WHITE,
    GRAY,
    DARK_GRAY,
    BLACK;

    // --- 🛠️ New Conversion Methods ---

    /**
     * Converts the enum constant name into a PascalCase string.
     * <p>
     * <b>Example:</b> {@code LIGHT_PURPLE.pascal()} returns {@code "LightPurple"}.
     * </p>
     * @return The PascalCase representation of this color.
     * @throws RuntimeException if the underlying {@link NameConversion} fails.
     */
    public String pascal() {
        try {
            // Uses the NameConversion utility to transform SCREAMING_SNAKE_CASE
            return NameConversion.sssToPascal(this.name());
        } catch (Exception e) {
            throw new RuntimeException("TextColor Error: Failed to convert '" + this.name() + "' to PascalCase. " + e.getMessage(), e);
        }
    }

    // --- 🛰️ ColorType Implementation ---

    /**
     * Retrieves the raw string representation of the color.
     * @return The lowercase Minecraft color name (e.g., "dark_red").
     */
    @Override
    public String getColor() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Performs a syntax check on the color identifier.
     * @throws IllegalStateException if the generated color name is invalid.
     */
    @Override
    public void validate() throws IllegalStateException {
        final String color = getColor();
        if (!VALID_COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalStateException("Internal TextColor mapping failed for: " + color);
        }
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Strictly retrieves a TextColor from a raw string identifier.
     * <p>
     * <b>Updated Policy:</b> This method no longer falls back to WHITE. It will
     * throw an exception if the input is invalid to ensure data integrity.
     * </p>
     * @param input The raw color name (e.g., "dark_red").
     * @return The matching {@link TextColor}.
     * @throws NullPointerException if input is null.
     * @throws IllegalArgumentException if the color is blank or unrecognized.
     */
    public static TextColor fromString(String input) {
        Objects.requireNonNull(input, "TextColor Lookup Error: Input cannot be null.");

        if (input.isBlank()) {
            throw new IllegalArgumentException("TextColor Lookup Error: Input cannot be blank.");
        }

        final String target = input.toUpperCase(Locale.ROOT).trim();
        try {
            return TextColor.valueOf(target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("TextColor Lookup Error: Unrecognized color identifier '" + input + "'.");
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