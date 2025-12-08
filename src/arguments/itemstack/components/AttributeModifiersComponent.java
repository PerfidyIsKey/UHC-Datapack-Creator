package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;
import arguments.itemstack.components.attributes.AttributeModifierEntry;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the 'attribute_modifiers' component. It now holds a list of
 * type-safe AttributeModifierEntry objects.
 * Format: attribute_modifiers=[{id:"...", ...},{id:"...", ...}]
 */
public class AttributeModifiersComponent implements ItemComponentTag {
    // 1. CHANGE: Use a List of AttributeModifierEntry instead of rawSnbtArray
    private final List<AttributeModifierEntry> modifiers;

    /**
     * @param modifiers The list of type-safe attribute modifier objects.
     */
    private AttributeModifiersComponent(List<AttributeModifierEntry> modifiers) {
        if (modifiers == null) {
            throw new IllegalArgumentException("Modifier list cannot be null.");
        }
        this.modifiers = modifiers;
    }

    // 2. CHANGE: Factory method accepts List
    public static AttributeModifiersComponent create(List<AttributeModifierEntry> modifiers) {
        return new AttributeModifiersComponent(modifiers);
    }

    @Override
    public String buildComponentString() {
        if (modifiers.isEmpty()) {
            return ""; // Or handle as desired if empty list is not allowed
        }

        // 3. CHANGE: Map the list to SNBT strings and join them into an array.
        String snbtArray = modifiers.stream()
                .map(AttributeModifierEntry::buildSnbt)
                .collect(Collectors.joining(",", "[", "]"));

        // Output format: attribute_modifiers=[{...},{...}]
        return "attribute_modifiers=" + snbtArray;
    }
}