package uhc.core;

/**
 * Defines the contract for any file component (function, tag, advancement, etc.)
 * that will be generated and placed within a Minecraft datapack structure.
 * <p>
 * Implementing classes are responsible for determining their file location and generating their content.
 */
public interface DatapackComponent {

    /**
     * Gets the main category folder of this component, relative to the {@code data/<namespace>/} folder.
     * <p>
     * **Namespace Context:**
     * <ul>
     * <li>If the component is a **function** or **loot table**, the category will be placed in the *custom* namespace.</li>
     * <li>If the component is a **function tag** (load/tick) or a **world generation** file, the category will typically be placed in the *minecraft* namespace.</li>
     * </ul>
     * <p>
     * Examples:
     * <ul>
     * <li>Functions: "function"</li>
     * <li>Function Tags: "tags/function"</li>
     * <li>World Generation: "worldgen/biome"</li>
     * </ul>
     * * @return The resource category path.
     */
    String getCategory();

    /**
     * Gets the path and file name of the component, relative to the {@code <namespace>/<category>} folder.
     * <p>
     * This path often includes subdirectories for organization.
     * * @return The file path (e.g., "init/load.mcfunction").
     */
    String getPath();

    /**
     * Generates the entire, final content of the file.
     * <p>
     * This method is called by the {@code Generator} and should handle all necessary
     * formatting and data serialization (e.g., JSON or list of commands).
     * * @return The complete file content as a single string.
     */
    String generateContent();
}