package commands.title;

/**
 * Represents the available actions for the Minecraft /title command.
 * These actions control how titles and subtitles are displayed and managed
 * for a player. The toString() method ensures the output is the
 * lowercase name required by Minecraft's command syntax.
 */
public enum TitleAction {
    /**
     * Clears the currently displayed title and subtitle from the player's screen.
     */
    CLEAR,

    /**
     * Resets the title display timings (fade in, stay, fade out) to their
     * default values (10, 70, 20 ticks respectively).
     */
    RESET,

    /**
     * Sets the title text to be displayed.
     * This action is followed by a JSON text component argument.
     */
    TITLE,

    /**
     * Sets the subtitle text to be displayed below the main title.
     * This action is followed by a JSON text component argument.
     */
    SUBTITLE,

    /**
     * Sets the action bar (a persistent message above the hotbar) text.
     * This action is followed by a JSON text component argument.
     */
    ACTIONBAR,

    /**
     * Sets the title display times in ticks for fade in, stay, and fade out.
     * This action is followed by three integer arguments.
     */
    TIMES;

    /**
     * Returns the command-friendly lowercase string of the enum constant.
     * This is used directly in the Minecraft /title command syntax.
     *
     * @return The lowercase name of the enum constant.
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}