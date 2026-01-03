package uhc.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * 🗃️ **Datapack File Generator and Synchronizer**
 * <p>
 * This class handles the physical serialization of the {@link Datapack} object model
 * to the filesystem. It performs a state-synchronization (diff) to ensure the
 * target directory is an exact mirror of the virtual structure, handling creation,
 * updates, and deletion of obsolete files.
 * </p>
 */
public class Generator {

    // --- 📄 Static Fields ---

    /** * The internal logger instance used for tracking synchronization status
     * and reporting filesystem mutations (CREATE, UPDATE, DELETE).
     */
    private static final Logger LOGGER = Logger.getLogger(Generator.class.getName());

    // --- 🏗️ Public Entry Point ---

    /**
     * Executes the comprehensive file generation and synchronization pipeline.
     * <p>
     * This method orchestrates the virtual-to-physical mapping, directory setup,
     * obsolete file purging, and content verification.
     * </p>
     * * @param datapack        The {@link Datapack} model to synchronize; must not be null.
     * @param outputDirectory The base string path where the datapack folder will be generated.
     * @throws IOException           if any critical I/O operation fails (permissions, disk space, etc.).
     * @throws NullPointerException  if the provided {@code datapack} is null.
     * @throws IllegalStateException if the root directory cannot be created or accessed.
     */
    public void generate(Datapack datapack, String outputDirectory) throws IOException {
        Objects.requireNonNull(datapack, "Generation Failure: Source Datapack cannot be null.");
        Objects.requireNonNull(outputDirectory, "Generation Failure: Output directory path cannot be null.");

        // Resolve absolute paths
        Path rootPath = Paths.get(outputDirectory, DatapackConfig.DATAPACK_FOLDER_NAME);
        File rootDir = rootPath.toFile();

        // Step 1: Map virtual components to physical paths
        Map<Path, String> intendedFiles = collectIntendedFiles(datapack, rootPath);

        // Step 2: Integrate mandatory metadata (pack.mcmeta)
        writePackMcmetaContent(datapack, rootPath, intendedFiles);

        // Step 3: Ensure root directory integrity
        if (!rootDir.exists() && !rootDir.mkdirs()) {
            throw new IOException("FileSystem Error: Failed to create or access root directory at: " + rootPath);
        }

        Path dataPath = rootPath.resolve("data");

        // Step 4: Synchronize Deletions (Sync Down)
        if (Files.exists(dataPath)) {
            deleteObsoleteFiles(dataPath, intendedFiles);
        }

        // Step 5: Synchronize Content (Sync Up)
        for (Map.Entry<Path, String> entry : intendedFiles.entrySet()) {
            Path fullPath = entry.getKey();
            String content = entry.getValue();
            Path relativeLogPath = rootPath.relativize(fullPath);

            try {
                if (Files.exists(fullPath)) {
                    String existingContent = Files.readString(fullPath, StandardCharsets.UTF_8);

                    if (!existingContent.equals(content)) {
                        writeFile(fullPath, content);
                        LOGGER.info("UPDATE: " + relativeLogPath);
                    } else {
                        LOGGER.info("UNCHANGED: " + relativeLogPath);
                    }
                } else {
                    writeFile(fullPath, content);
                    LOGGER.info("CREATE: " + relativeLogPath);
                }
            } catch (IOException e) {
                throw new IOException("Sync Failure: Could not process file " + relativeLogPath + ". " + e.getMessage(), e);
            }
        }
    }

    // --- ⚙️ Internal Logic Segments ---

    /**
     * Traverses the virtual {@link Datapack} structure to build a flat map of intended files.
     * * @param datapack The source model.
     * @param rootPath The target root path.
     * @return A {@link Map} where keys are absolute filesystem {@link Path}s and values are file contents.
     * @throws RuntimeException if content generation for a component fails.
     */
    private Map<Path, String> collectIntendedFiles(Datapack datapack, Path rootPath) {
        Map<Path, String> fileMap = new HashMap<>();
        Path dataPath = rootPath.resolve("data");

        try {
            datapack.getNamespaces().forEach((name, namespace) -> {
                Path namespacePath = dataPath.resolve(namespace.getName());

                namespace.getComponentsByCategory().forEach((category, components) -> {
                    Path categoryPath = namespacePath.resolve(category);

                    for (DatapackComponent component : components) {
                        Path fullPath = categoryPath.resolve(component.getPath());
                        fileMap.put(fullPath, component.generateContent());
                    }
                });
            });
        } catch (Exception e) {
            throw new RuntimeException("Model Error: Failed to traverse Datapack structure for file collection. " + e.getMessage(), e);
        }

        return fileMap;
    }

    /**
     * Formats the {@code pack.mcmeta} JSON structure and injects it into the synchronization map.
     * * @param datapack      The source for metadata (Description/Format).
     * @param rootPath      The root directory of the pack.
     * @param intendedFiles The map where the generated path/content will be stored.
     */
    private void writePackMcmetaContent(Datapack datapack, Path rootPath, Map<Path, String> intendedFiles) {
        try {
            String jsonContent = String.format("""
                            {
                              "pack": {
                                "description": "%s",
                                "pack_format": %d
                              }
                            }
                            """,
                    datapack.getDescription(),
                    datapack.getPackFormat()
            );
            intendedFiles.put(rootPath.resolve("pack.mcmeta"), jsonContent);
        } catch (Exception e) {
            throw new RuntimeException("Metadata Error: Failed to generate pack.mcmeta content. " + e.getMessage(), e);
        }
    }

    /**
     * Purges files and directories from the 'data' folder that are not present in the code model.
     * * @param directory     The 'data' directory path to scan.
     * @param intendedFiles The map of paths that are permitted to exist.
     * @throws IOException if directory walking or file deletion fails.
     */
    private void deleteObsoleteFiles(Path directory, Map<Path, String> intendedFiles) throws IOException {
        // Phase 1: File Deletion
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.filter(Files::isRegularFile)
                    .forEach(existingPath -> {
                        if (!intendedFiles.containsKey(existingPath)) {
                            try {
                                Files.delete(existingPath);
                                LOGGER.warning("DELETE: " + directory.getParent().relativize(existingPath));
                            } catch (IOException e) {
                                LOGGER.log(Level.SEVERE, "Cleanup Failure: Could not delete obsolete file: " + existingPath, e);
                            }
                        }
                    });
        } catch (Exception e) {
            throw new IOException("Cleanup Error: Failed during obsolete file scanning. " + e.getMessage(), e);
        }

        // Phase 2: Empty Directory Cleanup (Bottom-Up)
        try (Stream<Path> stream = Files.walk(directory)) {
            stream.filter(Files::isDirectory)
                    .sorted((p1, p2) -> p2.toString().length() - p1.toString().length())
                    .forEach(dir -> {
                        try {
                            if (dir.equals(directory)) return;
                            try (Stream<Path> list = Files.list(dir)) {
                                if (list.findAny().isEmpty()) {
                                    Files.delete(dir);
                                }
                            }
                        } catch (IOException ignored) {
                            // Directory cleanup failures are non-critical and suppressed to prevent halting sync
                        }
                    });
        }
    }

    /**
     * Low-level helper to write string content to a file using UTF-8 encoding.
     * * @param fullPath The absolute destination path.
     * @param content  The raw string content.
     * @throws IOException if parent directories cannot be created or file writing fails.
     */
    private void writeFile(Path fullPath, String content) throws IOException {
        Path parentDir = fullPath.getParent();
        if (parentDir != null) {
            try {
                Files.createDirectories(parentDir);
            } catch (IOException e) {
                throw new IOException("FileSystem Error: Could not create directory structure " + parentDir, e);
            }
        }

        try {
            Files.writeString(fullPath, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IOException("FileSystem Error: Failed to write content to " + fullPath, e);
        }
    }
}