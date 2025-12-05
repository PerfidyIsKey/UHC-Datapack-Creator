package arguments.itemstack.components.attributes;

import shared.attributes.AttributeTooltipDisplayType;

/**
 * Represents the optional 'display' tag within an AttributeModifierEntry.
 * Format: {type:"<display_type>"}
 */
public class AttributeDisplayTag {
    private final AttributeTooltipDisplayType type;

    private AttributeDisplayTag(AttributeTooltipDisplayType type) {
        if (type == null) {
            throw new IllegalArgumentException("Display type cannot be null.");
        }
        this.type = type;
    }

    public static AttributeDisplayTag create(AttributeTooltipDisplayType type) {
        return new AttributeDisplayTag(type);
    }

    /**
     * Builds the SNBT compound tag string for the display tag.
     * Example output: {type:"hidden"}
     */
    public String buildSnbt() {
        // Output format: {type:"hidden"}
        return "{type:\"" + type.getCommandString() + "\"}";
    }
}