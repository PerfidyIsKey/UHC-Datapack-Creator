package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.RecipeId;

import java.util.Objects;

/**
 * 📖 **Recipe Command Builder**
 * <p>
 * Provides a fluent API for the {@code /recipe} command, used to unlock or
 * lock crafting recipes for players.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /recipe <give|take> <targets> [<recipe>|*]}
 * </p>
 */
public class RecipeCommand implements MinecraftCommand {

    private final RecipeAction action;
    private final Entity targets;

    /**
     * The specific recipe to affect. If null, defaults to "*" (all recipes).
     */
    private RecipeId recipe;

    private RecipeCommand(RecipeAction action, Entity targets) {
        // Enforce non-nullability for mandatory command components
        this.action = Objects.requireNonNull(action, "RecipeAction cannot be null.");
        this.targets = Objects.requireNonNull(targets, "Targets entity selector cannot be null.");
    }

    /**
     * Initializes a new Recipe command builder.
     * @param action Whether to GIVE or TAKE the recipe.
     * @param targets The target players (e.g., @a or a specific name).
     * @return A new RecipeCommand instance.
     */
    public static RecipeCommand create(RecipeAction action, Entity targets) {
        return new RecipeCommand(action, targets);
    }

    /**
     * Specifies a particular recipe to unlock or lock.
     * <p>If this method is not called, the command will target ALL recipes ({@code *}).</p>
     * @param recipe The ID of the recipe (e.g., minecraft:diamond_axe).
     * @return The current builder instance.
     */
    public RecipeCommand recipe(RecipeId recipe) {
        this.recipe = recipe;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * @return The formatted command (e.g., "recipe give @a minecraft:bread" or "recipe take @s *").
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("recipe ");

        // Append mandatory action and targets
        sb.append(action).append(" ").append(targets);

        // Append the specific recipe ID or the global wildcard
        sb.append(" ");
        if (recipe == null) {
            sb.append("*");
        } else {
            sb.append(recipe);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Defines the operation type for the recipe command.
     */
    public enum RecipeAction {
        GIVE,
        TAKE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}