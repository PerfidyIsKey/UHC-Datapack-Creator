package uhc.arguments.itemstack.components;

import uhc.arguments.itemstack.ItemComponentTag;

/**
 * Represents the 'minecraft:lore' component. This implementation takes a single plain string
 * and formats it as a single-element JSON array of quoted text, suitable for SNBT:
 * {@code lore=["Your lore line here"]}.
 */
public class LoreComponent implements ItemComponentTag {
    private final String plainLoreText;

    /**
     * Private constructor that enforces non-null and non-empty input.
     * @param plainLoreText The unformatted text for the single lore line.
     * @throws IllegalArgumentException if the input string is null or empty after trimming.
     */
    private LoreComponent(String plainLoreText) {
        if (plainLoreText == null || plainLoreText.trim().isEmpty()) {
            throw new IllegalArgumentException("Lore text cannot be null or empty.");
        }
        this.plainLoreText = plainLoreText;
    }

    /**
     * Static factory method to create the Lore component.
     * @param plainLoreText The unformatted text for the single lore line.
     * @return A new LoreComponent instance.
     */
    public static LoreComponent create(String plainLoreText) {
        return new LoreComponent(plainLoreText);
    }

    /**
     * Builds the SNBT representation of the component tag.
     * @return The formatted component string, e.g., {@code lore=["This holy weapon impales anything it touches"]}.
     */
    @Override
    public String buildComponentString() {
        // Goal format: lore=["The text line"]

        // 1. Escape internal double quotes if they exist in the plain text.
        // This prevents the string from being prematurely terminated in the final JSON output.
        String escapedText = plainLoreText.replace("\"", "\\\"");

        // 2. Wrap the text in double quotes to make it a valid JSON string element.
        String quotedText = "\"" + escapedText + "\"";

        // 3. Wrap the quoted text in array brackets to form the JSON array structure.
        String jsonArray = "[" + quotedText + "]";

        return "lore=" + jsonArray;
    }
}