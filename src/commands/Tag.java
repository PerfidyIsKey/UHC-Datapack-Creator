package commands;

import uhc.arguments.entity.Entity;
import uhc.resource.StaticEntityTag;
import commands.tag.TagAction;

/**
 * Fluent builder for the Minecraft /tag command.
 * The structure is: tag <targets> <action> [name]
 */
public class Tag {
    private final Entity target;
    private final TagAction action;
    private StaticEntityTag tagName;

    private Tag(Entity target, TagAction action) {
        if (target == null) {
            throw new IllegalArgumentException("Target entity cannot be null.");
        }
        if (action == null) {
            throw new IllegalArgumentException("Tag action cannot be null.");
        }
        this.target = target;
        this.action = action;
    }

    /**
     * Factory method to start a new tag command (e.g., tag @a ...).
     * @param target The target entity selector.
     * @param action The action (ADD, REMOVE).
     */
    public static Tag action(Entity target, TagAction action) {
        if (action == TagAction.LIST) {
            throw new IllegalArgumentException("Use Tag.list() for the list action, as it requires no tag name.");
        }
        return new Tag(target, action);
    }

    /**
     * Factory method specifically for the list action (e.g., tag @a list).
     * @param target The target entity selector.
     */
    public static Tag list(Entity target) {
        return new Tag(target, TagAction.LIST);
    }

    /**
     * Specifies the type-safe name for the ADD or REMOVE action.
     * This method is optional for LIST, but required for ADD/REMOVE.
     * @param tagName The type-safe name of the tag (e.g., StaticEntityTag.IS_FLYING).
     */
    public Tag name(StaticEntityTag tagName) {
        if (this.action == TagAction.LIST) {
            throw new IllegalStateException("The LIST action does not accept a tag name.");
        }
        if (tagName == null) {
            throw new IllegalArgumentException("Tag name cannot be null for ADD/REMOVE actions.");
        }
        this.tagName = tagName;
        return this;
    }

    /**
     * Finalizes the builder and creates the command string.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("tag ");
        sb.append(target).append(" ").append(action);

        // ADD and REMOVE must have a name
        if (action != TagAction.LIST) {
            if (tagName == null) {
                throw new IllegalStateException("Tag name is required for the " + action.toString().toUpperCase() + " action.");
            }
            sb.append(" ").append(tagName);
        }

        return sb.toString();
    }
}