package uhc.core;

/**
 * Represents a single file within the Minecraft datapack structure
 * (e.g., a function, loot table, or recipe).
 */
public interface DatapackComponent {

    /**
     * The file path relative to the <namespace>/<content_type> folder.
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