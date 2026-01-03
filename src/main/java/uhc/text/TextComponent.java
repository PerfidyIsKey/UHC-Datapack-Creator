package uhc.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import uhc.arguments.entity.Entity;
import uhc.text.color.ColorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 💬 **Universal Minecraft Text Component Builder**
 * <p>
 * This class provides a fluent API for creating complex Minecraft JSON text components.
 * It leverages Jackson for serialization and supports deep-cloning of existing components.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextComponent {

    // --- 🔧 Core Serialization ---

    /** Centralized Jackson Mapper configured to handle Java 18 record parameter names. */
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new ParameterNamesModule());

    // --- 📄 Fields ---

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

    @JsonProperty("color")
    private String color;

    @JsonProperty("bold")
    private Boolean bold;

    @JsonProperty("italic")
    private Boolean italic;

    @JsonProperty("obfuscated")
    private Boolean obfuscated;

    @JsonProperty("insertion")
    private String insertion;

    @JsonProperty("click_event")
    private ClickEvent clickEvent;

    @JsonProperty("hover_event")
    private HoverEvent hoverEvent;

    @JsonProperty("extra")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TextComponent> extra;

    // --- 🏗️ Constructors & Factories ---

    /** Private constructor to enforce factory usage. */
    private TextComponent() {}

    /**
     * Performs a deep copy of an existing TextComponent.
     * <p>
     * Every field is copied, and nested lists (extra, with) are recursively cloned
     * to ensure the new instance has no shared references with the source.
     * </p>
     * @param other The component to copy.
     * @return A new, independent TextComponent instance.
     * @throws NullPointerException if the input component is null.
     */
    public static TextComponent load(TextComponent other) {
        Objects.requireNonNull(other, "TextComponent load failed: Source component cannot be null.");

        TextComponent tc = new TextComponent();
        try {
            // Copy basic properties
            tc.type = other.type;
            tc.text = other.text;
            tc.selector = other.selector;
            tc.keybind = other.keybind;
            tc.translate = other.translate;
            tc.fallback = other.fallback;
            tc.color = other.color;
            tc.bold = other.bold;
            tc.italic = other.italic;
            tc.obfuscated = other.obfuscated;
            tc.insertion = other.insertion;
            tc.clickEvent = other.clickEvent;
            tc.hoverEvent = other.hoverEvent;

            // Deep copy 'with' list (for translatables)
            if (other.with != null) {
                tc.with = other.with.stream()
                        .map(TextComponent::load)
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            // Deep copy 'extra' list (child components)
            if (other.extra != null) {
                tc.extra = other.extra.stream()
                        .map(TextComponent::load)
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            return tc;
        } catch (Exception e) {
            throw new RuntimeException("TextComponent load failed: Deep copy operation encountered an error: " + e.getMessage(), e);
        }
    }

    /** Creates a literal text component. */
    public static TextComponent text(String text) {
        TextComponent tc = new TextComponent();
        tc.text = text;
        return tc;
    }

    /** Creates a translatable component. */
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

    /** Creates a selector component. */
    public static TextComponent selector(Entity target) {
        Objects.requireNonNull(target, "Selector target cannot be null.");
        TextComponent tc = new TextComponent();
        tc.selector = target.toString();
        return tc;
    }

    // --- 🎨 Styling & Interactivity ---

    public TextComponent color(ColorType color) {
        if (color == null) {
            this.color = null;
            return this;
        }
        String colorValue = color.getColor();
        if (colorValue == null || colorValue.isBlank()) {
            throw new IllegalArgumentException("Styling failed: ColorType returned an invalid color string for " + this.text);
        }
        this.color = colorValue;
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

    public TextComponent append(TextComponent other, boolean keepFormat) {
        Objects.requireNonNull(other, "Append failed: Cannot append a null TextComponent.");
        if (!keepFormat) {
            other.resetFormatting();
        }
        if (this.extra == null) this.extra = new ArrayList<>();
        this.extra.add(other);
        return this;
    }

    public TextComponent append(TextComponent other) {
        return append(other, false);
    }

    private void resetFormatting() {
        this.color = null;
        this.bold = null;
        this.italic = null;
        this.obfuscated = null;
        this.insertion = null;
        this.clickEvent = null;
        this.hoverEvent = null;
    }

    public TextComponent insertion(String text) {
        this.insertion = text;
        return this;
    }

    public TextComponent click(ClickEvent event) {
        this.clickEvent = event;
        return this;
    }

    public TextComponent hover(HoverEvent event) {
        this.hoverEvent = event;
        return this;
    }

    // --- ⚙️ Build Logic ---

    public String build() {
        try {
            if (isPlainLiteral()) {
                return MAPPER.writeValueAsString(text != null ? text : "");
            }
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Critical failure: TextComponent serialization failed for " + this.text, e);
        }
    }

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