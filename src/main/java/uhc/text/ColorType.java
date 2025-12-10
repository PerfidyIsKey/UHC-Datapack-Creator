package uhc.text;

/**
 * Defines the contract for any class used to represent a color within command arguments.
 * Implementations must provide the raw color value as a string, suitable for direct
 * use or for prefixing (e.g., "red" or "#RRGGBB").
 */
public interface ColorType {

    /**
     * Retrieves the raw string representation of the color.
     * <p>
     * For named colors (like {@code TextColor}), this returns the name (e.g., "red").
     * For custom colors (like {@code HexColor}), this returns the code (e.g., "#808080").
     * * @return The string value of the color.
     */
    String getColor();

    /**
     * Returns the string representation of the color. In this context, it should
     * typically be the same as {@code getColor()}.
     * * @return The string value of the color.
     */
    @Override
    String toString();
}