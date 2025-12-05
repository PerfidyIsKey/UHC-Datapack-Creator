package arguments.itemstack;

import shared.item.ItemId;

/**
 * Defines the structure for a Minecraft ItemStack used in commands:
 * <item_id>[component1=value1,component2={...}]
 */
public interface ItemStack {
    /**
     * Returns the base ItemId (e.g., minecraft:goat_horn).
     */
    ItemId getItemId();

    /**
     * Returns the full item stack string, including the item ID and components.
     */
    String build();
}