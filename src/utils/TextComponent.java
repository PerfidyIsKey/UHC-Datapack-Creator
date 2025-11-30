package utils;

import arguments.Entity;
import shared.TextColor;
import shared.HexColor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for creating Minecraft Text Component strings (JSON format), used in commands
 * like {@code /title} and for item names/lore.
 * This class handles formatting, click events, and entity selectors.
 */
public class TextComponent {

    // --- Core Methods ---

    /**
     * Creates a simple, unformatted Minecraft text component string.
     * In the context of commands (like /title), a raw string is often sufficient
     * but this method provides a uniform way to handle simple content.
     * @param text The plain string to display.
     * @return The plain string itself.
     */
    public static String simple(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text content cannot be null.");
        }
        return text;
    }

    /**
     * Creates a complex component with a click event (returns raw JSON string).
     * @param text The display text.
     * @param action The click event action (e.g., "run_command", "suggest_command").
     * @param command The value for the action (e.g., the command string to run).
     * @return A raw JSON string.
     */
    public static String withClickCommand(String text, String action, String command) {
        if (text == null || action == null || command == null) {
            throw new IllegalArgumentException("Text, action, and command must be non-null.");
        }
        String escapedText = text.replace("\"", "\\\"");
        String escapedCommand = command.replace("\"", "\\\"");

        return String.format(
                "{\"text\":\"%s\",\"click_event\":{\"action\":\"%s\",\"command\":\"%s\"}}",
                escapedText, action, escapedCommand
        );
    }

    // --- Array Utility ---

    /**
     * Combines multiple raw JSON text component strings into a single JSON array string.
     * This format is used for item names, lore, and sometimes in complex title components.
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

    // --- Complex Component Methods ---

    // Base Method
    /**
     * Creates a single JSON text component string with full formatting options using a raw color string.
     * This is the base method that handles named colors, hex codes, and all formatting booleans.
     * All boolean arguments are optional (can be null).
     */
    public static String complex(
            String text,
            String colorString, // Handles named color or hex string (e.g., "red" or "#RRGGBB")
            Boolean bold,
            Boolean italic,
            Boolean obfuscated
    ) {
        if (text == null) {
            throw new IllegalArgumentException("Text content cannot be null.");
        }
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

    // Convenience Overloads

    /**
     * Overload: Creates a complex component using a type-safe TextColor enum and no formatting options.
     */
    public static String complex(String text, TextColor color) {
        // Calls the base method with the enum's Minecraft name string
        String colorString = (color != null) ? color.getColor() : null;
        return complex(text, colorString, null, null, null);
    }

    /**
     * Overload: Creates a complex component using a type-safe HexColor and no formatting options.
     */
    public static String complex(String text, HexColor color) {
        // Calls the base method with the HexColor string
        String colorString = (color != null) ? color.getColor() : null;
        return complex(text, colorString, null, null, null);
    }

    /**
     * Overload: Creates a complex component using a type-safe TextColor enum and full formatting options.
     */
    public static String complex(
            String text,
            TextColor color, // Uses the type-safe enum
            Boolean bold,
            Boolean italic,
            Boolean obfuscated
    ) {
        // Calls the base method with the enum's Minecraft name string
        String colorString = (color != null) ? color.getColor() : null;
        return complex(text, colorString, bold, italic, obfuscated);
    }

    /**
     * Overload: Creates a complex component using a type-safe HexColor and full formatting options.
     */
    public static String complex(
            String text,
            HexColor color, // Uses the type-safe class
            Boolean bold,
            Boolean italic,
            Boolean obfuscated
    ) {
        // Calls the base method with the HexColor string
        String colorString = (color != null) ? color.getColor() : null;
        return complex(text, colorString, bold, italic, obfuscated);
    }


    // --- Selector Component Methods ---

    /**
     * Creates a text component that displays the name of the entity specified by the target selector.
     * All formatting arguments are optional (can be null).
     */
    public static String selector(Entity target,
                                  TextColor color,
                                  Boolean bold,
                                  Boolean italic,
                                  Boolean obfuscated
    ) {
        if (target == null) {
            throw new IllegalArgumentException("Entity target for selector cannot be null.");
        }

        StringBuilder sb = new StringBuilder("{");
        // "selector" field uses the raw entity selector string
        sb.append("\"selector\":\"").append(target).append("\"");

        if (color != null) {
            sb.append(",\"color\":\"").append(color).append("\"");
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

    /**
     * Convenience overload for a simple, unformatted selector component.
     */
    public static String selector(Entity target) {
        return selector(target, null, null, null, null);
    }
}