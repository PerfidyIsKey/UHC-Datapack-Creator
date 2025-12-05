package commands;

import arguments.Entity;
import arguments.time.VariableGameTime;
import commands.title.TitleAction;

/**
 * Represents the Minecraft {@code /title} command used to display on-screen messages,
 * set display times, or clear/reset existing titles for one or more targets.
 * <p>
 * This class uses the **Builder Pattern** to construct one of the mutually exclusive command forms:
 * <ul>
 * <li>{@code title <targets> clear | reset}</li>
 * <li>{@code title <targets> times <fadeIn> <stay> <fadeOut>}</li>
 * <li>{@code title <targets> title | subtitle | actionbar <text>}</li>
 * </ul>
 */
public class Title {
    private final Entity targets;
    private TitleAction action; // Holds the primary command type (e.g., CLEAR, TIMES, TITLE).
    private String textContent; // Stores the JSON/String content used for TITLE, SUBTITLE, or ACTIONBAR commands.
    private VariableGameTime fadeIn; // The fade-in duration for the TIMES command.
    private VariableGameTime stay;   // The duration the title stays visible for the TIMES command.
    private VariableGameTime fadeOut;  // The fade-out duration for the TIMES command.

    private Title(Entity targets) {
        if (targets == null) {
            throw new IllegalArgumentException("Title command must target non-null entities.");
        }
        this.targets = targets;
    }

    /**
     * Creates a new builder instance for the Minecraft {@code /title} command.
     *
     * @param targets The entity or group of entities to target (e.g., '@p', '@a', 'PlayerName').
     * @return A new Title builder instance.
     */
    public static Title create(Entity targets) {
        return new Title(targets);
    }

    // --- Mutually Exclusive Command Setters ---

    /**
     * Sets the command action to {@code CLEAR}.
     * <p>
     * Command form: {@code title <targets> clear}
     *
     * @return The current builder instance for chaining.
     */
    public Title clear() {
        this.action = TitleAction.CLEAR;
        return this;
    }

    /**
     * Sets the command action to {@code RESET}.
     * <p>
     * This reverts any previously set display times for the targets to the Minecraft defaults.
     * Command form: {@code title <targets> reset}
     *
     * @return The current builder instance for chaining.
     */
    public Title reset() {
        this.action = TitleAction.RESET;
        return this;
    }

    /**
     * Sets the command to display text as a **main title** and defines the content.
     * <p>
     * Command form: {@code title <targets> title <content>}
     *
     * @param content The text (plain string or JSON text component) to display.
     * @return The current builder instance for chaining.
     */
    public Title title(String content) {
        this.action = TitleAction.TITLE;
        this.textContent = content;
        return this;
    }

    /**
     * Sets the command to display text as a **subtitle** and defines the content.
     * <p>
     * Command form: {@code title <targets> subtitle <content>}
     *
     * @param content The text (plain string or JSON text component) to display.
     * @return The current builder instance for chaining.
     */
    public Title subtitle(String content) {
        this.action = TitleAction.SUBTITLE;
        this.textContent = content;
        return this;
    }

    /**
     * Sets the command to display text on the **action bar** (the area above the hotbar).
     * <p>
     * Command form: {@code title <targets> actionbar <content>}
     *
     * @param content The text (plain string or JSON text component) to display.
     * @return The current builder instance for chaining.
     */
    public Title actionbar(String content) {
        this.action = TitleAction.ACTIONBAR;
        this.textContent = content;
        return this;
    }

    /**
     * Sets the command to customize the title display timings.
     * <p>
     * This implicitly sets the action to {@code TIMES}. All three parameters are mandatory.
     * Command form: {@code title <targets> times <fadeIn> <stay> <fadeOut>}
     *
     * @param fadeIn The duration for the title to fade in.
     * @param stay The duration the title stays visible.
     * @param fadeOut The duration for the title to fade out.
     * @return The current builder instance for chaining.
     * @throws IllegalArgumentException if any timing argument is {@code null}.
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

    /**
     * Sets the command to customize the title display timings using the Minecraft default values.
     * <p>
     * This implicitly sets the action to {@code TIMES} and uses the default values:
     * **Fade In: 10 ticks, Stay: 70 ticks, Fade Out: 20 ticks**.
     * Command form: {@code title <targets> times 10 70 20}
     *
     * @return The current builder instance for chaining.
     */
    public Title defaultDisplayTimes() {
        this.action = TitleAction.TIMES;
        this.fadeIn = VariableGameTime.tick(10);
        this.stay = VariableGameTime.tick(70);
        this.fadeOut = VariableGameTime.tick(20);
        return this;
    }

    // --- Build Method ---

    /**
     * Finalizes and builds the complete Minecraft {@code /title} command string.
     * <p>
     * Validation ensures that all required components for the selected command type are present.
     *
     * @return The complete, runnable command string.
     * @throws IllegalStateException if no action (text, times, clear/reset) was configured, or if mandatory text content is missing.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("title ");
        sb.append(targets).append(" ");

        if (action == null) {
            throw new IllegalStateException("Cannot build command. Must specify a title action (e.g., clear(), title(), displayTimes()).");
        }

        switch (action) {
            case CLEAR:
            case RESET:
                // Command form: title <targets> clear | reset
                return sb.append(action).toString();

            case TIMES:
                // Command form: title <targets> times <fadeIn> <stay> <fadeOut>
                // Validation for nulls handled in displayTimes() method.
                if (fadeIn == null) {
                    // This handles cases where action was set to TIMES but no displayTimes method was called (should not happen if using the builder methods).
                    throw new IllegalStateException("Display times must be configured when the TIMES action is selected.");
                }
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