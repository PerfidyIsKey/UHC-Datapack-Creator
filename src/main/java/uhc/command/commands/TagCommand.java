package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.tag.EntityTag;

import java.util.Objects;

/**
 * 🏷️ **Tag Command Builder**
 * <p>
 * Provides a fluent, type-safe API for constructing Minecraft {@code /tag} commands.
 * This builder enforces the correct syntax by providing specific methods for
 * {@code add}, {@code remove}, and {@code list} operations.
 * </p>
 */
public class TagCommand implements MinecraftCommand {

    /** The entity selector representing the target(s) of this command. */
    private final Entity target;

    /** The specific sub-command action string (add, remove, or list). */
    private String action;

    /** The namespaced or indexed tag name to be applied or removed. */
    private EntityTag tagName;

    // --- 🏗️ Constructors ---

    /**
     * Private constructor to initialize the target.
     * Command actions are assigned through fluent methods.
     * @param target The non-null entity selector.
     */
    private TagCommand(Entity target) {
        this.target = Objects.requireNonNull(target, "Target entity selector cannot be null.");
    }

    // --- 🛠️ Static Factory Entry Point ---

    /**
     * Begins the construction of a tag command for a specific target.
     * @param target The entity selector to target.
     * @return A new TagCommand builder instance.
     * @throws NullPointerException if target is null.
     */
    public static TagCommand target(Entity target) {
        return new TagCommand(target);
    }

    // --- 🛰️ Action Methods ---

    /**
     * Configures the command to add a specific tag to the target.
     * @param tagName The {@link EntityTag} to add.
     * @return The current builder instance.
     * @throws NullPointerException if tagName is null.
     */
    public TagCommand add(EntityTag tagName) {
        this.action = "add";
        this.tagName = Objects.requireNonNull(tagName, "Tag to add cannot be null.");
        this.tagName.validate();
        return this;
    }

    /**
     * Configures the command to remove a specific tag from the target.
     * @param tagName The {@link EntityTag} to remove.
     * @return The current builder instance.
     * @throws NullPointerException if tagName is null.
     */
    public TagCommand remove(EntityTag tagName) {
        this.action = "remove";
        this.tagName = Objects.requireNonNull(tagName, "Tag to remove cannot be null.");
        this.tagName.validate();
        return this;
    }

    /**
     * Configures the command to list all tags currently on the target entity.
     * @return The current builder instance.
     */
    public TagCommand list() {
        this.action = "list";
        this.tagName = null; // List does not use a tag name
        return this;
    }

    // --- ⚙️ Command Generation ---

    /**
     * Generates the final Minecraft command string.
     * <p><b>Error Catching:</b> Ensures an action has been selected before generation.</p>
     * @return A formatted command string (e.g., "tag @s add IronMan").
     * @throws IllegalStateException if no action (add/remove/list) was specified.
     */
    @Override
    public String generate() {
        if (action == null) {
            throw new IllegalStateException("No action (add, remove, or list) was specified for the TagCommand.");
        }

        StringBuilder sb = new StringBuilder("tag ");

        // Append Target
        sb.append(target).append(" ");

        // Append Action
        sb.append(action);

        // Append Tag Name if applicable
        if (tagName != null) {
            sb.append(" ").append(tagName.getTagName());
        }

        return sb.toString();
    }

    /**
     * Returns the generated command string.
     * @return The raw command string.
     */
    @Override
    public String toString() {
        return generate();
    }
}