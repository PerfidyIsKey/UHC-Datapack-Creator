package uhc.core;

/**
 * Represents a single file within the Minecraft datapack structure.
 */
public interface DatapackComponent {

    /**
     * Gets the main category path of this component within the namespace.
     * E.g., "function", "loot_table", or "tags/function".
     * @return The category path.
     */
    String getCategory();

    /**
     * The file path relative to the <namespace>/<category> folder.
     * E.g., "my_function.mcfunction"
     * @return The file name, including the extension.
     */
    String getPath();

    /**
     * Generates the entire content of the file.
     * @return The file content as a single string.
     */
    String generateContent();
}