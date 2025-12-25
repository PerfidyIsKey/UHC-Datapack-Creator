package uhc.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import uhc.arguments.entity.Entity;
import uhc.resource.color.ColorType;

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

    /** Centralized Jackson Mapper configured to handle Java 18 record parameter names. */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new ParameterNamesModule());

    /** The type of component (e.g., "translatable"). Usually inferred by fields present. */
    @JsonProperty("type")
    private String type;

    /** Raw literal text content. Used for standard messages. */
    @JsonProperty("text")
    private String text;

    /** Target selector string (e.g., "@a", "@p"). Displays the names of targeted entities. */
    @JsonProperty("selector")
    private String selector;

    /** A keybind identifier (e.g., "key.jump"). Displays the player's bound key. */
    @JsonProperty("keybind")
    private String keybind;

    /** The translation key identifier (e.g., "item.minecraft.diamond_sword"). */
    @JsonProperty("translate")
    private String translate;

    /** Text to display if the translation key is missing on the client side. */
    @JsonProperty("fallback")
    private String fallback;

    /** Arguments used to fill placeholders (%s) in a translatable component. */
    @JsonProperty("with")
    private List<TextComponent> with;

    /** The color of the text. Can be a named color or a hex code (#RRGGBB). */
    @JsonProperty("color")
    private String color;

    /** Whether the text should be rendered in **bold**. */
    @JsonProperty("bold")
    private Boolean bold;

    /** Whether the text should be rendered in *italics*. */
    @JsonProperty("italic")
    private Boolean italic;

    /** Whether the text should be obfuscated (magic scrambled characters). */
    @JsonProperty("obfuscated")
    private Boolean obfuscated;

    /** Text inserted into the player's chat bar when they shift-click this component. */
    @JsonProperty("insertion")
    private String insertion;

    /** Defines an action (like running a command) when the component is clicked. */
    @JsonProperty("click_event")
    private ClickEvent clickEvent;

    /** Defines a tooltip or information to show when the component is hovered over. */
    @JsonProperty("hover_event")
    private HoverEvent hoverEvent;

    /** A list of child components that follow this one and inherit its formatting. */
    @JsonProperty("extra")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TextComponent> extra;

    /** Private constructor to enforce the use of static factory methods. */
    private TextComponent() {}

    // --- Static Factory Methods ---

    /**
     * Creates a literal text component.
     * @param text The raw text to display.
     * @return A new TextComponent instance.
     */
    public static TextComponent text(String text) {
        TextComponent tc = new TextComponent();
        tc.text = text;
        return tc;
    }

    /**
     * Creates a translatable component based on a language key.
     * @param key The translation identifier (e.g., "chat.type.text").
     * @param fallback Optional text to show if the key is missing on the client.
     * @param with Optional components to fill the placeholders in the translation.
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
     * Creates a component that displays an entity's name based on a selector.
     * @param target The {@link Entity} selector object.
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

    /**
     * Sets the color of the text using a type-safe {@link ColorType}.
     * Supports named colors (TextColor) and hex codes (HexColor).
     * @param color The color implementation. If null, formatting is removed.
     * @return This TextComponent for chaining.
     * @throws IllegalArgumentException if the provided ColorType returns a blank string.
     */
    public TextComponent color(ColorType color) {
        if (color == null) {
            this.color = null;
            return this;
        }
        String colorValue = color.getColor();
        if (colorValue == null || colorValue.isBlank()) {
            throw new IllegalArgumentException("The provided ColorType returned an invalid color string.");
        }
        this.color = colorValue;
        return this;
    }

    /** Toggles bold formatting. */
    public TextComponent bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    /** Toggles italic formatting. */
    public TextComponent italic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    /** Toggles obfuscated (magic) formatting. */
    public TextComponent obfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    /**
     * Appends a child component to this component's 'extra' list.
     * @param other The component to be added.
     * @param keepFormat If true, the child inherits parent styles. If false, styles are reset.
     * @return This parent component for chaining.
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
     * Appends a child component, defaulting keepFormat to false (starts plain).
     * @param other The component to be added.
     * @return This parent component for chaining.
     */
    public TextComponent append(TextComponent other) {
        return append(other, false);
    }

    /** Clears all formatting and interactivity to ensure a "plain" start for child components. */
    private void resetFormatting() {
        this.color = null;
        this.bold = null;
        this.italic = null;
        this.obfuscated = null;
        this.insertion = null;
        this.clickEvent = null;
        this.hoverEvent = null;
    }

    // --- Interactivity Methods ---

    /** Sets the text to be pasted into the chat bar when shift-clicked. */
    public TextComponent insertion(String text) {
        this.insertion = text;
        return this;
    }

    /** Assigns a type-safe {@link ClickEvent} to the component. */
    public TextComponent click(ClickEvent event) {
        this.clickEvent = event;
        return this;
    }

    /** Assigns a type-safe {@link HoverEvent} to the component. */
    public TextComponent hover(HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }

    // --- Build Logic ---

    /**
     * Serializes this component into a Minecraft-compatible JSON string.
     * Optimizes simple text into a raw string to reduce JSON overhead.
     * @return Valid JSON string.
     * @throws RuntimeException if Jackson fails to serialize.
     */
    public String build() {
        try {
            if (isPlainLiteral()) {
                return MAPPER.writeValueAsString(text != null ? text : "");
            }
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Critical failure during TextComponent serialization", e);
        }
    }

    /** Checks if the component is a literal string with no styles or extra metadata. */
    private boolean isPlainLiteral() {
        return text != null && type == null && translate == null && selector == null &&
                keybind == null && color == null && bold == null && italic == null &&
                obfuscated == null && insertion == null && clickEvent == null &&
                hoverEvent == null && (extra == null || extra.isEmpty());
    }

    @Override
    public String toString() {
        return build();
    }
}