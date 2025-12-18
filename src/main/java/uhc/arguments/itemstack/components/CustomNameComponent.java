package uhc.arguments.itemstack.components;

import uhc.arguments.itemstack.ItemComponentTag;
import uhc.text.TextComponent;

/**
 * Represents the 'minecraft:custom_name' component for item stacks.
 * <p>
 * This class ensures that a {@link TextComponent} (whether a single object
 * or an array) is correctly formatted for Minecraft's Item SNBT.
 * </p>
 */
public class CustomNameComponent implements ItemComponentTag {
    private final TextComponent textComponent;

    private CustomNameComponent(TextComponent textComponent) {
        if (textComponent == null) {
            throw new IllegalArgumentException("TextComponent for custom_name cannot be null.");
        }
        this.textComponent = textComponent;
    }

    /**
     * Factory method using the type-safe TextComponent object.
     * Supports single components, selectors, and composite arrays.
     */
    public static CustomNameComponent create(TextComponent textComponent) {
        return new CustomNameComponent(textComponent);
    }

    /**
     * Convenience factory for plain text.
     */
    public static CustomNameComponent create(String plainText) {
        return new CustomNameComponent(TextComponent.simple(plainText));
    }

    /**
     * Builds the SNBT representation.
     * <p>
     * Uses single quotes around the build result to prevent JSON double-quote conflicts.
     * Result: {@code custom_name='{"text":"...","color":"..."}'}
     * OR {@code custom_name='[{"text":"..."},{...}]'}
     * </p>
     */
    @Override
    public String buildComponentString() {
        return "custom_name='" + textComponent.build() + "'";
    }
}