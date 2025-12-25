package uhc.arguments.item;

import uhc.data.nbt.item.components.ItemComponent;
import uhc.resource.item.ItemResource;
import uhc.resource.item.components.ComponentId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 📦 **ItemStack Argument Builder**
 * <p>
 * Represents an item stack string specifically formatted for Minecraft commands.
 * This builder supports the modern component-based syntax introduced in 1.20.5.
 * </p>
 * <p>
 * <b>Format:</b> {@code <item_id>[<component_id>=<value>, !<removed_component_id>]}
 * </p>
 */
public class ItemStack {

    // --- ⚙️ State & Fields ---

    /** * The base item type (e.g., minecraft:diamond_sword).
     * This field is immutable to ensure the identity of the stack remains constant.
     */
    private final ItemResource itemResource;

    /** * Internal list of components to be added or modified on the stack.
     */
    private final List<ItemComponent> components = new ArrayList<>();

    /** * Internal list of component IDs to be explicitly removed from the item's default state.
     * These are prefixed with an exclamation mark (!) in the final string.
     */
    private final List<ComponentId> removedComponents = new ArrayList<>();

    // --- 🏗️ Constructors & Factories ---

    /**
     * Private constructor to enforce use of the static factory method.
     * @param itemResource The base resource identifier for the item.
     */
    private ItemStack(ItemResource itemResource) {
        this.itemResource = itemResource;
    }

    /**
     * Initializes a new ItemStack builder for command arguments.
     * <p><b>Error Catching:</b> Validates that the provided resource is not null
     * and calls its {@code validate()} method to ensure legal Minecraft syntax.</p>
     * @param itemResource The base item type (supports {@code ItemId} or {@code DynamicItem}).
     * @return A new ItemStack instance ready for modification.
     * @throws NullPointerException if itemResource is null.
     * @throws IllegalStateException if itemResource has an invalid syntax.
     */
    public static ItemStack create(ItemResource itemResource) {
        Objects.requireNonNull(itemResource, "Item resource cannot be null.");
        // Ensure the resource is valid before starting the build process
        itemResource.validate();
        return new ItemStack(itemResource);
    }

    // --- 🛠️ Fluent API (Modifications) ---

    /**
     * Adds a data component to the item stack.
     * <p><b>Format:</b> {@code component_id=value}</p>
     * @param component The component implementation (e.g., Damage, CustomName).
     * @return This instance for method chaining.
     * @throws NullPointerException if component is null.
     */
    public ItemStack with(ItemComponent component) {
        Objects.requireNonNull(component, "Component to add cannot be null.");
        this.components.add(component);
        return this;
    }

    /**
     * Marks a specific component for removal from the item's default state.
     * <p><b>Format:</b> {@code !component_id}</p>
     * @param componentId The registry ID of the component to remove (e.g., {@code !enchantments}).
     * @return This instance for method chaining.
     * @throws NullPointerException if componentId is null.
     */
    public ItemStack remove(ComponentId componentId) {
        Objects.requireNonNull(componentId, "Component ID to remove cannot be null.");
        this.removedComponents.add(componentId);
        return this;
    }

    // --- 🚀 Finalization & Serialization ---

    /**
     * Builds the final SNBT string ready for use in Minecraft commands.
     * <p><b>Logic:</b> Aggregates all removals and additions into a comma-separated list
     * enclosed in square brackets.</p>
     * <p>Example Output: {@code minecraft:iron_sword[damage=5,!enchantments]}</p>
     * @return A formatted string representing the full item stack.
     * @throws IllegalStateException if the item resource location is missing.
     */
    public String build() {
        String baseId = itemResource.getResourceLocation();
        if (baseId == null || baseId.isBlank()) {
            throw new IllegalStateException("Cannot build ItemStack with an empty ResourceLocation.");
        }

        StringBuilder builder = new StringBuilder(baseId);
        List<String> entries = new ArrayList<>();

        // 1. Process explicit removals (!id)
        for (ComponentId id : removedComponents) {
            entries.add("!" + id.getResourceLocation());
        }

        // 2. Process additions and modifications (id=value)
        for (ItemComponent component : components) {
            // Note: toNbt().toString() must return valid SNBT for the command line
            entries.add(component.getId().getResourceLocation() + "=" + component.toNbt().toString());
        }

        // 3. Append component block if entries exist
        if (!entries.isEmpty()) {
            builder.append("[");
            builder.append(String.join(",", entries));
            builder.append("]");
        }

        return builder.toString();
    }

    /**
     * Returns the formatted item stack string. Equivalent to calling {@link #build()}.
     * @return The built string.
     */
    @Override
    public String toString() {
        return build();
    }
}