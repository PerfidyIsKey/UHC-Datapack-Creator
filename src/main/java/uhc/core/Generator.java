package uhc.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * 🗃️ **Datapack File Generator and Synchronizer**
 * * This class orchestrates the physical creation, updating, and cleanup of the
 * datapack files on the disk. It performs a comparison (diff) between the
 * current {@code Datapack} object structure and the existing file system to
 * ensure the output directory {@code uhc_datapack} is an exact mirror of the code structure.
 */
public class Generator {

    private static final Logger LOGGER = Logger.getLogger(Generator.class.getName());
    private static final String BINARY_IMAGE_NAME = "pack.png";

    /**
     * Executes the comprehensive datapack generation and synchronization process.
     * * The high-level process steps are:
     * 1. Collect all intended file paths and content from the Datapack object.
     * 2. Synchronize static resources (like {@code pack.png}).
     * 3. Delete obsolete files from previous runs (synchronization cleanup).
     * 4. Create or update all current files based on content comparison.
     * * @param datapack The complete object representation of the datapack, containing all namespaces and components.
     * @param outputDirectory The physical root path (e.g., 'Server/world/datapacks') where the pack folder should be created.
     * @throws IOException If a critical file system operation (like directory creation or file writing) fails.
     * @throws IllegalArgumentException if the provided {@code datapack} or {@code outputDirectory} is null.
     */
    public void generate(Datapack datapack, String outputDirectory) throws IOException {

        // Error Catching: Ensure inputs are valid before starting file operations.
        if (datapack == null) throw new IllegalArgumentException("Datapack object cannot be null.");
        if (outputDirectory == null || outputDirectory.isBlank()) throw new IllegalArgumentException("Output directory path cannot be null or blank.");

        // Construct the full, absolute path to the datapack folder (e.g., .../datapacks/uhc_datapack)
        Path rootPath = Paths.get(outputDirectory, DatapackConfig.DATAPACK_FOLDER_NAME);
        File rootDir = rootPath.toFile();

        // 1. Collect all files intended for writing (Maps the final file path to its string content)
        Map<Path, String> intendedFiles = collectIntendedFiles(datapack, rootPath);

        // 2. Add the mandatory pack.mcmeta file content to the intended map
        writePackMcmetaContent(datapack, rootPath, intendedFiles);

        // --- Directory Setup ---
        // Ensure the root datapack folder exists before proceeding.
        if (!rootDir.exists() && !rootDir.mkdirs()) {
            throw new IOException("Failed to create root directory: " + rootPath);
        }

        // --- Static Resources Synchronization (pack.png) ---
        Path packImagePath = rootPath.resolve(BINARY_IMAGE_NAME);
        boolean packImageFound = copyPackImage(rootPath); // Pass the root directory path

        if (packImageFound) {
            // Resource found: Add placeholder to prevent deletion and mark it as an intended file.
            intendedFiles.put(packImagePath, "BINARY_FILE_PLACEHOLDER");
        } else {
            // Resource NOT found in classpath: Synchronize by deleting the file from disk if it exists.
            if (Files.exists(packImagePath)) {
                try {
                    Files.delete(packImagePath);
                    LOGGER.warning("DELETE: " + packImagePath.getFileName() + " (Resource removed from classpath)");
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to delete obsolete pack.png: " + packImagePath, e);
                }
            }
            // By not adding to intendedFiles, it is properly ignored by the deletion loop.
        }

        // --- Comparison and Synchronization ---
        // The path to the 'data' directory where all Minecraft-specific content resides.
        Path dataPath = rootPath.resolve("data");

        // 3. Delete obsolete files (only files within the 'data' directory)
        if (Files.exists(dataPath)) {
            deleteObsoleteFiles(dataPath, intendedFiles);
        }

        // 4. Create or Update intended files
        for (Map.Entry<Path, String> entry : intendedFiles.entrySet()) {
            Path fullPath = entry.getKey();
            String content = entry.getValue();

            // Error Catching: Skip if the path is somehow null, though unlikely here.
            if (fullPath == null) continue;

            // Calculate the readable, relative path for logging: [pack.mcmeta or data/...]
            Path relativeLogPath = rootPath.relativize(fullPath);

            // Skip content comparison for binary files (like pack.png). Status determined in copyPackImage().
            if (fullPath.getFileName().toString().equals(BINARY_IMAGE_NAME)) {
                continue;
            }

            // Check if file exists and content is the same to avoid unnecessary file writes
            if (Files.exists(fullPath)) {

                // Read the existing content with the correct UTF-8 encoding
                String existingContent = Files.readString(fullPath, StandardCharsets.UTF_8);

                if (!existingContent.equals(content)) {
                    // Content has changed -> UPDATE
                    writeFile(fullPath, content);
                    LOGGER.config("UPDATE: " + relativeLogPath);
                } else {
                    // Content is identical -> LOG STATUS
                    LOGGER.config("UNCHANGED: " + relativeLogPath);
                }
            } else {
                // File does not exist -> CREATE
                writeFile(fullPath, content);
                LOGGER.config("CREATE: " + relativeLogPath);
            }
        }
    }

    /**
     * Helper method to recursively collect all intended file paths and their content
     * from the {@code Datapack} object structure.
     * * It iterates through namespaces, categories, and components to build a flat map
     * of destination {@code Path} to content {@code String}.
     * @param datapack The source Datapack object.
     * @param rootPath The root path of the final datapack folder.
     * @return A map of full {@code Path} objects to their intended String content.
     */
    private Map<Path, String> collectIntendedFiles(Datapack datapack, Path rootPath) {
        Map<Path, String> fileMap = new HashMap<>();
        Path dataPath = rootPath.resolve("data");

        // Loop through all registered namespaces (e.g., "minecraft", "uhc_core_pack")
        for (Map.Entry<String, Namespace> entry : datapack.getNamespaces().entrySet()) {
            // Error Catching: Ensure the map entry components are not null
            if (entry.getValue() == null) continue;

            Namespace namespace = entry.getValue();
            Path namespacePath = dataPath.resolve(namespace.getName());

            // Loop through all component categories (e.g., "function", "tags/function")
            for (Map.Entry<String, List<DatapackComponent>> categoryEntry : namespace.getComponentsByCategory().entrySet()) {
                Path categoryPath = namespacePath.resolve(categoryEntry.getKey());

                List<DatapackComponent> components = categoryEntry.getValue();

                // Loop through all individual components (files)
                for (DatapackComponent component : components) {
                    // Error Catching: Ensure component is valid and generates content
                    if (component == null || component.getPath() == null) continue;

                    // Construct the full path using the component's internal path (e.g., "init/load.mcfunction")
                    Path fullPath = categoryPath.resolve(component.getPath());
                    fileMap.put(fullPath, component.generateContent());
                }
            }
        }
        return fileMap;
    }

    /**
     * Generates the JSON content for {@code pack.mcmeta} and adds it to the map of intended files.
     * * @param datapack The source of metadata (description, format) used for the file content.
     * @param rootPath The root directory of the datapack.
     * @param intendedFiles The map to which the file path and content are added.
     */
    private void writePackMcmetaContent(Datapack datapack, Path rootPath, Map<Path, String> intendedFiles) {

        // Uses text block feature (Java 15+) for cleaner multiline string
        String jsonContent = String.format("""
                        {
                          "pack": {
                            "description": "%s",
                            "min_format": %d,
                            "max_format": %d
                          }
                        }
                        """,
                datapack.getDescription(),
                datapack.getPackFormat(),
                datapack.getPackFormat()
        );

        intendedFiles.put(rootPath.resolve("pack.mcmeta"), jsonContent);
    }

    /**
     * Compares files on disk within the {@code 'data'} directory against the map of intended files
     * and deletes any that are obsolete (no longer present in the current {@code Datapack} object structure).
     * * This operation is critical for full synchronization.
     * @param directory The 'data' directory path to start the recursive check from.
     * @param intendedFiles The map of files that *should* exist.
     * @throws IOException If the directory walking process fails.
     */
    private void deleteObsoleteFiles(Path directory, Map<Path, String> intendedFiles) throws IOException {

        // Recursively walk through all files and directories starting at 'data'
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.filter(Files::isRegularFile)
                    .forEach(existingPath -> {
                        // Check if the existing file on disk is NOT in our intended map
                        if (!intendedFiles.containsKey(existingPath)) {
                            try {
                                Files.delete(existingPath);
                                // Log deletion as a WARNING (yellow color)
                                LOGGER.warning("DELETE: " + directory.relativize(existingPath));
                            } catch (IOException e) {
                                // Log failure to delete as SEVERE error, but continue cleanup
                                LOGGER.log(Level.SEVERE, "Failed to delete obsolete file: " + existingPath, e);
                            }
                        }
                    });
        }

        // Cleanup: Delete empty directories from bottom up (longest paths first)
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.filter(Files::isDirectory)
                    // Sort by path length descending to delete children before parents
                    .sorted((p1, p2) -> p2.toString().length() - p1.toString().length())
                    .forEach(dir -> {
                        try {
                            if (dir.equals(directory)) return; // Skip the root 'data' directory itself
                            // Use Files.list() to check directory emptiness efficiently
                            if (Files.list(dir).findAny().isEmpty()) {
                                Files.delete(dir);
                                LOGGER.fine("CLEANUP: Deleted empty directory " + directory.relativize(dir));
                            }
                        } catch (IOException e) {
                            // Log cleanup failure but do not throw, as it's not critical
                            LOGGER.log(Level.FINER, "Could not delete empty directory: " + dir);
                        }
                    });
        }
    }

    /**
     * Writes string content to a specified file path, creating parent directories as necessary.
     * * @param fullPath The complete file path to write to.
     * @param content The string content to be written (using UTF-8 encoding).
     * @throws IOException If directory creation or file writing fails.
     */
    private void writeFile(Path fullPath, String content) throws IOException {

        // Safety improvement: Use Files.createDirectories for atomic and safer directory creation
        Path parentDir = fullPath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        // Use Files.writeString for reliable, explicit UTF-8 encoding, which is required by Minecraft.
        java.nio.file.Files.writeString(
                fullPath,
                content,
                StandardCharsets.UTF_8
        );
    }

    /**
     * Copies the mandatory {@code pack.png} image resource from the classpath to the root of the output datapack folder.
     * This method determines if the resource exists in the classpath and, if so, copies/updates the file based on size comparison.
     * @param rootPath The path to the root of the final datapack folder (e.g., {@code .../uhc_datapack}).
     * @return {@code true} if the resource was found and handled (i.e., it should exist in the output); {@code false} if the resource was missing from the classpath.
     * @throws IOException If file writing fails or if an I/O error occurs during comparison.
     */
    private boolean copyPackImage(Path rootPath) throws IOException {

        Path destinationPath = rootPath.resolve(BINARY_IMAGE_NAME);

        // Access the resource using the application's ClassLoader.
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(BINARY_IMAGE_NAME)) {

            if (inputStream == null) {
                // Resource is MISSING from classpath. Signal to the caller to delete the existing file.
                LOGGER.config("MISSING: Required resource '" + BINARY_IMAGE_NAME + "' not found in classpath.");
                return false;
            }

            // --- Resource Found Logic (Copy/Update) ---
            boolean needsCopy = true;

            if (Files.exists(destinationPath)) {
                // Check file size to determine if content is unchanged (fast check for binary files)
                long existingSize = Files.size(destinationPath);
                long newSize = inputStream.available();

                if (existingSize == newSize) {
                    LOGGER.config("UNCHANGED: " + destinationPath.getFileName() + " (Binary size match)");
                    needsCopy = false;
                } else {
                    LOGGER.config("UPDATE: " + destinationPath.getFileName() + " (Binary size mismatch)");
                }
            } else {
                LOGGER.config("CREATE: " + destinationPath.getFileName());
            }

            if (needsCopy) {
                // Perform the copy operation, overwriting the destination file.
                // NOTE: InputStream.available() consumes the stream position, so we reset it if possible,
                // but since we are copying immediately after, a simple Files.copy is fine.
                Files.copy(inputStream, destinationPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                LOGGER.config("COPIED: " + destinationPath.getFileName());
            }

            return true; // Indicate that the file IS present and handled.

        } catch (IOException e) {
            // Catch I/O errors during copy/check (e.g., file permissions)
            LOGGER.log(Level.SEVERE, "Failed to handle pack.png at: " + destinationPath, e);
            throw e; // Re-throw to halt generation if file writing fails.
        }
    }
}