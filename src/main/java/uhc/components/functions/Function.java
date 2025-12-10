package uhc.components.functions;

import uhc.command.MinecraftCommand;
import uhc.core.DatapackComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 📜 **Minecraft Function Component**
 * <p>
 * Represents a single Minecraft function file ({@code .mcfunction}).
 * This component is responsible for storing a sequential list of commands and
 * generating the final text content of the file.
 * </p>
 * File location structure: {@code data/<namespace>/function/path/to/name.mcfunction}
 */
public class Function implements DatapackComponent {

    private static final String FILE_EXTENSION = ".mcfunction";
    private static final String CATEGORY_FOLDER = "function";

    // The path/name relative to the 'function' folder (e.g., "init/load"), derived from FunctionPath.
    private final String path;

    // The sequential list of commands and command-like objects (comments) that form the function's content.
    private final List<MinecraftCommand> commands = new ArrayList<>();

    /**
     * Constructs a new function component using a type-safe {@code FunctionPath} enum.
     * * @param functionPath The enum constant defining the path and name of the function.
     * @throws IllegalArgumentException if the provided {@code FunctionPath} object is null.
     */
    public Function(FunctionPath functionPath) {
        // --- Input Validation ---
        if (functionPath == null) {
            throw new IllegalArgumentException("The FunctionPath enum constant cannot be null when constructing a Function component.");
        }
        // Functionality maintained: extract the lower-cased, normalized path.
        this.path = functionPath.getPath();
    }

    /**
     * Returns the category folder name for functions.
     * * @return The constant category folder name: {@code "function"}.
     */
    @Override
    public String getCategory() {
        return CATEGORY_FOLDER;
    }

    /**
     * Returns the complete file path relative to the category folder.
     * * @return The complete path including the required {@code .mcfunction} extension (e.g., {@code "init/load.mcfunction"}).
     */
    @Override
    public String getPath() {
        return path + FILE_EXTENSION;
    }

    /**
     * Adds a new line (either an executable command or a documentation comment) to the function's sequence.
     * * @param command The {@code MinecraftCommand} object to be added.
     * @throws IllegalArgumentException if the provided command object is null.
     */
    public void addLine(MinecraftCommand command) {
        // --- Error Catching ---
        if (command == null) {
            // Refined message: clarifies that the command object itself is invalid.
            throw new IllegalArgumentException("Cannot add a null reference to the command sequence of function: " + this.path);
        }
        this.commands.add(command);
    }

    /**
     * Generates the final content of the {@code .mcfunction} file.
     * <p>
     * This method iterates through all stored commands, calls their {@code generate()}
     * method to retrieve their string representation, and joins them using the newline
     * character (required file format).
     * </p>
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