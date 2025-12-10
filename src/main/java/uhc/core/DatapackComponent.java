package uhc.core;

/**
 * 📝 **Datapack Component Contract**
 * <p>
 * Defines the contract for any file component (function, tag, advancement, etc.)
 * that will be generated and placed within a Minecraft datapack structure.
 * </p>
 * Implementing classes are responsible for determining their file location
 * (category and path) and generating their final, ready-to-write content.
 */
public interface DatapackComponent {

    /**
     * Gets the main **resource category folder** of this component.
     * <p>
     * The category path is relative to the {@code data/<namespace>/} folder.
     * </p>
     * <ul>
     * <li>**Example:** For functions, this is {@code "function"}.</li>
     * <li>**Example:** For function tags, this is {@code "tags/function"}.</li>
     * </ul>
     * * @return The resource category path string (e.g., "function" or "tags/function").
     */
    String getCategory();

    /**
     * Gets the file name and its relative path **within its category folder**.
     * <p>
     * This method must return the full filename, including any organizing subdirectories
     * and the file extension. This combines to form the final disk path:
     * {@code data/<namespace>/<category>/<path>}.
     * </p>
     * * @return The file path (e.g., "init/load.mcfunction" or "custom_tag.json").
     */
    String getPath();

    /**
     * Generates the entire, final content of the file component.
     * <p>
     * This method is called by the {@code Generator} and must handle all necessary
     * serialization (e.g., converting objects to JSON or a sequence of commands)
     * and formatting required for the target file type.
     * </p>
     * * @return The complete file content as a single string, ready for writing to disk.
     */
    String generateContent();
}