package uhc.text;

import java.util.Locale;

/**
 * Defines standard Minecraft text colors used in JSON text components (1.16+ format).
 * The enum constants' names automatically map to the official lowercase color names expected by the game's JSON parser.
 */
public enum TextColor implements ColorType {
    DARK_RED,
    RED,
    GOLD,
    YELLOW,
    DARK_GREEN,
    GREEN,
    AQUA,
    DARK_AQUA,
    DARK_BLUE,
    BLUE,
    LIGHT_PURPLE,
    DARK_PURPLE,
    WHITE,
    GRAY,
    DARK_GRAY,
    BLACK;

    /**
     * Retrieves the raw string representation of the color.
     * This method automatically converts the enum constant's name (e.g., DARK_RED)
     * into the required lowercase Minecraft string (e.g., "dark_red").
     * * @return The Minecraft-specific color name (e.g., "dark_red").
     */
    @Override
    public String getColor() {
        // Use Locale.ROOT for consistent, language-independent case conversion,
        // which is standard practice for technical identifiers like Minecraft color names.
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Overrides the default toString to return the official Minecraft color name,
     * making the enum usable directly in command string concatenation.
     * * @return The Minecraft-specific color name.
     */
    @Override
    public String toString() {
        return getColor();
    }
}