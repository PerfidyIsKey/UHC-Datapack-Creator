package uhc.components.functions;

/**
 * 🔗 **Type-Safe Minecraft Function Paths**
 * <p>
 * This enum acts as a centralized registry for all {@code .mcfunction} files within the datapack.
 * By defining functions here, we ensure:
 * <ul>
 * <li><b>Type Safety:</b> No more raw strings for function references.</li>
 * <li><b>Refactoring Safety:</b> Renaming an enum updates all references automatically.</li>
 * <li><b>Format Compliance:</b> Paths are automatically normalized to lowercase and use forward slashes.</li>
 * </ul>
 */
public enum FunctionPath {

    // --- Examples of Function Paths ---

    /**
     * Represents: {@code /function/reset.mcfunction}
     * <br>Located in the root of the namespace's function folder.
     */
    RESET,

    /**
     * Represents: {@code /function/init/load.mcfunction}
     * <br>Located in the {@code init} sub-folder.
     */
    LOAD("init"),

    /**
     * Represents: {@code /function/loop/main/tick.mcfunction}
     * <br>Located in the {@code loop/main} sub-folder.
     */
    TICK("loop/main"),

    // --- End Examples ---

    ; // Semicolon required to separate enum constants from fields/methods

    // The Minecraft-compliant path prefix (e.g., "init/" or "loop/main/")
    private final String pathPrefix;

    /**
     * Private constructor for functions located in the root 'function' folder.
     * <p>
     * The path prefix defaults to an empty string, meaning the function name
     * becomes the file name directly (e.g., {@code RESET} -> {@code reset}).
     */
    FunctionPath() {
        this.pathPrefix = "";
    }

    /**
     * Private constructor for functions located within a specific folder structure.
     * <p>
     * This constructor handles path normalization:
     * <ul>
     * <li>Converts backslashes (Windows style) to forward slashes (Minecraft style).</li>
     * <li>Ensures the path ends with a trailing slash to act as a proper prefix.</li>
     * </ul>
     *
     * @param folderLocation The relative folder path (e.g., "init" or "loop/main").
     * @throws IllegalArgumentException If the provided folder location is null or empty.
     */
    FunctionPath(String folderLocation) {
        if (folderLocation == null || folderLocation.isBlank()) {
            throw new IllegalArgumentException("Folder location cannot be null or empty when using the parameterized constructor.");
        }

        // Normalize slashes: Windows paths use '\', but Minecraft internal paths MUST use '/'
        String normalizedPath = folderLocation.trim().replace('\\', '/');

        // Ensure the path ends with a forward slash so it concatenates correctly with the filename
        if (!normalizedPath.endsWith("/")) {
            normalizedPath += "/";
        }

        this.pathPrefix = normalizedPath;
    }

    /**
     * Generates the final, complete Minecraft function path string.
     * <p>
     * The logic ensures strict compliance with Minecraft resource location rules:
     * <ol>
     * <li><b>Prefix:</b> Uses the normalized folder path (e.g., "init/").</li>
     * <li><b>Name:</b> Converts the enum constant (e.g., "LOAD") to <b>lowercase</b> ("load").</li>
     * </ol>
     * Example: {@code TICK("loop/main")} becomes {@code "loop/main/tick"}.
     *
     * @return The complete function path string, excluding the {@code .mcfunction} extension.
     */
    public String getPath() {
        // CRITICAL FIX: Minecraft resource locations (files/folders) must be lowercase.
        // We convert the uppercase enum constant (e.g., LOAD) to lowercase (e.g., load).
        String functionName = this.name().toLowerCase();

        // Concatenate the path prefix (e.g., "init/") with the function name (e.g., "load")
        return pathPrefix + functionName;
    }

    /**
     * Returns the string representation of the path.
     * Delegates to {@link #getPath()} to ensure consistent formatting everywhere this object is printed.
     *
     * @return The complete function path string.
     */
    @Override
    public String toString() {
        return getPath();
    }
}