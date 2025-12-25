package uhc.text;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🖱️ **Polymorphic Click Event System**
 * <p>
 * Defines actions that occur when a player clicks on a {@link TextComponent}.
 * This system is type-safe and serializes to the standard Minecraft JSON format.
 * </p>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "action"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClickEvent.OpenUrl.class, name = "open_url"),
        @JsonSubTypes.Type(value = ClickEvent.OpenFile.class, name = "open_file"),
        @JsonSubTypes.Type(value = ClickEvent.RunCommand.class, name = "run_command"),
        @JsonSubTypes.Type(value = ClickEvent.SuggestCommand.class, name = "suggest_command"),
        @JsonSubTypes.Type(value = ClickEvent.ChangePage.class, name = "change_page"),
        @JsonSubTypes.Type(value = ClickEvent.CopyToClipboard.class, name = "copy_to_clipboard"),
        @JsonSubTypes.Type(value = ClickEvent.ShowDialog.class, name = "show_dialog"),
        @JsonSubTypes.Type(value = ClickEvent.Custom.class, name = "custom")
})
public interface ClickEvent {

    /** @return The Minecraft-internal action name. */
    @JsonProperty("action")
    String getAction();

    // --- Static Helper Factories ---

    /**
     * Opens a URL in the player's default web browser.
     * @param url The target URL (e.g., "https://minecraft.net").
     * @throws IllegalArgumentException if the URL is null or blank.
     */
    static OpenUrl openUrl(String url) {
        if (url == null || url.isBlank()) throw new IllegalArgumentException("URL cannot be null or blank.");
        return new OpenUrl(url);
    }

    /**
     * Executes a command as the player who clicked.
     * @param command A type-safe {@link MinecraftCommand}.
     * @throws NullPointerException if the command or generated string is null.
     */
    static RunCommand runCommand(MinecraftCommand command) {
        Objects.requireNonNull(command, "Command object cannot be null.");
        String generated = command.generate();
        if (generated == null || generated.isBlank()) {
            throw new IllegalArgumentException("Generated command string cannot be null or empty.");
        }
        return new RunCommand(generated);
    }

    /**
     * Injects a string into the player's chat input bar.
     * @param command A type-safe {@link MinecraftCommand}.
     * @throws NullPointerException if the command is null.
     */
    static SuggestCommand suggestCommand(MinecraftCommand command) {
        Objects.requireNonNull(command, "Command object cannot be null.");
        return new SuggestCommand(command.generate());
    }

    /**
     * Changes the current page of a written book.
     * @param page The target page index (1-indexed).
     * @throws IllegalArgumentException if the page is less than 1.
     */
    static ChangePage changePage(int page) {
        if (page < 1) throw new IllegalArgumentException("Book pages must be 1 or greater.");
        return new ChangePage(page);
    }

    /**
     * Copies the specified text to the player's system clipboard.
     * @param text The string to be copied.
     */
    static CopyToClipboard copy(String text) {
        return new CopyToClipboard(Objects.requireNonNull(text, "Copy value cannot be null."));
    }

    // --- Implementations ---

    /** Implementation for opening URLs. */
    record OpenUrl(@JsonProperty("value") String value) implements ClickEvent {
        @Override public String getAction() { return "open_url"; }
    }

    /** Implementation for opening local files (Note: Restricted on most clients). */
    record OpenFile(@JsonProperty("value") String value) implements ClickEvent {
        @Override public String getAction() { return "open_file"; }
    }

    /** Implementation for executing commands. */
    record RunCommand(@JsonProperty("value") String value) implements ClickEvent {
        public RunCommand {
            Objects.requireNonNull(value, "Command string cannot be null.");
        }
        @Override public String getAction() { return "run_command"; }
    }

    /** Implementation for suggesting commands in the chat bar. */
    record SuggestCommand(@JsonProperty("value") String value) implements ClickEvent {
        public SuggestCommand {
            Objects.requireNonNull(value, "Suggestion string cannot be null.");
        }
        @Override public String getAction() { return "suggest_command"; }
    }

    /** Implementation for book page navigation. */
    record ChangePage(@JsonProperty("value") int value) implements ClickEvent {
        @Override public String getAction() { return "change_page"; }
    }

    /** Implementation for copying text to clipboard. */
    record CopyToClipboard(@JsonProperty("value") String value) implements ClickEvent {
        @Override public String getAction() { return "copy_to_clipboard"; }
    }

    /** Implementation for showing specific dialog identifiers. */
    record ShowDialog(@JsonProperty("value") String value) implements ClickEvent {
        @Override public String getAction() { return "show_dialog"; }
    }

    /** Implementation for custom events with extra payloads. */
    record Custom(
            @JsonProperty("value") String value,
            @JsonProperty("payload") String payload
    ) implements ClickEvent {
        @Override public String getAction() { return "custom"; }
    }
}