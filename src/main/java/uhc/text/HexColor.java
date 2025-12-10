package uhc.text;

/**
 * Represents a color defined by an RGB hexadecimal code (e.g., #RRGGBB).
 * This class implements the {@code ColorType} interface and ensures the hex code is validated upon creation.
 */
public class HexColor implements ColorType{
    private final String hex;

    /**
     * Private constructor that stores the validated hexadecimal string.
     * @param hex The validated hexadecimal color code, including the leading '#'.
     */
    private HexColor(String hex) {
        this.hex = hex;
    }

    /**
     * Static factory method to create a HexColor instance with input validation.
     * * @param hex The hexadecimal color code. Must be 7 characters long (including the '#').
     * Example formats: "#RRGGBB" or "#rrggbbaa" (if alpha is supported by the command).
     * @return A new, validated HexColor instance.
     * @throws IllegalArgumentException if the hex string is null, empty, or fails validation checks.
     */
    public static HexColor create(String hex) {
        if (hex == null || hex.trim().isEmpty()) {
            throw new IllegalArgumentException("Hex color string cannot be null or empty.");
        }

        String trimmedHex = hex.trim();

        // 1. Check for leading '#'. If missing, prepend it.
        if (!trimmedHex.startsWith("#")) {
            trimmedHex = "#" + trimmedHex;
        }

        // 2. Validate length (7 characters for #RRGGBB or 9 for #RRGGBBAA)
        if (trimmedHex.length() != 7 && trimmedHex.length() != 9) {
            throw new IllegalArgumentException("Hex color must be 7 characters (#RRGGBB) or 9 characters (#RRGGBBAA). Provided length: " + trimmedHex.length());
        }

        // 3. Validate characters (simple check for hex digits after the #)
        try {
            // Attempt to parse the digits after the '#'
            Integer.parseInt(trimmedHex.substring(1), 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Hex color contains invalid characters. Must contain only hex digits (0-9, A-F) after the '#'.", e);
        }

        return new HexColor(trimmedHex);
    }

    /**
     * Retrieves the raw hexadecimal string.
     * @return The hex code string (e.g., "#808080").
     */
    @Override
    public String getColor() {
        return hex;
    }

    /**
     * Returns the hex code string. This is typically used when concatenating the command string.
     * @return The hex code string (e.g., "#808080").
     */
    @Override
    public String toString() {
        return getColor();
    }
}