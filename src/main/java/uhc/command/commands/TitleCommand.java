package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.time.VariableGameTime;
import uhc.command.MinecraftCommand;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 📢 **Title Command Builder**
 * <p>
 * Provides a fluent API for the {@code /title} command. This command displays
 * large text overlays, subtitles, or action bar messages to players.
 * </p>
 * <p>
 * <b>Syntax Variants:</b>
 * <ul>
 * <li>{@code /title <targets> <title|subtitle|actionbar> <text>}</li>
 * <li>{@code /title <targets> times <fadeIn> <stay> <fadeOut>}</li>
 * <li>{@code /title <targets> <clear|reset>}</li>
 * </ul>
 * </p>
 */
public class TitleCommand implements MinecraftCommand {
    private final Entity targets;
    private TitleAction action;
    private TextComponent textComponent;
    private VariableGameTime fadeIn;
    private VariableGameTime stay;
    private VariableGameTime fadeOut;

    private TitleCommand(Entity targets) {
        this.targets = Objects.requireNonNull(targets, "Title command must target non-null entities.");
    }

    /**
     * Initializes a new Title command builder for specific targets.
     * @param targets The players who will see the title.
     * @return A new TitleCommand instance.
     */
    public static TitleCommand create(Entity targets) {
        return new TitleCommand(targets);
    }

    // --- Mutually Exclusive Command Setters ---

    /** Removes the title currently on the screen. */
    public TitleCommand clear() {
        this.action = TitleAction.CLEAR;
        return this;
    }

    /** Resets the title timings and clears the text. */
    public TitleCommand reset() {
        this.action = TitleAction.RESET;
        return this;
    }

    /** Sets the main large title. */
    public TitleCommand title(TextComponent content) {
        this.action = TitleAction.TITLE;
        this.textComponent = Objects.requireNonNull(content, "Title content cannot be null.");
        return this;
    }

    public TitleCommand title(String content) {
        return title(TextComponent.simple(content));
    }

    /** Sets the smaller subtitle text (appears only if a title is also shown). */
    public TitleCommand subtitle(TextComponent content) {
        this.action = TitleAction.SUBTITLE;
        this.textComponent = Objects.requireNonNull(content, "Subtitle content cannot be null.");
        return this;
    }

    public TitleCommand subtitle(String content) {
        return subtitle(TextComponent.simple(content));
    }

    /** Sets the text displayed above the player's hotbar. */
    public TitleCommand actionbar(TextComponent content) {
        this.action = TitleAction.ACTIONBAR;
        this.textComponent = Objects.requireNonNull(content, "Actionbar content cannot be null.");
        return this;
    }

    public TitleCommand actionbar(String content) {
        return actionbar(TextComponent.simple(content));
    }

    /**
     * Configures the durations for the title animations.
     * @param fadeIn Ticks for the title to fade in.
     * @param stay Ticks for the title to remain fully opaque.
     * @param fadeOut Ticks for the title to fade out.
     */
    public TitleCommand displayTimes(VariableGameTime fadeIn, VariableGameTime stay, VariableGameTime fadeOut) {
        if (fadeIn == null || stay == null || fadeOut == null) {
            throw new IllegalArgumentException("All three display times must be non-null.");
        }
        this.action = TitleAction.TIMES;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        return this;
    }

    /** Sets display times to the vanilla default (10, 70, 20 ticks). */
    public TitleCommand defaultDisplayTimes() {
        return displayTimes(
                VariableGameTime.tick(10),
                VariableGameTime.tick(70),
                VariableGameTime.tick(20)
        );
    }

    /**
     * Generates the final Minecraft command string.
     * @throws IllegalStateException if an action or required content is missing.
     */
    @Override
    public String generate() {
        if (action == null) {
            throw new IllegalStateException("Must specify a title action (e.g., clear(), title()).");
        }

        StringBuilder sb = new StringBuilder("title ");
        sb.append(targets).append(" ");

        switch (action) {
            case CLEAR:
            case RESET:
                return sb.append(action).toString();

            case TIMES:
                if (fadeIn == null) {
                    throw new IllegalStateException("Display times must be configured via displayTimes().");
                }
                return sb.append("times ")
                        .append(fadeIn.getTime()).append(" ")
                        .append(stay.getTime()).append(" ")
                        .append(fadeOut.getTime())
                        .toString();

            case TITLE:
            case SUBTITLE:
            case ACTIONBAR:
                if (textComponent == null) {
                    throw new IllegalStateException("Text content must be set for action: " + action);
                }
                // textComponent.build() should return the raw JSON string
                return sb.append(action)
                        .append(" ")
                        .append(textComponent.build())
                        .toString();

            default:
                throw new IllegalStateException("Unrecognized TitleAction: " + action);
        }
    }

    @Override
    public String toString() {
        return generate();
    }

    /** Available operations for the /title command. */
    public enum TitleAction {
        CLEAR,
        RESET,
        TITLE,
        SUBTITLE,
        ACTIONBAR,
        TIMES;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}