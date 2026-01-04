package uhc.components.functions;

import uhc.command.MinecraftCommand;
import uhc.core.DatapackComponent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 📜 **Minecraft Function Component**
 * <p>
 * Represents a single Minecraft function file ({@code .mcfunction}).
 * This component is responsible for storing a sequential list of commands and
 * generating the final text content of the file.
 * </p>
 * <p><b>File Structure:</b> {@code data/<namespace>/function/path/to/name.mcfunction}</p>
 */
public class Function implements DatapackComponent {

    // --- 📂 Constants ---

    /** * The standard file extension for Minecraft function files.
     */
    private static final String FILE_EXTENSION = ".mcfunction";

    /** * The root category folder within a datapack namespace for functions.
     */
    private static final String CATEGORY_FOLDER = "function";

    // --- 🛠️ Fields ---

    /** * The internal path and name of the function relative to the category folder.
     * <p>Example: {@code "init/load"}</p>
     */
    private final String path;

    /** * The sequential list of {@link MinecraftCommand} objects that form the file's content.
     * Maintains the exact order in which commands were added to ensure execution logic remains intact.
     */
    private final List<MinecraftCommand> commands = new ArrayList<>();

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new function component using a type-safe {@link FunctionPath}.
     * * @param functionPath The interface constant defining the relative path and filename.
     * @throws NullPointerException if the provided functionPath is null.
     * @throws IllegalStateException if the path resolution returns an invalid string.
     */
    public Function(FunctionPath functionPath) {
        // Validation: Catching nulls early to avoid corrupted file paths
        Objects.requireNonNull(functionPath, "Function Initialization Error: FunctionPath cannot be null.");

        try {
            this.path = functionPath.getPath();

            if (this.path == null || this.path.isBlank()) {
                throw new IllegalStateException("Function Initialization Error: The resolved path for " + functionPath + " is null or empty.");
            }
        } catch (Exception e) {
            // Rethrowing as IllegalStateException to provide a clear error message without fallbacks
            throw new IllegalStateException("Function Initialization Error: Failed to resolve path from " + functionPath.getClass().getSimpleName(), e);
        }
    }

    // --- 🛰️ Datapack Identity Methods ---

    /**
     * Retrieves the category identifier used for directory nesting within the datapack.
     * * @return The constant {@code "function"}.
     */
    @Override
    public String getCategory() {
        return CATEGORY_FOLDER;
    }

    /**
     * Retrieves the complete file path, including the file extension.
     * * @return The full path (e.g., {@code "systems/timer.mcfunction"}).
     * @throws IllegalStateException if the path field was not properly initialized during construction.
     */
    @Override
    public String getPath() {
        if (this.path == null) {
            throw new IllegalStateException("Function Path Error: Internal path was never properly initialized.");
        }
        return this.path + FILE_EXTENSION;
    }

    // --- ⚔️ Command Management ---

    /**
     * Appends a single command to the function's sequence.
     * * @param command The {@link MinecraftCommand} to add; must not be null.
     * @throws NullPointerException if the command reference is null.
     */
    public void addLine(MinecraftCommand command) {
        if (command == null) {
            throw new NullPointerException("Function Edit Error: Cannot add a null command to function at path: " + this.path);
        }
        this.commands.add(command);
    }

    /**
     * Appends a collection of commands to the function sequence in the order they are provided.
     * * @param commands A collection of objects extending {@link MinecraftCommand}.
     * @throws NullPointerException if the input collection is null or contains any null elements.
     */
    public void addAll(Collection<? extends MinecraftCommand> commands) {
        Objects.requireNonNull(commands, "Function Edit Error: The command collection for function '" + this.path + "' cannot be null.");

        for (MinecraftCommand cmd : commands) {
            if (cmd == null) {
                // Strict validation: Reject the entire operation if any element is null to maintain integrity
                throw new NullPointerException("Function Edit Error: A null command was detected in the batch addition for function: " + this.path);
            }
            this.commands.add(cmd);
        }
    }

    /**
     * Overloaded helper to add multiple commands using variable arguments.
     * * @param commands Varargs array of {@link MinecraftCommand} objects.
     * @throws NullPointerException if the array itself is null or any argument in the array is null.
     */
    public void addAll(MinecraftCommand... commands) {
        if (commands == null) {
            throw new NullPointerException("Function Edit Error: The command varargs array for function '" + this.path + "' is null.");
        }
        // Utilizing List.of to trigger immediate NullPointerException if any element is null
        this.addAll(List.of(commands));
    }

    // --- ⚙️ Content Generation ---

    /**
     * Compiles all stored {@link MinecraftCommand} objects into a single string formatted for a {@code .mcfunction} file.
     * * @return A newline-separated string containing the raw command text.
     * @throws RuntimeException if the function is empty or if any command fails internal generation.
     */
    @Override
    public String generateContent() {
        // Strict Policy: No empty function files allowed in the assembly
        if (this.commands.isEmpty()) {
            throw new RuntimeException("Function Generation Error: Attempted to generate an empty file for [" + this.path + "]. Functions must contain at least one command.");
        }

        try {
            return this.commands.stream()
                    .map(cmd -> {
                        String generated = cmd.generate();
                        if (generated == null || generated.isBlank()) {
                            throw new IllegalStateException("Command Generation Error: Command in '" + this.path + "' produced a null or blank string.");
                        }
                        return generated;
                    })
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            // Context-heavy error message to pinpoint exact failure point in the assembly pipeline
            throw new RuntimeException("CRITICAL GENERATION FAILURE: Error compiling commands for function: " + this.path + ". Reason: " + e.getMessage(), e);
        }
    }
}