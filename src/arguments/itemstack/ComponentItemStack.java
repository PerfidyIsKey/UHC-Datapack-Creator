package arguments.itemstack;

import arguments.itemstack.components.DamageComponent; // New required import
import arguments.itemstack.components.UnbreakableComponent; // New required import
import shared.item.ItemId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * An ItemStack that allows for the structured addition of Minecraft 1.20.5+ data components
 * (e.g., custom_name, enchantments, attribute_modifiers).
 * Implements the Builder Pattern for component chaining to generate a full SNBT command string.
 * <p>
 * Final command format: {@code item_id[component1=value1,component2=value2]}
 */
public class ComponentItemStack implements ItemStack {

    private final ItemId itemId;
    // Stores components. Key is the component name (e.g., "damage"), Value is the component object.
    private final Map<String, ItemComponentTag> components;

    /**
     * Private constructor to enforce starting the chain with the static factory method.
     * @param itemId The base item ID resource location (e.g., minecraft:splash_potion).
     * @throws IllegalArgumentException if the ItemId is null.
     */
    private ComponentItemStack(ItemId itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null. An ItemStack must be based on a valid item.");
        }
        this.itemId = itemId;
        // LinkedHashMap maintains insertion order, which is good practice for readability.
        this.components = new LinkedHashMap<>();
    }

    /**
     * Static factory method to start the builder chain for creating an item stack with components.
     * @param itemId The ItemId enum constant (e.g., ItemId.SPLASH_POTION).
     * @return A new ComponentItemStack instance ready for component additions.
     */
    public static ComponentItemStack create(ItemId itemId) {
        return new ComponentItemStack(itemId);
    }

    // --- Component Chain Methods ---

    /**
     * Adds an item component to the stack. If a component with the same name already exists,
     * it will be overwritten by this new one.
     *
     * @param component The ItemComponentTag instance to add.
     * @return This ComponentItemStack instance for fluent chaining.
     * @throws IllegalArgumentException if the component is null.
     * @throws IllegalStateException if the component's {@code buildComponentString()} does not follow the "name=value" format.
     */
    public ComponentItemStack addComponent(ItemComponentTag component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }

        // We rely on the convention that buildComponentString() returns 'component_name=value'
        String fullString = component.buildComponentString();
        int equalsIndex = fullString.indexOf('=');

        String componentName;
        if (equalsIndex > 0) {
            // Extract the component name before the first '='
            componentName = fullString.substring(0, equalsIndex);
        } else {
            // This enforces the contract that every component must provide its name and value separated by '='
            throw new IllegalStateException(
                    "Component tag string did not contain '='. Cannot determine component name from: " + fullString +
                            ". All ItemComponentTag implementations must return 'name=value'."
            );
        }

        this.components.put(componentName, component);
        return this;
    }

    /**
     * Auxiliary method for setting the item's damage value (durability loss).
     * Uses the external, static DamageComponent class.
     * * @param amount The numerical damage value (e.g., 0 for full durability, 50 for damage).
     * @return This ComponentItemStack instance for fluent chaining.
     */
    public ComponentItemStack damage(int amount) {
        return addComponent(DamageComponent.create(amount));
    }

    /**
     * Auxiliary method for marking the item as unbreakable.
     * Uses the external, static UnbreakableComponent class, which has the value {@code {}}.
     * * @return This ComponentItemStack instance for fluent chaining.
     */
    public ComponentItemStack unbreakable() {
        return addComponent(UnbreakableComponent.create());
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
     * Returns the full item stack string in the required Minecraft SNBT format:
     * {@code item_id[component1=value1,component2=value2, ...]}
     *
     * @return The complete SNBT command argument string.
     */
    @Override
    public String build() {
        if (components.isEmpty()) {
            // If no components were added, return only the resource location (e.g., "minecraft:diamond").
            return itemId.getResourceLocation();
        }

        // Join components using a comma, wrapped in square brackets: [...,...,...]
        StringJoiner componentJoiner = new StringJoiner(",", "[", "]");

        // Add the SNBT string for each component (e.g., "damage=10") to the joiner.
        for (ItemComponentTag component : components.values()) {
            componentJoiner.add(component.buildComponentString());
        }

        return itemId.getResourceLocation() + componentJoiner.toString();
    }
}