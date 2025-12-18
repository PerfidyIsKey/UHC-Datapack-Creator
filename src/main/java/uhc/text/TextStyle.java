package uhc.text;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 🎨 **TextStyle Builder**
 * <p>
 * A utility class to fluently construct the JSON string required by the Minecraft
 * {@code /scoreboard players display numberformat ... styled <json>} command.
 * </p>
 * This ensures the generated string is correctly formatted and quoted for use
 * directly within an MCFunction file.
 */
public class TextStyle {

    // Using LinkedHashMap to maintain the order of insertion, though not strictly required by JSON.
    private final Map<String, Object> properties = new LinkedHashMap<>();

    /**
     * Private constructor to enforce the use of the static factory method.
     */
    private TextStyle() {
        // Initializes with default properties if necessary, but here we start empty.
    }

    /**
     * Factory method to start building a new TextStyle instance.
     * @return A new TextStyle builder instance.
     */
    public static TextStyle create() {
        return new TextStyle();
    }

    // --- Formatting Methods (Fluent API) ---

    /**
     * Sets the primary text color, which can be a standard Minecraft color name or a hex code (e.g., "#FF00AA").
     * @param color The color name (e.g., "red", "gold") or a hex code string.
     * @return The builder instance for chaining.
     */
    public TextStyle color(String color) {
        this.properties.put("color", color);
        return this;
    }

    /**
     * Sets the text to bold.
     * @param bold True to enable bold, false to disable.
     * @return The builder instance for chaining.
     */
    public TextStyle bold(boolean bold) {
        this.properties.put("bold", bold);
        return this;
    }

    /**
     * Sets the text to italic.
     * @param italic True to enable italic, false to disable.
     * @return The builder instance for chaining.
     */
    public TextStyle italic(boolean italic) {
        this.properties.put("italic", italic);
        return this;
    }

    /**
     * Sets the text to be underlined.
     * @param underlined True to enable underline, false to disable.
     * @return The builder instance for chaining.
     */
    public TextStyle underlined(boolean underlined) {
        this.properties.put("underlined", underlined);
        return this;
    }

    /**
     * Sets the text to be strikethrough.
     * @param strikethrough True to enable strikethrough, false to disable.
     * @return The builder instance for chaining.
     */
    public TextStyle strikethrough(boolean strikethrough) {
        this.properties.put("strikethrough", strikethrough);
        return this;
    }

    /**
     * Sets the text to be obfuscated (randomly shifting characters).
     * @param obfuscated True to enable obfuscation, false to disable.
     * @return The builder instance for chaining.
     */
    public TextStyle obfuscated(boolean obfuscated) {
        this.properties.put("obfuscated", obfuscated);
        return this;
    }

    // --- Generation Method ---

    /**
     * Generates the final, quoted JSON string required by the Minecraft command.
     * @return The JSON string, enclosed in single quotes. Example: '{"color":"red","bold":true}'
     */
    public String generate() {
        if (properties.isEmpty()) {
            // Return an empty JSON object if no properties were set.
            return "'{}'";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("'"); // Start with a single quote for the MC command argument

        // Manually build the JSON structure to avoid external JSON libraries
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            // Append key: "key"
            sb.append("\"").append(entry.getKey()).append("\":");

            // Append value: Handle string and boolean types
            Object value = entry.getValue();
            if (value instanceof String) {
                // Append string value: "value"
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof Boolean) {
                // Append boolean value: true or false
                sb.append(value);
            } else {
                // Future-proofing for numbers/other types if needed
                sb.append(value.toString());
            }
            first = false;
        }
        sb.append("}");

        sb.append("'"); // End with a single quote

        return sb.toString();
    }

    /**
     * Returns the generated JSON string.
     */
    @Override
    public String toString() {
        return generate();
    }
}