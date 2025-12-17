package uhc.text;

import uhc.arguments.entity.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 💬 **Minecraft Text Component Builder**
 * <p>
 * Represents a structured Minecraft text component (JSON).
 * Supports single objects, selectors, click events, and composite arrays.
 * </p>
 */
public class TextComponent {

    private String text;
    private String selector;
    private String color;
    private Boolean bold;
    private Boolean italic;
    private Boolean obfuscated;
    private ClickEvent clickEvent;
    private final List<TextComponent> extra = new ArrayList<>();

    // --- Constructors ---

    private TextComponent() {}

    private TextComponent(String text) {
        this.text = text;
    }

    // --- Static Factory Methods ---

    /** Factory for a simple text component. */
    public static TextComponent simple(String text) {
        if (text == null) throw new IllegalArgumentException("Text cannot be null.");
        return new TextComponent(text);
    }

    /** Factory for a selector-based component (e.g., displays a player's name). */
    public static TextComponent selector(Entity target) {
        if (target == null) throw new IllegalArgumentException("Target cannot be null.");
        TextComponent component = new TextComponent();
        component.selector = target.toString();
        return component;
    }

    /**
     * Factory for a component with a click event.
     * Often used for signs or chat messages that trigger commands.
     */
    public static TextComponent withClickCommand(String text, String action, String command) {
        if (text == null || action == null || command == null) {
            throw new IllegalArgumentException("Text, action, and command must be non-null.");
        }
        return TextComponent.simple(text).click(action, command);
    }

    /** * Factory for a composite array component.
     * Allows multiple components to be treated as a single type-safe TextComponent.
     */
    public static TextComponent array(List<TextComponent> parts) {
        TextComponent composite = new TextComponent();
        composite.extra.addAll(parts);
        return composite;
    }

    /** Varargs overload for the array factory. */
    public static TextComponent array(TextComponent... parts) {
        return array(List.of(parts));
    }

    // --- Builder Style Methods ---

    public TextComponent color(TextColor color) {
        this.color = (color != null) ? color.toString() : null;
        return this;
    }

    public TextComponent color(HexColor color) {
        this.color = (color != null) ? color.getColor() : null;
        return this;
    }

    public TextComponent bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public TextComponent italic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public TextComponent obfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    /**
     * Internal click event setter.
     * @param action The action (e.g., "run_command", "open_url").
     * @param value The value (e.g., "/say Hi").
     */
    public TextComponent click(String action, String value) {
        this.clickEvent = new ClickEvent(action, value);
        return this;
    }

    /**
     * Appends a child component to the "extra" list.
     */
    public TextComponent append(TextComponent other) {
        if (other != null) {
            this.extra.add(other);
        }
        return this;
    }

    // --- Generation Logic ---

    /**
     * Builds the final JSON string suitable for Minecraft commands.
     */
    public String build() {
        // Handle Case: Composite Array (Container for children only)
        if (text == null && selector == null && !extra.isEmpty()) {
            return "[" + extra.stream()
                    .map(TextComponent::build)
                    .collect(Collectors.joining(",")) + "]";
        }

        // Handle Case: Minimal Raw String (No styles, no children)
        if (color == null && bold == null && italic == null &&
                obfuscated == null && clickEvent == null && extra.isEmpty() && selector == null) {
            return "\"" + escape(text) + "\"";
        }

        // Handle Case: JSON Object {}
        StringBuilder sb = new StringBuilder("{");
        boolean firstField = true;

        if (text != null) {
            sb.append("\"text\":\"").append(escape(text)).append("\"");
            firstField = false;
        } else if (selector != null) {
            sb.append("\"selector\":\"").append(escape(selector)).append("\"");
            firstField = false;
        }

        if (color != null) appendField(sb, "color", color, firstField);
        if (bold != null) appendField(sb, "bold", bold.toString(), false);
        if (italic != null) appendField(sb, "italic", italic.toString(), false);
        if (obfuscated != null) appendField(sb, "obfuscated", obfuscated.toString(), false);

        if (clickEvent != null) {
            sb.append(",\"click_event\":{\"action\":\"")
                    .append(escape(clickEvent.action))
                    .append("\",\"value\":\"")
                    .append(escape(clickEvent.value))
                    .append("\"}");
        }

        if (!extra.isEmpty()) {
            sb.append(",\"extra\":[");
            sb.append(extra.stream().map(TextComponent::build).collect(Collectors.joining(",")));
            sb.append("]");
        }

        sb.append("}");
        return sb.toString();
    }

    /** Helper to handle comma placement and value quoting in JSON fields. */
    private void appendField(StringBuilder sb, String key, String value, boolean isFirst) {
        if (!isFirst) sb.append(",");
        sb.append("\"").append(key).append("\":");
        // JSON booleans are not quoted
        if (value.equals("true") || value.equals("false")) {
            sb.append(value);
        } else {
            sb.append("\"").append(escape(value)).append("\"");
        }
    }

    /** Escapes backslashes and quotes to maintain valid JSON syntax. */
    private String escape(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @Override
    public String toString() {
        return build();
    }

    // --- Internal Record ---
    private record ClickEvent(String action, String value) {}
}