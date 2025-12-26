package uhc.text.color;

import java.util.Objects;

/**
 * 🎨 **Hexadecimal RGB Color Representation**
 * <p>
 * Represents a specific color defined by an RGB hexadecimal code (e.g., {@code #RRGGBB}).
 * This class ensures that custom colors used in chat, bossbars, or leather armor
 * NBT are properly formatted before being passed to the Minecraft engine.
 * </p>
 */
public class HexColor implements ColorType {

    // --- ⚙️ Internal State ---

    /** * The validated hexadecimal string, including the leading '#' symbol.
     * Guaranteed to be either 7 or 9 characters long and contain only valid hex digits.
     */
    private final String hex;

    // --- 🏗️ Constructors ---

    /**
     * **Private Internal Constructor**
     * <p>Stores the already-validated hexadecimal string. Instances must be
     * created via the static factory {@link #of(String)}.</p>
     * @param hex The validated hex string.
     */
    private HexColor(String hex) {
        this.hex = hex;
    }

    // --- 🏭 Static Factory Methods ---

    /**
     * **Safe Factory Method**
     * <p>Creates and validates a new HexColor instance. If the leading '#' is missing,
     * it is automatically prepended.</p>
     * <p><b>Error Catching:</b>
     * <ul>
     * <li>Throws {@link IllegalArgumentException} if length is not 7 or 9.</li>
     * <li>Throws {@link IllegalArgumentException} if non-hex characters are present.</li>
     * <li>Handles null or empty inputs gracefully with clear error messages.</li>
     * </ul>
     * </p>
     * @param hex The raw hex string (e.g., "FF5555" or "#FF5555").
     * @return A immutable, validated {@link HexColor} instance.
     * @throws NullPointerException if the hex string is null.
     * @throws IllegalArgumentException if the format is invalid.
     */
    public static HexColor create(String hex) {
        String input = Objects.requireNonNull(hex, "Hex color string cannot be null.").trim();

        if (input.isEmpty()) {
            throw new IllegalArgumentException("Hex color string cannot be empty.");
        }

        // 1. Ensure the leading '#' is present
        String formattedHex = input.startsWith("#") ? input : "#" + input;

        // 2. Validate length (Minecraft supports #RRGGBB or #AARRGGBB in certain NBT/UI contexts)
        int length = formattedHex.length();
        if (length != 7 && length != 9) {
            throw new IllegalArgumentException("Invalid hex length: " + length + ". Must be 7 (#RRGGBB) or 9 (#AARRGGBB).");
        }

        // 3. Character validation using parsing test
        try {
            // Use Long to prevent overflow errors on 8-digit (alpha) hex codes
            Long.parseLong(formattedHex.substring(1), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Hex color '" + formattedHex + "' contains invalid non-hexadecimal characters.", e);
        }

        return new HexColor(formattedHex);
    }

    // --- 🛰️ ColorType Implementation ---

    /**
     * Retrieves the raw hexadecimal string for use in JSON text components or commands.
     * @return The hex code string (e.g., "#808080").
     */
    @Override
    public String getColor() {
        return hex;
    }

    /**
     * Performs a final verification that the stored hex string matches the required pattern.
     * <p><b>Error Catching:</b> This provides a second layer of defense using the regex
     * defined in the {@link ColorType} interface.</p>
     * @throws IllegalStateException if the stored hex is malformed.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (!VALID_COLOR_PATTERN.matcher(hex).matches()) {
            throw new IllegalStateException("Internal HexColor state is malformed: " + hex);
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the hex code string for seamless integration with string concatenation.
     * @return The result of {@link #getColor()}.
     */
    @Override
    public String toString() {
        return getColor();
    }
}