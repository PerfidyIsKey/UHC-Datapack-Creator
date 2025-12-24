package uhc.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uhc.arguments.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 💬 **Minecraft Text Component Builder (Jackson Powered)**
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // Automatically omits null fields
public class TextComponent {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

    @JsonProperty("selector")
    private String selector;

    @JsonProperty("keybind")
    private String keybind;

    // Translation fields are handled flat in the root object per Minecraft spec
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

    @JsonProperty("extra")
    @JsonInclude(JsonInclude.Include.NON_EMPTY) // Omit if list is empty
    private List<TextComponent> extra;

    private TextComponent() {}

    // --- Static Factory Methods ---

    public static TextComponent text(String text) {
        TextComponent tc = new TextComponent();
        tc.text = text;
        return tc;
    }

    public static TextComponent translatable(String key, String fallback, TextComponent... with) {
        TextComponent tc = new TextComponent();
        tc.type = "translatable";
        tc.translate = Objects.requireNonNull(key);
        tc.fallback = fallback;
        if (with != null && with.length > 0) {
            tc.with = List.of(with);
        }
        return tc;
    }

    public static TextComponent selector(Entity target) {
        TextComponent tc = new TextComponent();
        tc.selector = target.toString();
        return tc;
    }

    // --- Styling Methods ---

    public TextComponent color(TextColor color) {
        this.color = color != null ? color.toString() : null;
        return this;
    }

    public TextComponent bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public TextComponent append(TextComponent other) {
        if (this.extra == null) this.extra = new ArrayList<>();
        this.extra.add(other);
        return this;
    }

    // --- Build Logic ---

    /**
     * Converts the component to a valid JSON string using Jackson.
     */
    public String build() {
        try {
            // Optimization: If it's just plain text with no styles, return raw string
            if (isPlainLiteral()) {
                return MAPPER.writeValueAsString(text);
            }
            return MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize TextComponent", e);
        }
    }

    private boolean isPlainLiteral() {
        return text != null && type == null && translate == null && selector == null &&
                color == null && bold == null && italic == null && (extra == null || extra.isEmpty());
    }

    @Override
    public String toString() {
        return build();
    }
}