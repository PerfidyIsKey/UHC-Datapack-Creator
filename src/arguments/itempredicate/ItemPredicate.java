package arguments.itempredicate;

import shared.item.ItemId;

/**
 * Defines the structure for a Minecraft Item Predicate used in commands:
 * <item_id>[test1=value1,test2={...}]
 * This is used for commands like 'clear' and 'hasitem'.
 */
public interface ItemPredicate {
    /**
     * Returns the base ItemId (e.g., minecraft:goat_horn).
     */
    ItemId getItemId();

    /**
     * Builds the full item predicate string, including the item ID and optional tests.
     */
    String build();
}