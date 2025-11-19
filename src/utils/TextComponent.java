package utils;

/**
 * Utility class for creating Minecraft Text Component strings.
 */
public class TextComponent {

    /**
     * Creates a simple, unformatted Minecraft text component string.
     * @param text The plain string to display.
     * @return The plain string itself. The TagConverter will wrap this in quotes for SNBT.
     */
    public static String simple(String text) {
        // Return the plain string. The TagConverter will quote this value for SNBT.
        return text;
    }

    /**
     * Creates a complex component with a click event (returns raw JSON string).
     * @param text The display text.
     * @param action The click event action.
     * @param command The value for the action.
     * @return A raw JSON string.
     */
    public static String withClickCommand(String text, String action, String command) {
        String escapedText = text.replace("\"", "\\\"");
        String escapedCommand = command.replace("\"", "\\\"");

        return String.format(
                "{\"text\":\"%s\",\"click_event\":{\"action\":\"%s\",\"command\":\"%s\"}}",
                escapedText, action, escapedCommand
        );
    }
}