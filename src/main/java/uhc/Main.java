package uhc;

import uhc.core.Datapack;
import uhc.core.DatapackConfig;
import uhc.core.Generator;
import uhc.modules.DatapackModule;
import uhc.modules.InitializationModule;
import uhc.logging.CustomConsoleFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;

/**
 * 🚀 **Main Application Entry Point**
 * * This class orchestrates the entire datapack generation process.
 * It is responsible for initializing the custom logging system, defining the
 * feature modules, building the {@code Datapack} structure, and calling the
 * {@code Generator} to write the files to the disk.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        // --- Logging Initialization ---

        // 1. Get the Root Logger
        Logger rootLogger = Logger.getLogger("");

        // CRITICAL: Prevent Java's default, messy console output by removing all default handlers
        // from the root logger before adding our custom one.
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        // 2. Setup Custom Handler
        try {
            ConsoleHandler customHandler = new ConsoleHandler();

            // Set the handler level to ALL so it passes all messages (CONFIG, INFO, WARNING, etc.)
            customHandler.setLevel(Level.ALL);
            customHandler.setFormatter(new CustomConsoleFormatter());

            // Add the custom handler to the Root Logger. All messages flow through this formatter.
            rootLogger.addHandler(customHandler);

            // 3. Set Specific Logger Levels
            // The Main logger uses INFO for general start/complete messages (Green).
            LOGGER.setLevel(Level.INFO);

            // The Generator logger uses CONFIG for file status messages (Yellow).
            Logger.getLogger(Generator.class.getName()).setLevel(Level.CONFIG);

        } catch (Exception e) {
            // Error Catching Improvement: If custom logging setup fails, print an error
            // and exit gracefully to prevent unpredictable console behavior.
            System.err.println("FATAL LOGGING ERROR: Failed to configure custom console formatter.");
            e.printStackTrace();
            // Exit immediately, as the logging system is compromised.
            System.exit(1);
        }
        // -------------------------------------------------------------

        // --- Datapack Orchestration ---

        // 1. Setup Datapack Core: Creates the empty structure based on DatapackConfig metadata.
        Datapack datapack = new Datapack();

        // 2. Define Modules: List all feature sets to be included in the final datapack.
        List<DatapackModule> modules = List.of(
                new InitializationModule()
        );

        // 3. Register Modules: Instruct each module to build its components (functions, tags, etc.)
        // and register them within the Datapack object structure.
        for (DatapackModule module : modules) {
            module.register(datapack, DatapackConfig.CUSTOM_NAMESPACE);
        }

        // 4. Generate Files: Call the Generator to translate the object structure into files on disk.
        Generator generator = new Generator();

        // Determine the final, absolute output path using the configurable root directory.
        Path absolutePath = Paths.get(DatapackConfig.OUTPUT_DIR_ROOT).toAbsolutePath();

        // Log the final target path before starting I/O operations.
        LOGGER.info("Starting Datapack generation process in: " + absolutePath.resolve(DatapackConfig.DATAPACK_FOLDER_NAME));

        try {
            generator.generate(datapack, absolutePath.toString());
            LOGGER.info("Generation and synchronization complete.");
        } catch (IOException e) {
            // Catch and log fatal I/O errors during file writing (e.g., permission issues).
            LOGGER.log(Level.SEVERE, "FATAL ERROR: Failed to write datapack files. Check file permissions or path.", e);
            // Optional: Exit with a non-zero code to indicate failure to external scripts/tools.
            // System.exit(1);
        }
    }
}