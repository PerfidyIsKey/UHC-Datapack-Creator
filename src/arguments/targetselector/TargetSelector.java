package arguments.targetselector;

/**
 * Defines the base target selector variables used in Minecraft commands.
 * <p>
 * These single-character selectors determine the scope of entity targeting
 * and can optionally be followed by a bracketed list of arguments
 * (e.g., @a[distance=..10]).
 */
public enum TargetSelector {

    /** * Selects all players currently online. Represents the command selector {@code @a}. */
    ALL_PLAYERS("@a"),

    /** * Selects all entities (players, mobs, items, etc.). Represents the command selector {@code @e}. */
    ALL_ENTITIES("@e"),

    /** * * Selects the nearest entity to the command execution source. Represents the command selector {@code @n}. */
    NEAREST_ENTITY("@n"),

    /** * Selects the nearest player to the command execution source. Represents the command selector {@code @p}. */
    NEAREST_PLAYER("@p"),

    /** * Selects a single random player. Represents the command selector {@code @r}. */
    RANDOM_PLAYER("@r"),

    /** * Selects the entity that executed the command (sender). Represents the command selector {@code @s}. */
    SENDER("@s");

    private final String selector;

    /**
     * Private constructor to assign the command string to the enum constant.
     *
     * @param selector The base selector string (e.g., "@p").
     */
    TargetSelector(String selector) {
        this.selector = selector;
    }

    /**
     * Returns the base selector string (e.g., "@a").
     */
    @Override
    public String toString() {
        return selector;
    }
}