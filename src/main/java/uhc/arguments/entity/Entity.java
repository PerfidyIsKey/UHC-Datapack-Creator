package uhc.arguments.entity;

import uhc.arguments.targetselector.SelectorArgumentsBuilder;
import uhc.arguments.targetselector.TargetSelector;

/**
 * Represents the {@code <targets>} argument in Minecraft commands.
 * <p>
 * This class abstracts the three ways to specify an entity or group of entities:
 * a player name, a raw UUID, or a target selector (@p, @e, @r, @a, @s) with optional arguments.
 */
public class Entity {

    // The final, immutable string representing the entity argument (name, UUID, or selector string).
    private final String entityIdentifier;

    /**
     * Private constructor to enforce object creation via static factory methods.
     *
     * @param identifier The raw entity identifier string.
     * @throws IllegalArgumentException if the identifier is null or empty.
     */
    private Entity (String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity identifier string cannot be null or empty.");
        }
        this.entityIdentifier = identifier;
    }

    // --- Factory Methods for Player Name or UUID ---

    /**
     * Creates an Entity argument using a player's **name**.
     *
     * @param name The target player's name (e.g., "Notch").
     * @return A new Entity instance.
     */
    public static Entity ofName(String name) {
        return new Entity(name);
    }

    /**
     * Creates an Entity argument using an entity's **UUID**.
     *
     * @param uuid The target entity's UUID string.
     * @return A new Entity instance.
     */
    public static Entity ofUuid(String uuid) {
        return new Entity(uuid);
    }

    // --- Factory Methods for Target Selectors ---

    /**
     * Creates an Entity argument using a simple {@link TargetSelector}
     * (e.g., {@code @a}, {@code @p}).
     *
     * @param selector The target selector constant (e.g., TargetSelector.ALL_PLAYERS).
     * @return A new Entity instance.
     * @throws IllegalArgumentException if the selector is null.
     */
    public static Entity ofSelector(TargetSelector selector) {
        if (selector == null) {
            throw new IllegalArgumentException("TargetSelector cannot be null.");
        }
        return new Entity(selector.toString());
    }

    /**
     * Creates an Entity argument using a {@link TargetSelector} with **arguments** * (e.g., {@code @e[type=cow,limit=1]}).
     *
     * @param selector The target selector constant.
     * @param argumentsBuilder The builder containing the selector arguments.
     * @return A new Entity instance.
     * @throws IllegalArgumentException if either argument is null.
     */
    public static Entity ofSelector(TargetSelector selector, SelectorArgumentsBuilder argumentsBuilder) {
        if (selector == null) {
            throw new IllegalArgumentException("TargetSelector cannot be null.");
        }
        if (argumentsBuilder == null) {
            throw new IllegalArgumentException("SelectorArgumentsBuilder cannot be null.");
        }
        // Concatenate the selector (e.g., "@e") and the argument string (e.g., "[type=cow]")
        return new Entity(selector + argumentsBuilder.build());
    }

    /**
     * Returns the raw selector, name, or UUID string, which is used in the final command.
     */
    @Override
    public String toString() {
        return entityIdentifier;
    }
}