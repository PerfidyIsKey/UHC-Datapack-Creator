package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.tag.EntityTag;

import java.util.Objects;

/**
 * 🏷️ **Tag Command Builder (TagCommand)**
 * <p>
 * Provides a flattened, type-safe API for constructing Minecraft {@code /tag} commands.
 * This class eliminates intermediate builder states by providing direct static methods
 * for the three primary tag operations: {@code add}, {@code remove}, and {@code list}.
 * </p>
 * <p>
 * <b>Syntax Support:</b>
 * <ul>
 * <li>{@code /tag <targets> add <name>}</li>
 * <li>{@code /tag <targets> remove <name>}</li>
 * <li>{@code /tag <targets> list}</li>
 * </ul>
 * </p>
 */
public class TagCommand implements MinecraftCommand {

    // --- 📄 Fields ---

    /** * The internal formatted sub-command segment following the base "tag" keyword.
     * This string typically contains the target selector, the action keyword,
     * and the optional tag identifier (e.g., "@p add objective_complete").
     */
    private final String instruction;

    // --- 🏗️ Private Constructor ---

    /**
     * Constructs a TagCommand with a pre-validated instruction segment.
     * <p><b>Error Catching:</b> Strictly enforces that the internal state is non-null
     * to prevent malformed command generation.</p>
     * @param instruction The formatted segment following the base "tag" keyword.
     * @throws NullPointerException if instruction is null with a clear error message.
     */
    private TagCommand(String instruction) {
        this.instruction = Objects.requireNonNull(instruction, "Tag Error: Internal instruction string cannot be null.");
    }

    // --- 🎯 Static Entry Points ---

    /**
     * Constructs a command to add a specific tag to one or more entities.
     * <p><b>Validation:</b> Triggers {@link EntityTag#validate()} to ensure the tag
     * name follows Minecraft's allowed character sets before the command is instantiated.</p>
     * @param target  The {@link Entity} selector representing the targets; must not be null.
     * @param tagName The {@link EntityTag} resource to apply; must not be null.
     * @return A new {@link TagCommand} instance configured for the 'add' operation.
     * @throws NullPointerException if target or tagName is null.
     * @throws RuntimeException if the tagName fails internal validation.
     */
    public static TagCommand add(Entity target, EntityTag tagName) {
        Objects.requireNonNull(target, "Tag Add Error: Target entity selector cannot be null.");
        Objects.requireNonNull(tagName, "Tag Add Error: EntityTag object cannot be null.");

        try {
            // Validate tag name (e.g., check for spaces or illegal characters)
            tagName.validate();

            // Format: <targets> add <name>
            final String segment = String.format("%s add %s", target.toString(), tagName.getTagName());
            return new TagCommand(segment);
        } catch (Exception e) {
            throw new RuntimeException("Tag Add Error: Failed to validate or format tag: " + tagName, e);
        }
    }

    /**
     * Constructs a command to remove a specific tag from one or more entities.
     * <p><b>Validation:</b> Triggers {@link EntityTag#validate()} to ensure the tag
     * name is legitimate before command construction.</p>
     * @param target  The {@link Entity} selector representing the targets; must not be null.
     * @param tagName The {@link EntityTag} resource to remove; must not be null.
     * @return A new {@link TagCommand} instance configured for the 'remove' operation.
     * @throws NullPointerException if target or tagName is null.
     * @throws RuntimeException if the tagName fails internal validation.
     */
    public static TagCommand remove(Entity target, EntityTag tagName) {
        Objects.requireNonNull(target, "Tag Remove Error: Target entity selector cannot be null.");
        Objects.requireNonNull(tagName, "Tag Remove Error: EntityTag object cannot be null.");

        try {
            tagName.validate();

            // Format: <targets> remove <name>
            final String segment = String.format("%s remove %s", target.toString(), tagName.getTagName());
            return new TagCommand(segment);
        } catch (Exception e) {
            throw new RuntimeException("Tag Remove Error: Failed to validate or format tag: " + tagName, e);
        }
    }

    /**
     * Constructs a command to list all tags currently assigned to the target entity or entities.
     * <p>This sub-command does not require a tag name as it returns the full collection of tags.</p>
     * @param target The {@link Entity} selector representing the targets; must not be null.
     * @return A new {@link TagCommand} instance configured for the 'list' operation.
     * @throws NullPointerException if target is null.
     */
    public static TagCommand list(Entity target) {
        Objects.requireNonNull(target, "Tag List Error: Target entity selector cannot be null.");

        // Format: <targets> list
        return new TagCommand(target.toString() + " list");
    }

    // --- ⚙️ Generation & Output ---

    /**
     * Generates the finalized Minecraft command string ready for execution.
     * <p>The result is prefixed with the standard "tag " base command.</p>
     * @return The complete command (e.g., "tag @a add game_active").
     */
    @Override
    public String generate() {
        return "tag " + this.instruction;
    }

    /**
     * Returns the generated command string.
     * <p><b>Error Catching:</b> If generation fails, it provides a clear
     * error comment instead of a silent fallback.</p>
     * @return The result of {@link #generate()}.
     */
    @Override
    public String toString() {
        try {
            return generate();
        } catch (Exception e) {
            return "/* CRITICAL ERROR: Unable to generate TagCommand - " + e.getMessage() + " */";
        }
    }
}