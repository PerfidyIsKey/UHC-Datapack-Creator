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
 */
public class Generator {

    private String packFolderName = "default_datapack";

    public void setPackFolderName(String name) {
        this.packFolderName = name;
    }

    public void generate(Datapack datapack, String outputDirectory) throws IOException {
        // ... (Path setup remains the same)
        Path rootPath = Paths.get(outputDirectory, packFolderName);
        File rootDir = rootPath.toFile();

        if (!rootDir.exists() && !rootDir.mkdirs()) {
            throw new IOException("Failed to create root directory: " + rootPath);
        }

        // 1. Write the mandatory pack.mcmeta file
        writePackMcmeta(datapack, rootPath);

        // 2. Iterate through namespaces and components to write content
        Path dataPath = rootPath.resolve("data");

        for (Map.Entry<String, Namespace> entry : datapack.getNamespaces().entrySet()) {
            Namespace namespace = entry.getValue();
            Path namespacePath = dataPath.resolve(namespace.getName());

            // Iterate over the components grouped by their category (e.g., "function", "tags/function")
            for (Map.Entry<String, List<DatapackComponent>> categoryEntry : namespace.getComponentsByCategory().entrySet()) {

                String category = categoryEntry.getKey();
                List<DatapackComponent> components = categoryEntry.getValue();

                // Construct the full category path (e.g., ".../data/uhc_core_pack/function")
                Path categoryPath = namespacePath.resolve(category);

                for (DatapackComponent component : components) {
                    // component.getPath() returns the relative filename (e.g., "init/load.mcfunction")
                    Path componentRelativePath = Paths.get(component.getPath());
                    Path fullPath = categoryPath.resolve(componentRelativePath);

                    writeFile(fullPath, component.generateContent());
                }
            }
        }
        System.out.println("Datapack generated successfully at: " + rootPath.toAbsolutePath());
    }

    private void writePackMcmeta(Datapack datapack, Path rootPath) throws IOException {

        // New JSON structure: description string and single integer formats.
        String jsonContent = String.format("""
                        {
                          "pack": {
                            "description": "%s",
                            "min_format": %d,
                            "max_format": %d
                          }
                        }
                        """,
                // Arguments passed to String.format:
                datapack.getDescription(), // String (%s)
                datapack.getPackFormat(),  // int (%d)
                datapack.getPackFormat()   // int (%d)
        );

        // This is the writeFile method that correctly handles UTF-8 encoding
        writeFile(rootPath.resolve("pack.mcmeta"), jsonContent);
    }

    private void writeFile(Path fullPath, String content) throws IOException {
        File file = fullPath.toFile();
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