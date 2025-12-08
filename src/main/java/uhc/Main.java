package uhc;

import uhc.core.Datapack;
import uhc.core.Generator;
import uhc.modules.DatapackModule;
import uhc.modules.GameLoopModule;
import uhc.modules.InitializationModule;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main entry point for the Datapack generation application.
 * This class is responsible for setting up the configuration, registering all feature modules,
 * and initiating the file generation process.
 */
public class Main {

    // Define a static logger for this class to provide robust error and status logging.
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String OUTPUT_DIR_ROOT = "Server/world/datapacks";
    private static final String DATAPACK_FOLDER_NAME = "uhc_datapack";

    /**
     * The name of the custom namespace where all primary functions and resources reside.
     */
    private static final String CUSTOM_NAMESPACE = "uhc_core_pack";

    public static void main(String[] args) {

        // --- 1. Setup Datapack Core ---
        // Initializes the Datapack object with its pack.mcmeta metadata.
        Datapack datapack = new Datapack("Example pack for Minecraft 1.21.9");

        // --- 2. Define Modules (Feature Registration) ---
        // List all the active features/modules to be included in the final datapack.
        List<DatapackModule> modules = List.of(
                new InitializationModule(),
                new GameLoopModule()
        );

        // --- 3. Register Modules ---
        // Iterate through each module and execute its registration logic, populating the Datapack object.
        for (DatapackModule module : modules) {
            module.register(datapack, CUSTOM_NAMESPACE);
        }

        // --- 4. Generate Files ---
        Generator generator = new Generator();
        generator.setPackFolderName(DATAPACK_FOLDER_NAME);

        // Construct the output path using NIO for OS compatibility.
        Path absolutePath = Paths.get(OUTPUT_DIR_ROOT).toAbsolutePath();

        LOGGER.info("Starting Datapack generation process.");

        try {
            // Write the Datapack object's contents to the file system.
            generator.generate(datapack, absolutePath.toString());
            LOGGER.info("Generation complete. Datapack written to: " + absolutePath);
        } catch (IOException e) {
            // Logging the exception at SEVERE level provides robust context (timestamp, class, and full stack trace).
            LOGGER.log(Level.SEVERE, "FATAL ERROR: Failed to write datapack files. Check file permissions or path.", e);
        }
    }
}