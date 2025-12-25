package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.item.ItemStack;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🎁 **Give Command Builder**
 * <p>
 * Provides a fluent API for the {@code /give} command. This command is used to
 * grant items directly to player inventories.
 * </p>
 * <p>
 * <b>Syntax:</b> {@code /give <targets> <item> [<count>]}
 * </p>
 */
public class GiveCommand implements MinecraftCommand {
    private final Entity targets;
    private final ItemStack item;
    private int count = 1; // Default to 1 as per Minecraft standard

    private GiveCommand(Entity targets, ItemStack item) {
        // Validation to prevent null command components
        this.targets = Objects.requireNonNull(targets, "Target entity selector cannot be null.");
        this.item = Objects.requireNonNull(item, "ItemStack cannot be null.");
    }

    /**
     * Initializes a new GiveCommand builder.
     * @param targets The entity/entities to receive the item (e.g., @a, @p).
     * @param item The item stack, including potential NBT data.
     * @return A new GiveCommand instance.
     */
    public static GiveCommand create(Entity targets, ItemStack item) {
        return new GiveCommand(targets, item);
    }

    /**
     * Sets the amount of the item to give.
     * @param count The quantity (Minecraft standard range is usually 1-64 per stack,
     * though the command supports up to 6400).
     * @return The current builder instance.
     * @throws IllegalArgumentException if count is less than 1.
     */
    public GiveCommand count(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count must be at least 1.");
        }
        this.count = count;
        return this;
    }

    /**
     * Generates the final Minecraft command string.
     * <p>Note: The item count is omitted if it is 1, as 1 is the implicit default
     * in Minecraft syntax.</p>
     * @return The formatted command string.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("give ");

        // Append mandatory targets and the item build (which includes ID and NBT)
        sb.append(targets).append(" ").append(item.build());

        // Minecraft defaults to 1 if the count argument is missing.
        // We only append it if the user wants more than one or if count was modified.
        if (count > 1) {
            sb.append(" ").append(count);
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return generate();
    }
}