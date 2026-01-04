package uhc.components.tags;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uhc.components.functions.FunctionPath;
import uhc.core.DatapackComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 🏷️ **Minecraft Function Tag Component**
 * <p>
 * This class represents a Minecraft function tag JSON file. These tags allow the game
 * to group multiple functions under a single alias (e.g., {@code #minecraft:tick})
 * for automatic execution or organizational purposes.
 * </p>
 * <p><b>File Structure:</b> {@code data/<namespace>/tags/function/<name>.json}</p>
 */
public class FunctionTag implements DatapackComponent {

    // --- 📂 Constants ---

    /** * The relative directory path where function tags are stored within a namespace.
     * Minecraft 1.21+ uses the singular 'function' folder inside 'tags'.
     */
    private static final String CATEGORY_FOLDER = "tags/function";

    /** * The standard file extension for Minecraft tag definitions.
     */
    private static final String FILE_EXTENSION = ".json";

    /** * Shared Jackson {@link ObjectMapper} used to construct and serialize the JSON tree.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // --- 🛠️ Fields ---

    /** * The type-safe path identity of the tag (e.g., {@code LOAD} or {@code TICK}).
     * This defines the final filename and the tag's purpose.
     */
    private final FunctionTagPath tagPath;

    /** * The ordered list of {@link FunctionPath} references that this tag will execute.
     * Maintains the sequence in which functions are called by the game engine.
     */
    private final List<FunctionPath> functions = new ArrayList<>();

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new Function Tag instance.
     * @param tagPath The {@link FunctionTagPath} defining the identity of this tag; must not be null.
     * @throws NullPointerException if the tagPath argument is null.
     */
    public FunctionTag(FunctionTagPath tagPath) {
        this.tagPath = Objects.requireNonNull(tagPath, "Tag Initialization Error: The FunctionTagPath cannot be null.");
    }

    // --- 🛰️ Datapack Identity Methods ---

    /**
     * Retrieves the category folder used for directory organization within the datapack.
     * @return The constant {@code "tags/function"}.
     */
    @Override
    public String getCategory() {
        return CATEGORY_FOLDER;
    }

    /**
     * Constructs the relative file path for this tag, including the file extension.
     * @return The formatted filename (e.g., {@code "tick.json"}).
     * @throws IllegalStateException if the internal tagPath produces a null or blank name.
     */
    @Override
    public String getPath() {
        String name = this.tagPath.getPath();
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Tag Path Error: The FunctionTagPath returned a null or blank filename for: " + this.tagPath);
        }
        return name + FILE_EXTENSION;
    }

    // --- ⚔️ Management Methods ---

    /**
     * Appends a single function reference to this tag's values list.
     * @param functionPath The {@link FunctionPath} to be included; must not be null.
     * @throws NullPointerException if the functionPath is null.
     */
    public void addFunction(FunctionPath functionPath) {
        Objects.requireNonNull(functionPath, "Tag Edit Error: Cannot add a null FunctionPath to tag: " + this.tagPath.getPath());
        this.functions.add(functionPath);
    }

    /**
     * Appends a collection of function references to this tag's values list.
     * @param functionPaths The list of functions to add; must not be null and must not contain null elements.
     * @throws NullPointerException if the list is null or if any specific element in the list is null.
     */
    public void addAll(List<FunctionPath> functionPaths) {
        Objects.requireNonNull(functionPaths, "Tag Edit Error: The provided function list for tag '" + this.tagPath.getPath() + "' is null.");
        for (FunctionPath fp : functionPaths) {
            // Re-using addFunction to ensure individual element validation
            this.addFunction(fp);
        }
    }

    // --- ⚙️ Content Generation (Jackson) ---

    /**
     * Generates the compliant JSON content for the Minecraft tag file.
     * <p>Uses the Jackson tree model to build an object with a {@code "values"} array
     * containing the fully qualified identifiers of the stored functions.</p>
     * @return A pretty-printed JSON string containing the function values.
     * @throws RuntimeException if JSON construction fails or if a function produces a null identifier.
     */
    @Override
    public String generateContent() {
        try {
            // Initialize the root JSON object: {}
            ObjectNode root = MAPPER.createObjectNode();

            // Create the "values" array: "values": []
            ArrayNode valuesArray = root.putArray("values");

            // Populating the array with identifiers
            for (FunctionPath fp : this.functions) {
                String identifier = fp.getIdentifier();

                // Strict validation: No null identifiers allowed in the output
                if (identifier == null || identifier.isBlank()) {
                    throw new IllegalStateException("Tag Content Error: FunctionPath in tag '" + this.tagPath.getPath() + "' produced a null identifier.");
                }

                valuesArray.add(identifier);
            }

            // Return the serialized JSON with indentation for readability
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(root);

        } catch (Exception e) {
            // Clear Error Message: Wrap the exception with the specific tag context
            throw new RuntimeException("CRITICAL GENERATION FAILURE: Error building JSON for function tag [" + this.tagPath.getPath() + "]. Reason: " + e.getMessage(), e);
        }
    }
}