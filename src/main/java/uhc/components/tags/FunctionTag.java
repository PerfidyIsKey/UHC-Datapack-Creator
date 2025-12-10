package uhc.components.tags;

import uhc.components.functions.FunctionPath;
import uhc.core.DatapackComponent;
import uhc.core.DatapackConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 🏷️ **Minecraft Function Tag Component**
 * <p>
 * Represents a Minecraft function tag file (e.g., {@code tick.json} or {@code load.json}).
 * These tags are essential components used by the game engine to execute lists of functions
 * automatically (like {@code minecraft:load}) or manually via the {@code function} command.
 * </p>
 * File location structure: {@code data/<namespace>/tags/function/<name>.json}
 */
public class FunctionTag implements DatapackComponent {

    // The type has been changed to the type-safe enum, which stores the tag name and target namespace.
    private final FunctionTagPath tagPath;

    // The list of functions (represented by type-safe enums) that this tag will execute.
    private final List<FunctionPath> functions = new ArrayList<>();

    /**
     * Creates a new Function Tag instance.
     * * @param tagPath The {@code FunctionTagPath} enum constant defining the name of the tag (e.g., LOAD, TICK).
     * @throws IllegalArgumentException if the provided {@code FunctionTagPath} is null.
     */
    public FunctionTag(FunctionTagPath tagPath) {
        // --- Input Validation ---
        if (tagPath == null) {
            throw new IllegalArgumentException("Function tag path enum cannot be null.");
        }

        // Assign the enum directly. We rely on the enum's internal logic (getPath())
        // to handle lowercase and formatting. No string manipulation needed here.
        this.tagPath = tagPath;
    }

    /**
     * Returns the category folder path relative to the namespace root.
     * Note: Minecraft 1.21+ uses the singular 'function' folder inside 'tags'.
     * * @return The category path string: {@code "tags/function"}.
     */
    @Override
    public String getCategory() {
        return "tags/function";
    }

    /**
     * Returns the complete file name for this component.
     * The file name is derived from the enum's path.
     * * @return The file name including the {@code .json} extension (e.g., {@code "load.json"}).
     */
    @Override
    public String getPath() {
        // We use the enum's getPath() method which returns the lowercase name.
        return tagPath.getPath() + ".json";
    }

    /**
     * Adds a function, specified by its type-safe enum, to this tag's list of functions.
     * * @param functionPath The {@code FunctionPath} enum constant defining the target function.
     */
    public void addFunction(FunctionPath functionPath) {
        // Error Catching: Prevent adding null entries to the list.
        if (functionPath != null) {
            this.functions.add(functionPath);
        }
    }

    /**
     * Generates the JSON content required for the tag file.
     * <p>
     * **Wiring Logic:** This method constructs the full, compliant Minecraft resource IDs
     * (Format: {@code namespace:path/to/function}) by combining the static namespace
     * (from {@code DatapackConfig}) with the {@code FunctionPath}'s fully qualified path.
     * </p>
     * @return The formatted JSON string content.
     */
    @Override
    public String generateContent() {
        // Handle the edge case of an empty tag list to ensure the output is still valid JSON
        if (functions.isEmpty()) {
            return "{\n  \"values\": []\n}";
        }

        // Stream and map each FunctionPath to its required JSON string format
        String values = functions.stream()
                .map(id -> {
                    // 1. Construct the full resource ID (e.g., "uhc_core_pack:init/load")
                    //    id.getPath() ensures the path is lowercase and includes the folder prefix.
                    String fullResourceID = DatapackConfig.CUSTOM_NAMESPACE + ":" + id.getPath();

                    // 2. Format as a quoted JSON array entry with indentation.
                    return "    \"" + fullResourceID + "\"";
                })
                .collect(Collectors.joining(",\n"));

        // Use a multi-line text block for clean JSON structure
        return String.format("""
                {
                  "values": [
                %s
                  ]
                }
                """, values);
    }
}