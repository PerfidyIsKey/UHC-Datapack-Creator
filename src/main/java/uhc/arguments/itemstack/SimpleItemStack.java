package uhc.arguments.itemstack;

import uhc.resource.ItemId;

/**
 * 📦 **Simple Item Stack Implementation**
 * <p>
 * Implements the {@code ItemStack} interface for items that do **not** require
 * custom data components (NBT or data tags). This class is used when only the base
 * {@code ItemId} is needed in the command string (e.g., {@code minecraft:diamond}).
 * </p>
 */
public class SimpleItemStack implements ItemStack {

    /** The fixed, non-component item ID for this stack. */
    private final ItemId itemId;

    /**
     * Private constructor to enforce use of the static factory method {@code create()}.
     * @param itemId The fixed {@code ItemId} enum constant.
     * @throws IllegalArgumentException if the provided {@code itemId} is null.
     */
    private SimpleItemStack(ItemId itemId) {
        if (itemId == null) {
            // CRITICAL ERROR CATCH: Ensure the fundamental ItemId is present.
            throw new IllegalArgumentException("ItemId cannot be null for SimpleItemStack.");
        }
        this.itemId = itemId;
    }

    /**
     * Static factory method to create an instance of a simple item stack.
     * @param itemId The {@code ItemId} constant representing the item type.
     * @return A new {@code SimpleItemStack} instance.
     */
    public static SimpleItemStack create(ItemId itemId) {
        return new SimpleItemStack(itemId);
    }

    // --- Implementation of ItemStack Interface ---

    /**
     * Retrieves the fixed {@code ItemId} associated with this stack.
     * @return The item's resource identifier.
     */
    @Override
    public ItemId getItemId() {
        return itemId;
    }

    /**
     * Generates the command string for this simple item stack.
     * <p>
     * For a simple stack, this is just the item's resource location without any trailing
     * brackets for components (e.g., {@code minecraft:air}).
     * </p>
     * @return The canonical string representation of the item for command usage.
     * * @implNote It is assumed that {@code ItemId.getResourceLocation()} returns the proper
     * Minecraft resource location string (e.g., {@code "minecraft:air"}).
     */
    @Override
    public String build() {
        return itemId.getResourceLocation();
    }
}