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
 * Implements the Builder Pattern for component chaining.
 */
public class ComponentItemStack implements ItemStack {

    private final ItemId itemId;
    private final Map<String, ItemComponentTag> components; // Key=Component Name, Value=Component Value String

    /**
     * Private constructor to enforce starting the chain with the static factory method.
     */
    private ComponentItemStack(ItemId itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null.");
        }
        this.itemId = itemId;
        this.components = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order
    }

    /**
     * Static factory method to start the builder chain for an item with components.
     * @param itemId The ItemId enum constant (e.g., ItemId.TRIDENT).
     * @return A new ComponentItemStack instance.
     */
    public static ComponentItemStack create(ItemId itemId) {
        return new ComponentItemStack(itemId);
    }

    // --- Component Chain Methods ---

    public ComponentItemStack addComponent(ItemComponentTag component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null.");
        }

        // This relies on the convention that buildComponentString() starts with 'name='
        String fullString = component.buildComponentString();
        int equalsIndex = fullString.indexOf('=');

        String componentName;
        if (equalsIndex > 0) {
            componentName = fullString.substring(0, equalsIndex);
        } else {
            // This is critical for catching components that don't follow the name=value pattern
            throw new IllegalStateException("Component tag string did not contain '='. Cannot determine component name from: " + fullString);
        }

        this.components.put(componentName, component);
        return this;
    }

    /**
     * Auxiliary method for damage.
     * Uses the external, static DamageComponent class.
     */
    public ComponentItemStack damage(int amount) {
        // FIX: Directly call the static create method of the external DamageComponent class
        return addComponent(DamageComponent.create(amount));
    }

    /**
     * Auxiliary method for unbreakable.
     * Uses the external, static UnbreakableComponent class.
     */
    public ComponentItemStack unbreakable() {
        // FIX: Directly call the static create method of the external UnbreakableComponent class
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
     * Returns the full item stack string in the format: item_id[component1=value1,component2=value2].
     */
    @Override
    public String build() {
        if (components.isEmpty()) {
            return itemId.getResourceLocation();
        }

        StringJoiner componentJoiner = new StringJoiner(",", "[", "]");

        for (ItemComponentTag component : components.values()) {
            componentJoiner.add(component.buildComponentString());
        }

        return itemId.getResourceLocation() + componentJoiner.toString();
    }
}