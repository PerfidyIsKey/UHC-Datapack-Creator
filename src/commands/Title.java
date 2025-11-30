package commands;

import arguments.Entity;
import arguments.time.VariableGameTime;
import commands.title.TitleAction;
// Removed the import for commands.title.TitleDisplayType

/**
 * Represents the Minecraft {@code /title} command used to display on-screen messages,
 * set display times, or clear/reset existing titles for one or more targets.
 * <p>
 * The class uses the builder pattern to construct one of the mutually exclusive forms:
 * <ul>
 * <li>{@code title <targets> title <text>}</li>
 * <li>{@code title <targets> times <fadeIn> <stay> <fadeOut>}</li>
 * <li>{@code title <targets> clear}</li>
 * </ul>
 */
public class Title {
    private final Entity targets;
    private TitleAction action; // Holds CLEAR, RESET, TITLE, SUBTITLE, ACTIONBAR, or TIMES implicitly
    private String textContent; // Stores the JSON/String content for TITLE/SUBTITLE/ACTIONBAR
    private VariableGameTime fadeIn;
    private VariableGameTime stay;
    private VariableGameTime fadeOut;

    private Title(Entity targets) {
        if (targets == null) {
            throw new IllegalArgumentException("Title command must target non-null entities.");
        }
        this.targets = targets;
    }

    /**
     * Creates a new builder instance for the /title command, specifying the targets.
     *
     * @param targets The entity or group of entities to target (e.g., '@p', '@a').
     * @return A new Title builder instance.
     */
    public static Title create(Entity targets) {
        return new Title(targets);
    }

    // --- Mutually Exclusive Command Setters ---

    /**
     * Sets the action to {@code CLEAR}, removing the currently displayed title and subtitle.
     * This form is mutually exclusive with {@code times} and text display commands.
     */
    public Title clear() {
        this.action = TitleAction.CLEAR;
        return this;
    }

    /**
     * Sets the action to {@code RESET}, reverting display times to default (10/70/20 ticks).
     * This form is mutually exclusive with {@code times} and text display commands.
     */
    public Title reset() {
        this.action = TitleAction.RESET;
        return this;
    }

    /**
     * Sets the action to display text as a **main title** and sets the content.
     *
     * @param content The text (plain string or JSON text component) to display.
     */
    public Title title(String content) {
        this.action = TitleAction.TITLE;
        this.textContent = content;
        return this;
    }

    /**
     * Sets the action to display text as a **subtitle** and sets the content.
     *
     * @param content The text (plain string or JSON text component) to display.
     */
    public Title subtitle(String content) {
        this.action = TitleAction.SUBTITLE;
        this.textContent = content;
        return this;
    }

    /**
     * Sets the action to display text on the **action bar** (above the hotbar).
     *
     * @param content The text (plain string or JSON text component) to display.
     */
    public Title actionbar(String content) {
        this.action = TitleAction.ACTIONBAR;
        this.textContent = content;
        return this;
    }

    /**
     * Sets the action implicitly to {@code TIMES} and defines the custom display timings.
     * All three parameters are mandatory when calling this method.
     *
     * @param fadeIn The duration for the title to fade in.
     * @param stay The duration the title stays visible.
     * @param fadeOut The duration for the title to fade out.
     */
    public Title displayTimes(VariableGameTime fadeIn, VariableGameTime stay, VariableGameTime fadeOut) {
        if (fadeIn == null || stay == null || fadeOut == null) {
            throw new IllegalArgumentException("All three display times (fadeIn, stay, fadeOut) must be non-null.");
        }
        this.action = TitleAction.TIMES;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        return this;
    }

    // Removed the redundant defaultDisplayTime() method as RESET action already handles the concept of defaults.

    // --- Build Method ---

    /**
     * Builds the final {@code /title} command string based on the configured properties.
     *
     * @return The complete command string.
     * @throws IllegalStateException if no action (text, times, clear/reset) was specified.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("title ");
        sb.append(targets).append(" ");

        if (action == null) {
            throw new IllegalStateException("Cannot build command. Must specify an action (e.g., clear(), title(), displayTimes()).");
        }

        switch (action) {
            case CLEAR:
            case RESET:
                // Command form: title <targets> clear | reset
                return sb.append(action).toString();

            case TIMES:
                // Command form: title <targets> times <fadeIn> <stay> <fadeOut>
                // Validation for nulls handled in displayTimes() method.
                return sb.append(action) // 'times'
                        .append(" ").append(fadeIn.getTime())
                        .append(" ").append(stay.getTime())
                        .append(" ").append(fadeOut.getTime())
                        .toString();

            case TITLE:
            case SUBTITLE:
            case ACTIONBAR:
                // Command form: title <targets> title | subtitle | actionbar <text>
                if (textContent == null) {
                    throw new IllegalStateException("Text content must be set for " + action.toString() + " command.");
                }
                return sb.append(action) // 'title', 'subtitle', or 'actionbar'
                        .append(" ").append(textContent)
                        .toString();

            default:
                throw new IllegalStateException("Unrecognized TitleAction: " + action);
        }
    }
}