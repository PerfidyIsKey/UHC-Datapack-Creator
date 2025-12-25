package uhc.text;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uhc.resource.color.*;

import java.util.Objects;

/**
 * 🎨 **TextStyle Builder**
 * <p>
 * A utility class to fluently construct the JSON structure required by Minecraft
 * commands, specifically for {@code /scoreboard players display numberformat}.
 * </p>
 * <p>
 * This builder uses Jackson to ensure that the resulting JSON is syntactically
 * perfect and properly escaped for use in MCFunction files.
 * </p>
 */
public class TextStyle {

    // --- ⚙️ Static Configuration ---

    /** * Shared Jackson ObjectMapper for JSON generation. */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // --- ⚙️ Internal State ---

    /** * Jackson ObjectNode to store style properties (color, bold, etc.). */
    private final ObjectNode root;

    // --- 🏗️ Constructors ---

    /**
     * **Private Constructor**
     * <p>Initializes a new ObjectNode. Use {@link #create()} to instantiate.</p>
     */
    private TextStyle() {
        this.root = MAPPER.createObjectNode();
    }

    /**
     * **Static Factory Method**
     * <p>The entry point for the fluent Builder API.</p>
     * @return A new {@link TextStyle} instance.
     */
    public static TextStyle create() {
        return new TextStyle();
    }

    // --- 🖌️ Formatting Methods (Fluent API) ---

    /**
     * Sets the primary text color using a type-safe {@link ColorType}.
     * <p><b>Error Catching:</b> Validates the color via {@link ColorType#validate()}
     * before adding it to the JSON node.</p>
     * @param color Implementation of {@link ColorType} (e.g., {@link TextColor} or {@link HexColor}).
     * @return The builder instance for chaining.
     * @throws NullPointerException if color is null.
     */
    public TextStyle color(ColorType color) {
        Objects.requireNonNull(color, "Color cannot be null.");
        color.validate();
        this.root.put("color", color.getColor());
        return this;
    }

    /**
     * Sets the text weight to bold.
     * @param bold {@code true} to enable bold.
     * @return The builder instance for chaining.
     */
    public TextStyle bold(boolean bold) {
        this.root.put("bold", bold);
        return this;
    }

    /**
     * Sets the text style to italic.
     * @param italic {@code true} to enable italics.
     * @return The builder instance for chaining.
     */
    public TextStyle italic(boolean italic) {
        this.root.put("italic", italic);
        return this;
    }

    /**
     * Sets whether the text is underlined.
     * @param underlined {@code true} to enable underline.
     * @return The builder instance for chaining.
     */
    public TextStyle underlined(boolean underlined) {
        this.root.put("underlined", underlined);
        return this;
    }

    /**
     * Sets whether the text has a strikethrough.
     * @param strikethrough {@code true} to enable strikethrough.
     * @return The builder instance for chaining.
     */
    public TextStyle strikethrough(boolean strikethrough) {
        this.root.put("strikethrough", strikethrough);
        return this;
    }

    /**
     * Sets the text to be obfuscated (randomly shifting characters).
     * @param obfuscated {@code true} to enable "magic" text.
     * @return The builder instance for chaining.
     */
    public TextStyle obfuscated(boolean obfuscated) {
        this.root.put("obfuscated", obfuscated);
        return this;
    }

    // --- 🛰️ Generation Methods ---

    /**
     * Generates the final JSON string enclosed in single quotes for Minecraft commands.
     * <p><b>Example Output:</b> {@code '{"color":"red","bold":true}'}</p>
     * <p><b>Error Catching:</b> Catches {@link JsonProcessingException} and wraps it
     * in a {@link RuntimeException} to avoid checked exception pollution.</p>
     * @return A single-quoted JSON string.
     */
    public String generate() {
        try {
            String json = MAPPER.writeValueAsString(this.root);
            return "'" + json + "'";
        } catch (JsonProcessingException e) {
            // This should logically never occur with a simple ObjectNode
            throw new RuntimeException("Failed to generate TextStyle JSON", e);
        }
    }

    /**
     * Returns the single-quoted JSON representation of the style.
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        return generate();
    }
}