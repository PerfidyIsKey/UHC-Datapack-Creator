package utils;

import shared.TextColor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for creating Minecraft Text Component strings (JSON format).
 * This class combines basic functionality with modern 1.20.5+ component formatting and type-safe overloads.
 */
public class TextComponent {

    // --- Core Methods (from OLD Class) ---

    /**
     * Creates a simple, unformatted Minecraft text component string.
     * @param text The plain string to display.
     * @return The plain string itself. The TagConverter will wrap this in quotes for SNBT.
     */
    public static String simple(String text) {
        // Return the plain string. The TagConverter will quote this value for SNBT.
        return text;
    }

    /**
     * Creates a complex component with a click event (returns raw JSON string).
     * @param text The display text.
     * @param action The click event action.
     * @param command The value for the action.
     * @return A raw JSON string.
     */
    public static String withClickCommand(String text, String action, String command) {
        String escapedText = text.replace("\"", "\\\"");
        String escapedCommand = command.replace("\"", "\\\"");

        return String.format(
                "{\"text\":\"%s\",\"click_event\":{\"action\":\"%s\",\"command\":\"%s\"}}",
                escapedText, action, escapedCommand
        );
    }

    // --- Array Utility (from NEW Class) ---

    /**
     * Combines multiple raw JSON text component strings into a single JSON array string.
     * This format is used for item names and lore.
     * @param components A list of individual raw JSON component strings.
     * @return A raw JSON array string (e.g., "[{...}, {...}]").
     */
    public static String array(List<String> components) {
        if (components == null || components.isEmpty()) {
            return "[]";
        }
        // Collect the raw strings and join them with a comma, wrapping the whole thing in array brackets.
        return "[" + components.stream().collect(Collectors.joining(",")) + "]";
    }

    // --- Complex Component Methods (from NEW Class) ---

    // Base Method
    /**
     * Creates a single JSON text component string with full formatting options using a raw color string.
     * This is the base method that all overloads call and handles both named colors and hex codes.
     */
    public static String complex(
            String text,
            String colorString, // Handles named color or hex string (e.g., "#RRGGBB")
            Boolean bold,
            Boolean italic,
            Boolean obfuscated
    ) {
        // Escape quotes within the text string
        String escapedText = text.replace("\"", "\\\"");

        StringBuilder sb = new StringBuilder("{");
        sb.append("\"text\":\"").append(escapedText).append("\"");

        if (colorString != null) {
            sb.append(",\"color\":\"").append(colorString).append("\"");
        }
        if (bold != null) {
            sb.append(",\"bold\":").append(bold);
        }
        if (italic != null) {
            sb.append(",\"italic\":").append(italic);
        }
        if (obfuscated != null) {
            sb.append(",\"obfuscated\":").append(obfuscated);
        }

        sb.append("}");
        return sb.toString();
    }

    // Type-safe Overload
    /**
     * Overload: Creates a complex component using a type-safe TextColor enum.
     */
    public static String complex(
            String text,
            TextColor color, // Uses the type-safe enum
            Boolean bold,
            Boolean italic,
            Boolean obfuscated
    ) {
        // Calls the base method with the enum's Minecraft name string
        String colorString = (color != null) ? color.getMinecraftName() : null;
        return complex(text, colorString, bold, italic, obfuscated);
    }
}