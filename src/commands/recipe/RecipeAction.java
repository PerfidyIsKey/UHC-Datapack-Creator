package commands.recipe;

/**
 * Defines the two possible actions for the Minecraft {@code /recipe} command:
 * granting or revoking recipes from players.
 * <p>
 * The output for command arguments must be the lowercase version of the constant name.
 */
public enum RecipeAction {

    /** * The action to grant the specified recipe(s) to the target entity/player.
     * Converts to the command argument "give".
     */
    GIVE,

    /** * The action to revoke (remove) the specified recipe(s) from the target entity/player.
     * Converts to the command argument "take".
     */
    TAKE;

    /**
     * Overrides the default {@code toString()} method to return the lowercase
     * name of the action, as required by the Minecraft command syntax.
     *
     * @return The lowercase action name (e.g., "give" or "take").
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}