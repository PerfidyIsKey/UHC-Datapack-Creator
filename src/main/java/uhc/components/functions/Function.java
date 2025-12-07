package uhc.components.functions;

import uhc.core.DatapackComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Minecraft function file (.mcfunction).
 */
public class Function implements DatapackComponent {

    // The relative path within the 'functions' folder (e.g., "startup")
    private final String path;
    private final List<FunctionCommand> commands = new ArrayList<>();

    /**
     * @param path The path/name of the function (e.g., "init/on_load").
     * The file extension is added automatically.
     */
    public Function(String path) {
        this.path = path;
    }

    /**
     * Adds a command to the function.
     * @param command The raw command string (e.g., "say Hello World!").
     */
    public void addCommand(String command) {
        this.commands.add(new FunctionCommand(command));
    }

    @Override
    public String getPath() {
        // Functions reside in <namespace>/functions/<path>.mcfunction
        // We only return the relative part within the functions folder.
        return "functions/" + path + ".mcfunction";
    }

    @Override
    public String generateContent() {
        // Joins all commands with a newline character for the file content.
        return commands.stream()
                .map(FunctionCommand::toString)
                .collect(Collectors.joining("\n"));
    }
}