package uhc.components.functions;

import uhc.core.DatapackComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Minecraft function file (.mcfunction).
 */
public class Function implements DatapackComponent {

    private final String path; // The path/name relative to the 'function' folder
    private final List<String> commands = new ArrayList<>(); // Changed to String list for simplicity

    public Function(String path) {
        this.path = path;
    }

    @Override
    public String getCategory() {
        return "function"; // The root folder for .mcfunction files
    }

    @Override
    public String getPath() {
        // E.g., "init/load.mcfunction" relative to the "function" folder
        return path + ".mcfunction";
    }

    // ... (addCommand and generateContent methods remain the same)
    public void addCommand(String command) {
        this.commands.add(command);
    }

    @Override
    public String generateContent() {
        return commands.stream()
                .collect(Collectors.joining("\n"));
    }
}