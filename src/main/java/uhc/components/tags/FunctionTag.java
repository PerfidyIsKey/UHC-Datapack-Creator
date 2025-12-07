package uhc.components.tags;

import uhc.core.DatapackComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Added import

/**
 * Represents a Minecraft function tag file (e.g., tick.json or load.json).
 */
public class FunctionTag implements DatapackComponent {

    private final String name;
    private final List<String> functions = new ArrayList<>();

    public FunctionTag(String name) {
        this.name = name;
    }

    @Override
    public String getCategory() {
        // The path to the tag folder relative to the namespace
        return "tags/function";
    }

    @Override
    public String getPath() {
        // E.g., "load.json" relative to the "tags/function" folder
        return name + ".json";
    }

    // ... (addFunction and generateContent methods remain the same)

    public void addFunction(String functionId) {
        this.functions.add(functionId);
    }

    @Override
    public String generateContent() {
        // Collect all function IDs, wrap them in quotes, and join them with a comma and newline
        String values = functions.stream()
                .map(id -> "    \"" + id + "\"")
                .collect(Collectors.joining(",\n"));

        // Use a multi-line string (text block) for clean JSON formatting
        return String.format("""
{
  "values": [
%s
  ]
}
""", values);
    }
}