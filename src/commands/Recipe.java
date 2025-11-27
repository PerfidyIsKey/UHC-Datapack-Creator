package commands;

import arguments.Entity;
import commands.recipe.RecipeAction;
import shared.RecipeId;

/**
 * Represents the Minecraft 'recipe' command structure.
 * This command allows players or servers to unlock or lock recipes for specified entities (players).
 * <p>
 * Command syntax: {@code recipe <action> <targets> [<recipe>|*]}
 */
public class Recipe {

    // The required action (GIVE or TAKE).
    private final RecipeAction action;
    // The required target entity selector (e.g., @a, player_name).
    private final Entity targets;
    // The optional specific recipe ID (e.g., 'minecraft:bread'), or null to use all recipes (*).
    private RecipeId recipe;

    /**
     * Private constructor to enforce instantiation via the static factory method.
     *
     * @param action The recipe action.
     * @param targets The target entities.
     */
    private Recipe(RecipeAction action, Entity targets) {
        this.action = action;
        this.targets = targets;
    }

    /**
     * Static factory method to create a new Recipe command instance.
     * Ensures all mandatory action and target arguments are non-null.
     *
     * @param action The required recipe action (GIVE or TAKE).
     * @param targets The required target entities (Entity selector).
     * @return A new Recipe instance ready for optional recipe ID configuration.
     * @throws IllegalArgumentException if {@code action} or {@code targets} is null.
     */
    public static Recipe create(RecipeAction action, Entity targets) {
        if (action == null) {
            throw new IllegalArgumentException("RecipeAction cannot be null for the recipe command.");
        }
        if (targets == null) {
            throw new IllegalArgumentException("Target entities argument cannot be null for the recipe command.");
        }
        return new Recipe(action, targets);
    }

    /**
     * Sets the optional specific recipe to be affected.
     * If this method is not called, all recipes will be affected (*).
     *
     * @param recipe The specific RecipeId to give or take.
     * @return The current Recipe instance for method chaining.
     */
    public Recipe recipe(RecipeId recipe) {
        this.recipe = recipe;
        return this;
    }

    /**
     * Constructs and returns the final string representation of the 'recipe' command.
     *
     * @return The complete, formatted Minecraft command string.
     */
    public String build() {
        // Start with mandatory arguments: recipe <action> <targets>
        StringBuilder sb = new StringBuilder("recipe ");

        // Append mandatory arguments
        sb.append(action.toString().toLowerCase()).append(" ").append(targets);

        // Append recipe argument or '*'
        sb.append(" ");
        if (recipe == null) {
            // Default to '*' (all recipes) if no specific recipe is set.
            sb.append("*");
        }
        else {
            // Use the specific recipe ID.
            sb.append(recipe);
        }

        return sb.toString();
    }
}