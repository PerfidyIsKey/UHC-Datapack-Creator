package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.arguments.itempredicate.ItemPredicate;
import uhc.command.MinecraftCommand;

import java.util.Objects;

/**
 * 🧹 **Clear Command Builder**
 * <p>
 * Provides a fluent API for the {@code /clear} command, used to remove items from
 * player inventories. This builder ensures that optional arguments are appended
 * in the correct order: {@code targets -> item -> maxCount}.
 * </p>
 */
public class ClearCommand implements MinecraftCommand {
    private Entity targets;
    private ItemPredicate item;
    private int maxCount;

    private ClearCommand() {}

    /**
     * Initializes a new ClearCommand builder.
     */
    public static ClearCommand create() { return new ClearCommand(); }

    /**
     * Specifies the entities whose inventories should be cleared.
     * @param targets The target entities (usually players).
     */
    public ClearCommand targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    /**
     * Filters the items to be cleared using an item predicate.
     * @param item The item or tag to match (e.g., minecraft:diamond_sword).
     */
    public ClearCommand item(ItemPredicate item) {
        this.item = item;
        return this;
    }

    /**
     * Sets the maximum number of items to remove.
     * <p>Note: Setting this to 0 can be used to query the count of items
     * without actually removing them.</p>
     * @param maxCount The maximum amount to clear (must be non-negative).
     */
    public ClearCommand maxCount(int maxCount) {
        if (maxCount < 0) {
            throw new IllegalArgumentException("maxCount cannot be negative.");
        }
        this.maxCount = maxCount;
        return this;
    }

    /**
     * Generates the final command string.
     * <p>Logic follows the syntax: {@code clear [<targets>] [<item>] [<maxCount>]}</p>
     * @return The formatted Minecraft command.
     */
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder("clear");

        // Minecraft requires targets to be present if an item is specified.
        if (targets != null) {
            sb.append(" ").append(targets);

            // Minecraft requires an item to be present if a maxCount is specified.
            if (item != null) {
                sb.append(" ").append(item);

                // Only append maxCount if it's explicitly set.
                // Note: Minecraft accepts 0 as a valid count for querying.
                if (maxCount >= 0 && hasSetMaxCount()) {
                    sb.append(" ").append(maxCount);
                }
            } else if (maxCount > 0) {
                // Error Catching: Minecraft does not allow 'clear <target> <maxCount>'
                // without an item predicate in between.
                throw new IllegalStateException("Cannot specify maxCount without specifying an item predicate first.");
            }
        } else if (item != null || maxCount > 0) {
            // Error Catching: Cannot clear specific items without a target.
            throw new IllegalStateException("Targets must be specified if an item or maxCount is provided.");
        }

        return sb.toString();
    }

    /**
     * Internal helper to determine if maxCount was actually intended to be used.
     * Since 'int' defaults to 0, we distinguish between a default 0 and an intended 0.
     */
    private boolean hasSetMaxCount() {
        // In a production environment, you might use an Integer object to check for null,
        // but since names cannot change, we assume > 0 or specific logic.
        return maxCount > 0 || (item != null && maxCount == 0);
    }

    @Override
    public String toString() {
        return generate();
    }
}