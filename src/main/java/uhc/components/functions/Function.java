package uhc.components.functions;

import uhc.command.MinecraftCommand;
import uhc.core.DatapackComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 📜 **Minecraft Function Component**
 * * Represents a single Minecraft function file ({@code .mcfunction}).
 * It stores a sequential list of {@code MinecraftCommand} objects, which
 * includes both executable commands and comments, and is responsible for
 * generating the final file content.
 */
public class Function implements DatapackComponent {

    // The path/name relative to the 'function' folder (e.g., "init/load").
    private final String path;

    // The sequential list of commands and command-like objects (comments) that form the function's content.
    private final List<MinecraftCommand> commands = new ArrayList<>();

    /**
     * Constructs a new function component.
     * * @param path The path and name of the function, relative to the {@code function} folder
     * (e.g., "init/load" or "loop/tick").
     * @throws IllegalArgumentException if the provided path is null or blank, which would result in an invalid file name.
     */
    public Function(String path) {
        // Error Catching: Validate that the function path is a valid identifier fragment.
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Function path cannot be null or empty.");
        }
        this.path = path;
    }

    /**
     * @return The constant category folder name for functions: {@code "function"}.
     */
    @Override
    public String getCategory() {
        return "function";
    }

    /**
     * @return The complete file path relative to the category folder, including the required {@code .mcfunction} extension.
     */
    @Override
    public String getPath() {
        return path + ".mcfunction";
    }

    /**
     * Adds a new line (either an executable command or a documentation comment) to the function's sequence.
     * * @param command The {@code MinecraftCommand} object to be added.
     * @throws IllegalArgumentException if the provided command object is null.
     */
    public void addLine(MinecraftCommand command) {
        // Error Catching: Ensure we don't accidentally store a null reference, which would cause an NPE later.
        if (command == null) {
            throw new IllegalArgumentException("Cannot add a null command (or function line) to the function.");
        }
        this.commands.add(command);
    }

    /**
     * Generates the final content of the {@code .mcfunction} file.
     * It calls the {@code generate()} method on every stored {@code MinecraftCommand} object
     * and joins the results with a newline character, ensuring the correct file format.
     * * @return A string containing all generated commands and comments, one per line.
     */
    @Override
    public String generateContent() {
        // Map the list of objects to their generated string content, then join them with newlines.
        return commands.stream()
                .map(MinecraftCommand::generate)
                .collect(Collectors.joining("\n"));
    }
}