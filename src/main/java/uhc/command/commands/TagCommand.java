package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.tag.StaticEntityTag;

import java.util.Objects;

/**
 * 🏷️ **Tag Command Builder**
 * <p>
 * Provides a fluent API for the {@code /tag} command. Tags are strings stored
 * on entities that can be used in target selectors (e.g., {@code @a[tag=uhc_player]}).
 * </p>
 * <p>
 * <b>Syntax Variants:</b>
 * <ul>
 * <li>{@code /tag <targets> add <name>}</li>
 * <li>{@code /tag <targets> remove <name>}</li>
 * <li>{@code /tag <targets> list}</li>
 * </ul>
 * </p>
 */
public class TagCommand implements MinecraftCommand {
    private final Entity target;
    private final TagAction action;
    private StaticEntityTag tagName;

    private TagCommand(Entity target, TagAction action) {
        // Enforce non-nullability for core command components
        this.target = Objects.requireNonNull(target, "Target entity selector cannot be null.");
        this.action = Objects.requireNonNull(action, "Tag action cannot be null.");
    }

    /**
     * Initializes a command for adding or removing tags.
     * @param target The entity selector to target.
     * @param action The operation to perform (ADD or REMOVE).
     * @return A new TagCommand builder instance.
     * @throws IllegalArgumentException if action is LIST (use .list() instead).
     */
    public static TagCommand action(Entity target, TagAction action) {
        if (action == TagAction.LIST) {
            throw new IllegalArgumentException("Action LIST should be initialized via TagCommand.list(target).");
        }
        return new TagCommand(target, action);
    }

    /**
     * Initializes a command specifically to list an entity's tags.
     * @param target The entity to query.
     * @return A new TagCommand builder instance configured for LIST.
     */
    public static TagCommand list(Entity target) {
        return new TagCommand(target, TagAction.LIST);
    }

    /**
     * Sets the specific tag name for the ADD or REMOVE operation.
     * @param tagName The type-safe tag constant from StaticEntityTag.
     * @return The current builder instance.
     * @throws IllegalStateException if called while the action is set to LIST.
     */
    public TagCommand name(StaticEntityTag tagName) {
        if (this.action == TagAction.LIST) {
            throw new IllegalStateException("The 'list' action does not accept a tag name argument.");
        }
        this.tagName = Objects.requireNonNull(tagName, "Tag name cannot be null for ADD/REMOVE actions.");
        return this;
    }

    /**
     * Validates state and generates the final Minecraft command string.
     * @return The formatted command (e.g., "tag @a add alive").
     * @throws IllegalStateException if a tag name is missing for ADD/REMOVE actions.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("tag ");

        // Structure: tag <target> <action>
        sb.append(target).append(" ").append(action);

        // Append the name if the action requires one
        if (action != TagAction.LIST) {
            if (tagName == null) {
                throw new IllegalStateException("Tag name must be specified for action: " + action);
            }
            sb.append(" ").append(tagName);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }

    /**
     * Represents the sub-commands available for /tag.
     */
    public enum TagAction {
        ADD,
        REMOVE,
        LIST;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}