package uhc.components.tags;

import uhc.core.DatapackComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Minecraft function tag file (e.g., tick.json or load.json).
 * <p>
 * File location: data/&lt;namespace&gt;/tags/function/&lt;name&gt;.json
 */
public class FunctionTag implements DatapackComponent {

    private final String name;
    private final List<String> functions = new ArrayList<>();

    /**
     * Creates a new Function Tag.
     * @param name The name of the tag (e.g., "tick" or "load").
     * Do not include the .json extension.
     */
    public FunctionTag(String name) {
        // specific safety check: remove .json if the user accidentally added it
        if (name.endsWith(".json")) {
            this.name = name.substring(0, name.length() - 5);
        } else {
            this.name = name;
        }
    }

    /**
     * Returns the category folder for this component.
     * Note: Minecraft 1.21+ uses the singular 'function' folder inside 'tags'.
     */
    @Override
    public String getCategory() {
        return "tags/function";
    }

    @Override
    public String getPath() {
        return name + ".json";
    }

    /**
     * Adds a function to this tag.
     * * @param functionId The full resource location of the function.
     * Format: "namespace:path/to/function"
     * Example: "uhc_core_pack:init/load"
     */
    public void addFunction(String functionId) {
        this.functions.add(functionId);
    }

    @Override
    public String generateContent() {
        // If the list is empty, return a simple empty JSON object to avoid formatting errors
        if (functions.isEmpty()) {
            return "{\n  \"values\": []\n}";
        }

        // Collect all function IDs, wrap them in quotes, and join them with a comma and newline
        // Indentation (4 spaces) is added here to align with the text block below
        String values = functions.stream()
                .map(id -> "    \"" + id + "\"")
                .collect(Collectors.joining(",\n"));

        // Use a multi-line text block for clean JSON structure
        // %s will be replaced by the list of formatted functions
        return String.format("""
                {
                  "values": [
                %s
                  ]
                }
                """, values);
    }
}