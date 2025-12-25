package uhc.arguments.itemstack;

import uhc.resource.item.ItemId;

/**
 * 🧱 **Minecraft Item Stack Argument**
 * <p>
 * Defines the structure for a complete item stack argument used within Minecraft commands
 * (such as {@code /give}, {@code /loot}, or {@code /item replace}).
 * </p>
 * <p>
 * The output string format must adhere to the Minecraft syntax:
 * {@code <item_id>[component1=value1,component2={...}]}
 * </p>
 */
public interface ItemStack {

    /**
     * Retrieves the base resource location (ID) of the item.
     * This is typically a constant, unchangeable property of the item stack definition.
     * * @return The item's resource ID (e.g., {@code minecraft:diamond_sword}).
     */
    ItemId getItemId();

    /**
     * Generates the final, complete item stack command argument string.
     * This includes the {@link #getItemId()} and all associated {@link ItemComponent} tags.
     * * @return The full item stack string (e.g., {@code minecraft:bow[unbreakable=true,damage=5]}).
     */
    String build();
}