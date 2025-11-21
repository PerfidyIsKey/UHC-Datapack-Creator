package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;

/**
 * Represents the 'lore' component. Takes a single plain string as input
 * and constructs the simple JSON array format: lore=["plain string"].
 */
public class LoreComponent implements ItemComponentTag {
    private final String plainLoreText;

    /**
     * @param plainLoreText The unformatted text for the lore line.
     */
    private LoreComponent(String plainLoreText) {
        if (plainLoreText == null || plainLoreText.trim().isEmpty()) {
            throw new IllegalArgumentException("Lore text cannot be null or empty.");
        }
        this.plainLoreText = plainLoreText;
    }

    public static LoreComponent create(String plainLoreText) {
        return new LoreComponent(plainLoreText);
    }

    @Override
    public String buildComponentString() {
        // To achieve: lore=["This holy weapon impales anything it touches"]
        // We escape internal quotes in the text, then wrap it in JSON quotes, then wrap the whole thing in array brackets.

        // Escape internal double quotes if they exist (though rare in lore)
        String escapedText = plainLoreText.replace("\"", "\\\"");

        // Format: "The text"
        String quotedText = "\"" + escapedText + "\"";

        // Format: ["The text"]
        String jsonArray = "[" + quotedText + "]";

        return "lore=" + jsonArray;
    }
}