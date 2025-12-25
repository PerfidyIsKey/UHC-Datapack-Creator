package uhc.arguments.item;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.resource.item.ItemResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 🔍 **Item Predicate Generator**
 * <p>
 * Generates a Minecraft item predicate string used for identifying specific items
 * based on their type and data components.
 * </p>
 * <p><b>Format:</b> {@code <item_type>[<component_tests>]}</p>
 */
public class ItemPredicate {

    // --- ⚙️ State & Fields ---

    /** * The base item type or item tag to match (e.g., "minecraft:diamond_sword"). */
    private final ItemResource itemType;

    /** * A collection of {@link ItemComponent} requirements that the item must satisfy. */
    private final List<ItemComponent> componentTests = new ArrayList<>();

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce controlled instantiation.
     * @param itemType The {@link ItemResource} identifying the base item or tag.
     */
    private ItemPredicate(ItemResource itemType) {
        this.itemType = Objects.requireNonNull(itemType, "ItemResource cannot be null for a predicate.");
    }

    /**
     * Creates a new item predicate builder for a specific item type or tag.
     * @param itemType The {@link ItemResource} (e.g., ItemId.STICK or a custom ItemResource).
     * @return A new ItemPredicate instance.
     */
    public static ItemPredicate create(ItemResource itemType) {
        return new ItemPredicate(itemType);
    }

    // --- 🛠️ Builder Methods ---

    /**
     * Adds a data component requirement to the predicate test list.
     * <p><b>Error Catching:</b> Silently ignores null components to prevent
     * malformed predicate strings like {@code stick[null]}.</p>
     * @param component The {@link ItemComponent} test to add.
     * @return This builder instance for chaining.
     */
    public ItemPredicate withTest(ItemComponent component) {
        if (component != null) {
            this.componentTests.add(component);
        }
        return this;
    }

    /**
     * Adds multiple data component requirements at once.
     * @param components A list of components to add.
     * @return This builder instance.
     */
    public ItemPredicate withTests(List<ItemComponent> components) {
        if (components != null) {
            components.forEach(this::withTest);
        }
        return this;
    }

    // --- 🛰️ Serialization Logic ---

    /**
     * Serializes the builder into a valid Minecraft item predicate string.
     * <p><b>Error Catching:</b> Validates the base item resource before construction.
     * If an error occurs during component serialization, it throws an informative
     * {@link IllegalStateException}.</p>
     * @return A formatted predicate string (e.g., {@code minecraft:bundle[minecraft:bundle_contents=[...]]}).
     * @throws IllegalStateException if a component fails to serialize correctly.
     */
    public String getAsPredicateString() {
        // Ensure the base item/tag is syntactically valid
        itemType.validate();

        StringBuilder builder = new StringBuilder(itemType.getResourceLocation());

        if (!componentTests.isEmpty()) {
            try {
                // Map each component to its NBT representation (which matches the component ID)
                String tests = componentTests.stream()
                        .map(component -> component.toNbt().toString())
                        .collect(Collectors.joining(","));

                if (!tests.isEmpty()) {
                    builder.append("[").append(tests).append("]");
                }
            } catch (Exception e) {
                throw new IllegalStateException("Critical failure while serializing ItemPredicate components for: " + itemType, e);
            }
        }

        return builder.toString();
    }

    /**
     * Returns the serialized predicate for direct use in commands or data files.
     * @return The result of {@link #getAsPredicateString()}.
     */
    @Override
    public String toString() {
        return getAsPredicateString();
    }
}