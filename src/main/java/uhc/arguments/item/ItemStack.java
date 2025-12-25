package uhc.arguments.item;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.resource.item.ItemId;
import uhc.resource.item.components.ComponentId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 📦 **ItemStack Argument Builder**
 * <p>
 * Represents an item stack string for Minecraft commands (1.20.5+).
 * Format: {@code <item_id>[<components>]}.
 * </p>
 */
public class ItemStack {

    // --- Private Fields ---

    /** * The base item type. */
    private final ItemId itemId;

    /** * List of components to be added or modified. */
    private final List<ItemComponent> components = new ArrayList<>();

    /** * List of component IDs to be explicitly removed (prefixed with !). */
    private final List<ComponentId> removedComponents = new ArrayList<>();

    // --- Constructors & Factories ---

    /**
     * Private constructor.
     * @param itemId The base ID of the item.
     */
    private ItemStack(ItemId itemId) {
        this.itemId = itemId;
    }

    /**
     * Initializes a new ItemStack for command arguments.
     * @param itemId The base item type.
     * @return A new ItemStack instance.
     * @throws NullPointerException if itemId is null.
     */
    public static ItemStack create(ItemId itemId) {
        return new ItemStack(Objects.requireNonNull(itemId, "Item ID cannot be null."));
    }

    // --- Fluent API (Modifications) ---

    /**
     * Adds a data component to the item stack.
     * <p>Format in string: {@code component_id=value}</p>
     * @param component The component implementation to add.
     * @return This instance for chaining.
     * @throws NullPointerException if component is null.
     */
    public ItemStack with(ItemComponent component) {
        Objects.requireNonNull(component, "Component cannot be null.");
        this.components.add(component);
        return this;
    }

    /**
     * Marks a component for removal from the item's default state.
     * <p>Format in string: {@code !component_id}</p>
     * @param componentId The registry ID of the component to remove.
     * @return This instance for chaining.
     * @throws NullPointerException if componentId is null.
     */
    public ItemStack remove(ComponentId componentId) {
        Objects.requireNonNull(componentId, "Component ID to remove cannot be null.");
        this.removedComponents.add(componentId);
        return this;
    }

    // --- Finalization ---

    /**
     * Builds the final command-ready string.
     * <p>Example output: {@code minecraft:iron_sword[damage=5,!enchantments]}</p>
     * @return The formatted item stack string.
     */
    public String build() {
        StringBuilder builder = new StringBuilder(itemId.getResourceLocation());

        List<String> entries = new ArrayList<>();

        // 1. Handle removals (!id)
        for (ComponentId id : removedComponents) {
            entries.add("!" + id.getResourceLocation());
        }

        // 2. Handle additions (id=value)
        for (ItemComponent component : components) {
            // toNbt().toString() provides the SNBT value required for command syntax
            entries.add(component.getId().getResourceLocation() + "=" + component.toNbt().toString());
        }

        // 3. Wrap in brackets if any components exist
        if (!entries.isEmpty()) {
            builder.append("[");
            builder.append(String.join(",", entries));
            builder.append("]");
        }

        return builder.toString();
    }

    /**
     * Equivalent to {@link #build()}.
     */
    @Override
    public String toString() {
        return build();
    }
}