package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;

/**
 * Represents the 'custom_name' component, which holds a raw JSON text component string.
 * Format: custom_name=[{"text":"The Impaler"}]
 */
public class CustomNameComponent implements ItemComponentTag {
    private final String rawJsonText;

    /**
     * @param rawJsonText The full JSON array/object string representing the text component.
     */
    private CustomNameComponent(String rawJsonText) {
        if (rawJsonText == null || rawJsonText.trim().isEmpty()) {
            throw new IllegalArgumentException("Raw JSON text for custom_name cannot be null or empty.");
        }
        this.rawJsonText = rawJsonText;
    }

    public static CustomNameComponent create(String rawJsonText) {
        return new CustomNameComponent(rawJsonText);
    }

    @Override
    public String buildComponentString() {
        // Output format: custom_name=...
        return "custom_name=" + rawJsonText;
    }
}