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

    /** The standard file extension for Minecraft function files. */
    private static final String FILE_EXTENSION = ".mcfunction";

    /** The root category folder within a datapack namespace for functions. */
    private static final String CATEGORY_FOLDER = "function";

    // --- 🛠️ Fields ---

    /** * The internal path and name of the function relative to the category folder.
     * Example: {@code "init/load"}
     */
    private final String path;

    /** * The sequential list of {@link MinecraftCommand} objects that form the file's content.
     * Maintains the exact order in which commands were added.
     */
    private final List<MinecraftCommand> commands = new ArrayList<>();

    // --- 🏗️ Constructor ---

    /**
     * Constructs a new function component using a type-safe {@link FunctionPath}.
     * * @param functionPath The enum constant defining the relative path and filename.
     * @throws IllegalArgumentException if the provided functionPath is null.
     */
    public Function(FunctionPath functionPath) {
        if (functionPath == null) {
            throw new IllegalArgumentException("FunctionPath cannot be null. A valid destination is required.");
        }
        this.path = functionPath.getPath();
    }

    // --- 🛰️ Datapack Identity Methods ---

    /**
     * Retrieves the category identifier used for directory nesting.
     * @return The constant {@code "function"}.
     */
    @Override
    public String getCategory() {
        return CATEGORY_FOLDER;
    }

    /**
     * Retrieves the complete file path, including the file extension.
     * @return The full path (e.g., {@code "systems/timer.mcfunction"}).
     */
    @Override
    public String getPath() {
        return path + FILE_EXTENSION;
    }

    // --- ⚔️ Command Management ---

    /**
     * Appends a single command to the function's sequence.
     * * @param command The {@link MinecraftCommand} to add.
     * @throws IllegalArgumentException if the command reference is null.
     */
    public void addLine(MinecraftCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Cannot add a null command to function: " + this.path);
        }
        this.commands.add(command);
    }

    /**
     * Appends a collection of commands to the function sequence.
     * <p><b>Error Catching:</b> Ensures the collection itself is not null and
     * filters out any internal null elements to prevent downstream generation failures.</p>
     * * @param commands A collection of objects extending {@link MinecraftCommand}.
     * @throws NullPointerException if the input collection is null.
     */
    public void addAll(Collection<? extends MinecraftCommand> commands) {
        Objects.requireNonNull(commands, "The command collection for function '" + this.path + "' cannot be null.");

        // Filter nulls to ensure the internal List remains clean
        commands.stream()
                .filter(Objects::nonNull)
                .forEach(this.commands::add);
    }

    /**
     * Overloaded helper to add multiple commands using variable arguments.
     * * @param commands Varargs array of {@link MinecraftCommand} objects.
     */
    public void addAll(MinecraftCommand... commands) {
        if (commands != null) {
            this.addAll(List.of(commands));
        }
    }

    // --- ⚙️ Content Generation ---

    /**
     * Compiles all stored commands into a single string suitable for a {@code .mcfunction} file.
     * * @return A newline-separated string of generated commands.
     * @throws RuntimeException if a command fails its internal string generation.
     */
    @Override
    public String generateContent() {
        try {
            return commands.stream()
                    .map(MinecraftCommand::generate)
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            // Error Catching: Wrap generation errors with context about which function failed.
            throw new RuntimeException("Critical failure generating content for function: " + this.path, e);
        }
    }
}