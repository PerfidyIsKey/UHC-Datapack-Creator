package arguments.targetselector;

/**
 * Represents the base target selector variables in Minecraft commands
 * (e.g., @p, @a, @e, @r, @s).
 */
public enum TargetSelector {
    ALL_PLAYERS("@a"),
    ALL_ENTITIES("@e"),
    NEAREST_ENTITY("@n"),
    NEAREST_PLAYER("@p"),
    RANDOM_PLAYER("@r"),
    SENDER("@s");

    private final String selector;

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