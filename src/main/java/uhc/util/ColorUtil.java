package uhc.util;

import java.util.Objects;

/**
 * 🎨 **Color Utility Class**
 * <p>
 * Provides high-performance utility methods for color conversions, specifically
 * for Minecraft NBT integer color requirements.
 * </p>
 */
public final class ColorUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private ColorUtil() {}

    /**
     * Converts a HEX color string into a decimal integer.
     * <p>
     * This is required for Minecraft components like {@code custom_color}
     * which store RGB values as a single 24-bit integer.
     * </p>
     *
     * @param hex The HEX string (e.g., "#FF5555", "55FF55").
     * @return The decimal representation of the color.
     * @throws NullPointerException     If the input string is null.
     * @throws IllegalArgumentException If the HEX string is malformed or contains invalid characters.
     */
    public static int hexToInt(String hex) {
        Objects.requireNonNull(hex, "HEX color string cannot be null.");

        // Remove the # prefix if it exists
        String cleanHex = hex.startsWith("#") ? hex.substring(1) : hex;

        // Validation: Minecraft expects RRGGBB (6 characters)
        if (cleanHex.length() != 6) {
            throw new IllegalArgumentException(String.format(
                    "Invalid HEX length for '%s'. Expected 6 characters, got %d.",
                    hex, cleanHex.length()
            ));
        }

        try {
            // Radix 16 for hexadecimal conversion
            return Integer.parseInt(cleanHex, 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(
                    "HEX string '%s' contains invalid non-hexadecimal characters.",
                    hex
            ));
        }
    }

    /**
     * Converts a decimal integer back to a HEX string.
     * @param color The decimal integer color.
     * @return A HEX string in the format "#RRGGBB".
     */
    public static String intToHex(int color) {
        // Mask to 24-bit to prevent alpha channel interference if passed from ARGB
        return String.format("#%06X", (0xFFFFFF & color));
    }
}