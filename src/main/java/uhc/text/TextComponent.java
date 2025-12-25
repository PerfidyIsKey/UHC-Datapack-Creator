package uhc.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import uhc.arguments.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 💬 **Universal Minecraft Text Component Builder**
 * <p>
 * This class provides a fluent API for creating complex Minecraft JSON text components.
 * It leverages Jackson for high-performance, safe serialization, supporting all modern
 * Minecraft features including translations, selectors, and interactive events.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextComponent {

    /** Centralized Jackson Mapper configured for modern Java features. */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new ParameterNamesModule());

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

    @JsonProperty("selector")
    private String selector;

    @JsonProperty("keybind")
    private String keybind;

    @JsonProperty("translate")
    private String translate;

    @JsonProperty("fallback")
    private String fallback;

    @JsonProperty("with")
    private List<TextComponent> with;

    // --- Styling Fields ---

    @JsonProperty("color")
    private String color;

    @JsonProperty("bold")
    private Boolean bold;

    @JsonProperty("italic")
    private Boolean italic;

    // --- Interactivity Fields ---

    @JsonProperty("insertion")
    private String insertion;

    @JsonProperty("click_event")
    private ClickEvent clickEvent;

    @JsonProperty("hover_event")
    private HoverEvent hoverEvent;

    /** Nested components that inherit the styles of this parent. */
    @JsonProperty("extra")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TextComponent> extra;

    /** Private constructor to enforce factory method usage. */
    private TextComponent() {}

    // --- Static Factory Methods ---

    /**
     * Creates a literal text component.
     * @param text The raw text to display.
     * @return A new TextComponent instance.
     */
    public static TextComponent text(String text) {
        TextComponent tc = new TextComponent();
        tc.text = text; // Minecraft allows null text in some NBT contexts, but build() handles literal optimization
        return tc;
    }

    /**
     * Creates a translatable component based on a language key.
     * @param key The translation identifier (e.g., "chat.type.text").
     * @param fallback Optional text to show if the key is missing on the client.
     * @param with Optional components to fill the %s slots in the translation.
     * @return A new translatable TextComponent.
     * @throws NullPointerException if the key is null.
     */
    public static TextComponent translatable(String key, String fallback, TextComponent... with) {
        TextComponent tc = new TextComponent();
        tc.type = "translatable";
        tc.translate = Objects.requireNonNull(key, "Translation key cannot be null.");
        tc.fallback = fallback;
        if (with != null && with.length > 0) {
            tc.with = List.of(with);
        }
        return tc;
    }

    /**
     * Creates a component that displays an entity's name (or players in a selector).
     * @param target The {@link Entity} selector or reference.
     * @return A new selector TextComponent.
     * @throws NullPointerException if target is null.
     */
    public static TextComponent selector(Entity target) {
        Objects.requireNonNull(target, "Selector target cannot be null.");
        TextComponent tc = new TextComponent();
        tc.selector = target.toString();
        return tc;
    }

    // --- Styling Methods ---

    /** Sets the color of the text using a type-safe {@link TextColor}. */
    public TextComponent color(TextColor color) {
        this.color = color != null ? color.toString() : null;
        return this;
    }

    /** Toggles the bold formatting. */
    public TextComponent bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    /** Toggles the italic formatting. */
    public TextComponent italic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    /**
     * Appends a child component to this component.
     * * @param other The component to be added to the 'extra' list.
     * @param keepFormat If true, the child inherits the styles (color, bold, etc.) of this parent.
     * If false (default), the child's styles are cleared to ensure it starts plain.
     * @return This parent component for fluent chaining.
     */
    public TextComponent append(TextComponent other, boolean keepFormat) {
        if (other != null) {
            if (!keepFormat) {
                other.resetFormatting();
            }
            if (this.extra == null) this.extra = new ArrayList<>();
            this.extra.add(other);
        }
        return this;
    }

    /**
     * Overloaded append method that defaults keepFormat to false.
     */
    public TextComponent append(TextComponent other) {
        return append(other, false);
    }

    /**
     * Internal helper to clear all styling and interactivity to ensure the component
     * starts "plain" when appended without formatting.
     */
    private void resetFormatting() {
        this.color = null;
        this.bold = null;
        this.italic = null;
        this.obfuscated = null; // Assuming you added this per the previous step
        this.insertion = null;
        this.clickEvent = null;
        this.hoverEvent = null;
    }

    // --- Interactivity Methods ---

    /**
     * Sets text to be pasted into the chat bar when shift-clicked.
     * @param text The insertion text.
     */
    public TextComponent insertion(String text) {
        this.insertion = text;
        return this;
    }

    /**
     * Assigns a type-safe {@link ClickEvent}.
     * @param event The click event configuration.
     */
    public TextComponent click(ClickEvent event) {
        this.clickEvent = event;
        return this;
    }

    /**
     * Assigns a type-safe {@link HoverEvent}.
     * @param event The hover event configuration.
     */
    public TextComponent hover(HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }

    // --- Build Logic ---

    /**
     * Serializes this component into a Minecraft-compatible JSON string.
     * <p>
     * Optimizes "plain" components into raw strings (e.g., "Hello" vs {"text":"Hello"})
     * to save packet space.
     * </p>
     * @return Valid JSON string for use in commands or packets.
     * @throws RuntimeException if serialization fails.
     */
    public String build() {
        try {
            if (isPlainLiteral()) {
                // Returns "text" (with quotes) for simple literals
                return MAPPER.writeValueAsString(text != null ? text : "");
            }
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Critical failure during TextComponent serialization", e);
        }
    }

    /**
     * Checks if this component is a literal string without any metadata.
     * Includes checks for interactivity to prevent mis-serialization.
     */
    private boolean isPlainLiteral() {
        return text != null && type == null && translate == null && selector == null &&
                keybind == null && color == null && bold == null && italic == null &&
                insertion == null && clickEvent == null && hoverEvent == null &&
                (extra == null || extra.isEmpty());
    }

    @Override
    public String toString() {
        return build();
    }
}