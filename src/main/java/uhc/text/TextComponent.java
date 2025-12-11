package uhc.text;

import uhc.arguments.entity.Entity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 💬 **Minecraft Text Component Builder**
 * <p>
 * Utility class for creating Minecraft Text Component strings (JSON format), used in commands
 * like {@code /title}, {@code /tellraw}, and for item names/lore.
 * </p>
 * This class handles:
 * <ul>
 * <li>Simple raw strings.</li>
 * <li>Complex components with formatting, colors (named or hex), and boolean styles.</li>
 * <li>Click/Hover event integration.</li>
 * <li>Entity selector components.</li>
 * <li>Combining multiple components into a JSON array.</li>
 * </ul>
 */
public class TextComponent {

    // --- Core Methods ---

    /**
     * Creates a simple, unformatted Minecraft text component string.
     * <p>
     * Note: This method currently returns the plain string itself, as commands like {@code /say}
     * or {@code /title} often accept raw text without JSON wrapping.
     * </p>
     * @param text The plain string to display.
     * @return The plain string itself.
     * @throws IllegalArgumentException if text content is null.
     */
    public static String simple(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text content cannot be null.");
        }
        return text;
    }

    /**
     * Creates a single JSON component string with a click event.
     * <p>
     * Example generated JSON: {@code {"text":"Click Me","click_event":{"action":"run_command","command":"/say Hi"}}}
     * </p>
     * @param text The display text.
     * @param action The click event action (e.g., "run_command", "suggest_command").
     * @param command The value for the action (e.g., the command string to run).
     * @return A raw JSON string for a single component.
     * @throws IllegalArgumentException if any argument is null.
     */
    public static String withClickCommand(String text, String action, String command) {
        if (text == null || action == null || command == null) {
            throw new IllegalArgumentException("Text, action, and command must be non-null.");
        }
        // Properly escape quotes for inclusion within the JSON strings
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
     * This format is mandatory for item names, lore, and often used in complex title commands.
     * <p>
     * Note: Input strings are assumed to be valid component JSON (e.g., from {@code complex()} or {@code selector()}).
     * </p>
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

    /**
     * Overload: Combines a mix of simple strings and raw JSON components into a single JSON array string.
     * <p>
     * Simple strings are automatically wrapped in minimal {@code {"text":"..."}} JSON components.
     * </p>
     * @param components A list of objects that are either raw JSON strings (complex components) or plain strings.
     * @return A raw JSON array string.
     */
    public static String arrayFromMixed(List<Object> components) {
        if (components == null || components.isEmpty()) {
            return "[]";
        }

        // Convert the list of mixed objects into a list of raw JSON strings
        List<String> rawJsonStrings = components.stream()
                .map(obj -> {
                    if (obj == null) return null;
                    String str = obj.toString();

                    // Heuristic check: if it doesn't look like JSON (doesn't start with '{'), wrap it.
                    if (str.startsWith("{")) {
                        return str; // Already looks like raw JSON component
                    } else {
                        // Wrap plain string in a minimal JSON component
                        String escapedText = str.replace("\"", "\\\"");
                        return String.format("{\"text\":\"%s\"}", escapedText);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return "[" + String.join(",", rawJsonStrings) + "]";
    }


    // --- Complex Component Methods ---

    // Base Method
    /**
     * Creates a single JSON text component string with full formatting options using a raw color string.
     * <p>
     * This method is the base for all complex text components, handling named colors, hex codes,
     * and all boolean formatting arguments (null indicates the property is omitted from the JSON).
     * </p>
     * @param text The display text.
     * @param colorString The color string (e.g., "red", or hex code "#RRGGBB"). Null to omit color.
     * @param bold True/False to set bold, null to omit.
     * @param italic True/False to set italic, null to omit.
     * @param obfuscated True/False to set obfuscated, null to omit.
     * @return A raw JSON string for a single component.
     * @throws IllegalArgumentException if text content is null.
     */
    public static String complex(
            String text,
            String colorString,
            Boolean bold,
            Boolean italic,
            Boolean obfuscated
    ) {
        if (text == null) {
            throw new IllegalArgumentException("Text content cannot be null.");
        }
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
        String colorString = (color != null) ? color.toString() : null; // Use toString() for safety
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
            TextColor color,
            Boolean bold,
            Boolean italic,
            Boolean obfuscated
    ) {
        // Calls the base method with the enum's Minecraft name string
        String colorString = (color != null) ? color.toString() : null; // Use toString() for safety
        return complex(text, colorString, bold, italic, obfuscated);
    }

    /**
     * Overload: Creates a complex component using a type-safe HexColor and full formatting options.
     */
    public static String complex(
            String text,
            HexColor color,
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
     * <p>
     * This component uses the {@code "selector"} key in the JSON.
     * All formatting arguments are optional (can be null).
     * </p>
     * @param target The target selector entity argument.
     * @param color The type-safe color enum.
     * @param bold True/False to set bold, null to omit.
     * @param italic True/False to set italic, null to omit.
     * @param obfuscated True/False to set obfuscated, null to omit.
     * @return A raw JSON string for a single component.
     * @throws IllegalArgumentException if entity target is null.
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

        // The toString() method of TextColor should provide the Minecraft color name (e.g., "red")
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