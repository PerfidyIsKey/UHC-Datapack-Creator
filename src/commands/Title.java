package commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.time.VariableGameTime;
import commands.title.TitleAction;
import uhc.text.TextComponent;

/**
 * Represents the Minecraft {@code /title} command.
 * Now supports type-safe {@link TextComponent} for title, subtitle, and actionbar content.
 */
public class Title {
    private final Entity targets;
    private TitleAction action;
    private TextComponent textComponent; // Now using the object-oriented TextComponent
    private VariableGameTime fadeIn;
    private VariableGameTime stay;
    private VariableGameTime fadeOut;

    private Title(Entity targets) {
        if (targets == null) {
            throw new IllegalArgumentException("Title command must target non-null entities.");
        }
        this.targets = targets;
    }

    public static Title create(Entity targets) {
        return new Title(targets);
    }

    // --- Mutually Exclusive Command Setters ---

    public Title clear() {
        this.action = TitleAction.CLEAR;
        return this;
    }

    public Title reset() {
        this.action = TitleAction.RESET;
        return this;
    }

    /** Sets the main title using a type-safe TextComponent. */
    public Title title(TextComponent content) {
        this.action = TitleAction.TITLE;
        this.textComponent = content;
        return this;
    }

    /** Overload for plain strings. */
    public Title title(String content) {
        return title(TextComponent.simple(content));
    }

    /** Sets the subtitle using a type-safe TextComponent. */
    public Title subtitle(TextComponent content) {
        this.action = TitleAction.SUBTITLE;
        this.textComponent = content;
        return this;
    }

    /** Overload for plain strings. */
    public Title subtitle(String content) {
        return subtitle(TextComponent.simple(content));
    }

    /** Sets the actionbar text using a type-safe TextComponent. */
    public Title actionbar(TextComponent content) {
        this.action = TitleAction.ACTIONBAR;
        this.textComponent = content;
        return this;
    }

    /** Overload for plain strings. */
    public Title actionbar(String content) {
        return actionbar(TextComponent.simple(content));
    }

    public Title displayTimes(VariableGameTime fadeIn, VariableGameTime stay, VariableGameTime fadeOut) {
        if (fadeIn == null || stay == null || fadeOut == null) {
            throw new IllegalArgumentException("All three display times must be non-null.");
        }
        this.action = TitleAction.TIMES;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        return this;
    }

    public Title defaultDisplayTimes() {
        this.action = TitleAction.TIMES;
        this.fadeIn = VariableGameTime.tick(10);
        this.stay = VariableGameTime.tick(70);
        this.fadeOut = VariableGameTime.tick(20);
        return this;
    }

    // --- Build Method ---

    public String build() {
        StringBuilder sb = new StringBuilder("title ");
        sb.append(targets).append(" ");

        if (action == null) {
            throw new IllegalStateException("Must specify a title action (e.g., clear(), title()).");
        }

        switch (action) {
            case CLEAR:
            case RESET:
                return sb.append(action.toString().toLowerCase()).toString();

            case TIMES:
                if (fadeIn == null) {
                    throw new IllegalStateException("Display times must be configured.");
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
                    throw new IllegalStateException("Text content must be set for " + action);
                }
                // Minecraft commands expect the JSON text component here
                return sb.append(action.toString().toLowerCase())
                        .append(" ")
                        .append(textComponent.build())
                        .toString();

            default:
                throw new IllegalStateException("Unrecognized TitleAction: " + action);
        }
    }
}