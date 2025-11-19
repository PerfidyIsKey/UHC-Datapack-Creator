package arguments;

import arguments.targetselector.SelectorArgumentsBuilder;
import arguments.targetselector.TargetSelector;

/**
 * Represents the <targets> argument in Minecraft commands.
 * This can be a player name, a target selector (@p, @e, etc.), or a UUID.
 */
public class Entity {
    private String selector;

    private Entity (String selector) {
        this.selector = selector;
    }

    /**
     * Factory method for convenience.
     */
    public static Entity ofName(String name) {
        return new Entity(name);
    }

    public static Entity ofUuid(String uuid) {
        return new Entity(uuid);
    }

    public static Entity ofSelector(TargetSelector selector) {
        return new Entity(selector.toString());
    }

    public static Entity ofSelector(TargetSelector selector, SelectorArgumentsBuilder argumentsBuilder) {
        return new Entity(selector + argumentsBuilder.build());
    }

    /**
     * Returns the raw selector string, which is used in the final command.
     */
    @Override
    public String toString() {
        return selector;
    }
}