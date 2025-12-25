package uhc.resource.color;

import java.util.regex.Pattern;

/**
 * 🎨 **Color Contract Interface**
 * <p>
 * Defines the foundational contract for any class representing a color within
 * command arguments, NBT data, or JSON components.
 * </p>
 * <p>
 * This interface bridges the gap between simple named colors (e.g., "red")
 * and modern Hexadecimal colors (e.g., "#FF5555"), ensuring that any
 * color used in the engine can be converted to a string format compatible
 * with Minecraft's text engine.
 * </p>
 */
public interface ColorType {

    // --- ⚙️ Validation Constants ---

    /** * Regex pattern to validate standard Minecraft color names or hex codes.
     * Matches lowercase words (red, blue) or hex strings starting with #.
     */
    Pattern VALID_COLOR_PATTERN = Pattern.compile("^[a-z_]+$|^#[0-9a-fA-F]{6}$");

    // --- 🛰️ Core Contract Methods ---

    /**
     * Retrieves the raw string representation of the color.
     * <p>
     * <b>Implementations:</b>
     * <ul>
     * <li>Named colors (e.g., {@code TextColor.RED}) return {@code "red"}.</li>
     * <li>Custom hex colors (e.g., {@code HexColor}) return {@code "#RRGGBB"}.</li>
     * </ul>
     * </p>
     * @return The raw, non-null string value of the color.
     */
    String getColor();

    /**
     * Performs a syntax check on the color string to prevent command injection
     * or malformed JSON components.
     * <p><b>Error Catching:</b> Validates that the color is either a valid
     * name (lowercase letters) or a valid 6-digit hex code.</p>
     * @throws IllegalStateException if the color format is invalid.
     */
    default void validate() throws IllegalStateException {
        String color = getColor();
        if (color == null || color.isBlank()) {
            throw new IllegalStateException("Color value cannot be null or empty.");
        }

        if (!VALID_COLOR_PATTERN.matcher(color).matches()) {
            throw new IllegalStateException("Invalid color format: '" + color +
                    "'. Must be a named color or a hex code (e.g., #FFFFFF).");
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the string representation of the color for seamless integration
     * with {@link StringBuilder} and string concatenation.
     * <p><b>Implementation:</b> By default, returns the result of {@link #getColor()}.</p>
     * @return The string value of the color.
     */
    @Override
    String toString();
}