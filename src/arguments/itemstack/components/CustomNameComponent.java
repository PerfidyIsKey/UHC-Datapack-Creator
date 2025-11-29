package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;

/**
 * Represents the 'minecraft:custom_name' component.
 * This component's value is expected to be a raw JSON Text Component string,
 * which can be a simple quoted string (e.g., "My Name") or a complex JSON array/object
 * (e.g., [{"text":"Hi", "color":"gold"}]).
 * * Format: custom_name="My Name" OR custom_name=[{"text":"My Name"}]
 */
public class CustomNameComponent implements ItemComponentTag {
    private final String rawJsonText;

    /**
     * Private constructor that processes the input string to ensure it is correctly
     * quoted for simple text or left alone if it's already complex JSON.
     * * @param rawJsonText The full JSON array/object string (e.g., "[{...}]")
     * or a simple, unquoted text string (e.g., "My Name").
     * @throws IllegalArgumentException if the input string is null or empty after trimming.
     */
    private CustomNameComponent(String rawJsonText) {
        if (rawJsonText == null) {
            throw new IllegalArgumentException("Raw JSON text for custom_name cannot be null.");
        }

        String trimmedText = rawJsonText.trim();
        if (trimmedText.isEmpty()) {
            throw new IllegalArgumentException("Raw JSON text for custom_name cannot be empty.");
        }

        // --- Logic to ensure correct SNBT quoting ---

        // 1. Check if the input is already a complex JSON object or array.
        if (trimmedText.startsWith("{") || trimmedText.startsWith("[")) {
            // Complex JSON (e.g., {"text":"..."}, [{"text":"..."}]). Use as is.
            this.rawJsonText = rawJsonText;
        }
        // 2. Check if the input is already a simple quoted string.
        else if (trimmedText.startsWith("\"") && trimmedText.endsWith("\"")) {
            // Simple quoted string (e.g., "The Name"). Use as is.
            this.rawJsonText = rawJsonText;
        }
        // 3. Otherwise, it's a plain, unquoted string from TextComponent.simple().
        else {
            // Wrap the plain string in quotes for valid SNBT output: custom_name="Value"
            this.rawJsonText = "\"" + rawJsonText + "\"";
        }
    }

    /**
     * Static factory method to create the component.
     * @param rawJsonText The JSON text string.
     * @return A new CustomNameComponent instance.
     */
    public static CustomNameComponent create(String rawJsonText) {
        return new CustomNameComponent(rawJsonText);
    }

    /**
     * Builds the SNBT representation of the component tag.
     * @return The formatted component string, e.g., 'custom_name="Developer Mode"'.
     */
    @Override
    public String buildComponentString() {
        // The rawJsonText field now holds the final, correctly quoted/formatted value.
        return "custom_name=" + rawJsonText;
    }
}