package uhc.arguments.itempredicate;

import uhc.resource.ItemId;

import java.util.List;
import java.util.StringJoiner;

/**
 * Concrete implementation for the Goat Horn Item Predicate.
 * This class can be extended to include tests (NBT filters) if needed later.
 */
public class SimpleItemPredicate implements ItemPredicate {
    private final ItemId itemId;
    // In a full implementation, this list would contain TestPredicateTag objects
    private final List<Object> tests;

    private SimpleItemPredicate(ItemId itemId) {
        this.itemId = itemId;
        this.tests = List.of(); // No tests for a simple predicate
    }

    public static SimpleItemPredicate create(ItemId itemId) {
        return new SimpleItemPredicate(itemId, List.of());
    }

    // Constructor that would support tests if implemented
    private SimpleItemPredicate(ItemId itemId, List<Object> tests) {
        this.itemId = itemId;
        this.tests = tests;
    }

    @Override
    public ItemId getItemId() {
        return itemId;
    }

    /**
     * Builds the full item predicate string.
     * Example: minecraft:goat_horn[durability={min:10,max:20}]
     */
    @Override
    public String build() {
        StringBuilder sb = new StringBuilder(itemId.getResourceLocation());

        if (!tests.isEmpty()) {
            // Placeholder for if item tests were implemented
            StringJoiner testsJoiner = new StringJoiner(",", "[", "]");
            // Logic to add test strings here
            sb.append(testsJoiner.toString());
        }

        return sb.toString();
    }

    /**
     * Overrides the default toString() to return the command-ready string.
     * This is what StringBuilder uses when you call sb.append(item).
     */
    @Override
    public String toString() {
        return build(); // Delegates to the method that creates the command string
    }
}