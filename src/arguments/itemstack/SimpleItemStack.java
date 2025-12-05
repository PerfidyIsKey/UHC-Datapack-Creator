package arguments.itemstack;

import shared.item.ItemId;

public class SimpleItemStack implements ItemStack {

    private final ItemId itemId;

    /**
     * Private constructor to enforce use of the static factory method.
     * @param itemId The fixed ItemId enum constant.
     */
    private SimpleItemStack(ItemId itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null for SimpleItemStack.");
        }
        this.itemId = itemId;
    }

    /**
     * Static factory method to create an instance.
     * @param itemId The ItemId constant.
     * @return A new SimpleItemStack instance.
     */
    public static SimpleItemStack create(ItemId itemId) {
        return new SimpleItemStack(itemId);
    }

    // --- Implementation of ItemStack Interface ---

    /**
     * Returns the fixed ItemId associated with this stack.
     */
    @Override
    public ItemId getItemId() {
        return itemId;
    }

    /**
     * Returns the string representation of the item, which is just the ItemId's resource location.
     * This fulfills the ItemStack contract.
     */
    @Override
    public String build() {
        // Assuming ItemId has a method (like getResourceLocation())
        // that provides the necessary string for the final item reference.
        // If ItemId is just the name, you might use: itemId.name().toLowerCase()
        return itemId.getResourceLocation();
    }
}