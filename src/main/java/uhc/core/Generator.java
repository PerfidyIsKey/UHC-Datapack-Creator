package uhc.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Handles the actual creation of files and folders on the file system.
 */
public class Generator {

    /**
     * Generates the datapack structure and content.
     * @param datapack The Datapack object to generate.
     * @param outputDirectory The root directory where the datapack folder will be created.
     * @throws IOException If there is an error writing files or creating directories.
     */
    public void generate(Datapack datapack, String outputDirectory) throws IOException {
        // Use the namespace name as the folder name for simplicity, or a custom name:
        String packFolderName = "uhc_datapack"; // <-- Use a fixed, unique name here
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

            for (DatapackComponent component : namespace.getComponents()) {
                Path fullPath = namespacePath.resolve(component.getPath());
                writeFile(fullPath, component.generateContent());
            }
        }
        System.out.println("Datapack generated successfully at: " + rootPath.toAbsolutePath());
    }

    private void writePackMcmeta(Datapack datapack, Path rootPath) throws IOException {
        String jsonContent = String.format("""
            {
                "pack": {
                    "pack_format": %d,
                    "description": "%s"
                }
            }
            """, datapack.getPackFormat(), datapack.getDescription());

        writeFile(rootPath.resolve("pack.mcmeta"), jsonContent);
    }

    private void writeFile(Path fullPath, String content) throws IOException {
        File file = fullPath.toFile();
        // Ensure the parent directories exist before writing the file
        if (file.getParentFile() != null && !file.getParentFile().mkdirs() && !file.getParentFile().exists()) {
            throw new IOException("Failed to create parent directories for: " + fullPath);
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}