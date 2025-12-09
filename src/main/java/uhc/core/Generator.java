package uhc.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;

/**
 * Handles the actual creation of files and folders on the file system.
 * This class translates the object-oriented structure (Datapack, Namespace, Component)
 * into the required Minecraft file hierarchy using robust, cross-platform NIO Path logic.
 */
public class Generator {

    /**
     * Executes the file generation process based on the contents of the Datapack object.
     * @param datapack The complete object representation of the datapack.
     * @param outputDirectory The root directory where the pack folder should be created
     * (sourced from DatapackConfig.OUTPUT_DIR_ROOT).
     * @throws IOException If directory creation or file writing fails.
     */
    public void generate(Datapack datapack, String outputDirectory) throws IOException {

        // Use NIO Path for cross-platform compatibility and static config value for the folder name.
        Path rootPath = Paths.get(outputDirectory, DatapackConfig.DATAPACK_FOLDER_NAME);
        File rootDir = rootPath.toFile();

        // Create the top-level datapack folder
        if (!rootDir.exists() && !rootDir.mkdirs()) {
            throw new IOException("Failed to create root directory: " + rootPath);
        }

        // 1. Write the mandatory pack.mcmeta file
        writePackMcmeta(datapack, rootPath);

        // 2. Iterate through namespaces and components to write content
        Path dataPath = rootPath.resolve("data");

        // Loop 1: Iterate Namespaces (e.g., "minecraft" and "uhc_core_pack")
        for (Map.Entry<String, Namespace> entry : datapack.getNamespaces().entrySet()) {
            Namespace namespace = entry.getValue();
            Path namespacePath = dataPath.resolve(namespace.getName());

            // Loop 2: Iterate Categories (e.g., "function", "tags/function")
            for (Map.Entry<String, List<DatapackComponent>> categoryEntry : namespace.getComponentsByCategory().entrySet()) {

                String category = categoryEntry.getKey();
                List<DatapackComponent> components = categoryEntry.getValue();

                // Construct the full category path (e.g., ".../data/uhc_core_pack/function")
                Path categoryPath = namespacePath.resolve(category);

                // Loop 3: Iterate Components (actual files, e.g., load.mcfunction)
                for (DatapackComponent component : components) {

                    // Use resolve(String) for resource IDs to safely handle subdirectories (e.g., "init/load.mcfunction")
                    Path fullPath = categoryPath.resolve(component.getPath());

                    // Write file content and create any necessary subdirectories (e.g., 'init/')
                    writeFile(fullPath, component.generateContent());
                }
            }
        }
    }

    /**
     * Generates and writes the pack.mcmeta file with the required version format.
     * @param datapack The source of metadata (description, format).
     * @param rootPath The root directory of the datapack.
     * @throws IOException If file writing fails.
     */
    private void writePackMcmeta(Datapack datapack, Path rootPath) throws IOException {

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

        writeFile(rootPath.resolve("pack.mcmeta"), jsonContent);
    }

    /**
     * Writes content to a specified file path, creating parent directories as necessary.
     * Uses explicit UTF-8 encoding for reliable file writing, as required by Minecraft.
     * @param fullPath The complete file path to write to.
     * @param content The string content to be written.
     * @throws IOException If directory creation or file writing fails.
     */
    private void writeFile(Path fullPath, String content) throws IOException {
        File file = fullPath.toFile();

        // Create necessary parent directories (e.g., init/ or loop/)
        if (file.getParentFile() != null && !file.getParentFile().mkdirs() && !file.getParentFile().exists()) {
            throw new IOException("Failed to create parent directories for: " + fullPath);
        }

        // Use Files.writeString for reliable, explicitly UTF-8 encoding
        java.nio.file.Files.writeString(
                fullPath,
                content,
                StandardCharsets.UTF_8
        );
    }
}