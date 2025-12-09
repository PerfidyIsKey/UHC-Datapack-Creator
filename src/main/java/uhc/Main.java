package uhc;

import uhc.core.Datapack;
import uhc.core.DatapackConfig;
import uhc.core.Generator;
import uhc.modules.DatapackModule;
import uhc.modules.GameLoopModule;
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
 * The main entry point for the Datapack generation application.
 * This class orchestrates the setup, registers all feature modules,
 * and initiates the file generation process.
 */
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        // --- Custom Logging Setup (Enables colorized output) ---
        try {
            // Disable default parent handler to control formatting
            LOGGER.setUseParentHandlers(false);

            ConsoleHandler handler = new ConsoleHandler();
            // Apply the custom color formatter
            handler.setFormatter(new CustomConsoleFormatter());
            LOGGER.addHandler(handler);
        } catch (Exception e) {
            // Robust fallback if custom logging setup fails (e.g., CustomConsoleFormatter error)
            LOGGER.log(Level.WARNING, "Failed to set up custom logger formatter. Using default console output.", e);
            // Re-enable parent handler so logs still appear
            LOGGER.setUseParentHandlers(true);
        }
        // -------------------------------------------------------------

        // --- 1. Setup Datapack Core ---
        // Datapack constructor is parameterless, pulling metadata from DatapackConfig.
        Datapack datapack = new Datapack();

        // --- 2. Define Modules (Feature Registration) ---
        // List all the active features/modules to be included.
        List<DatapackModule> modules = List.of(
                new InitializationModule(),
                new GameLoopModule()
        );

        // --- 3. Register Modules ---
        // Pass the Datapack object and the custom namespace defined in the Config.
        for (DatapackModule module : modules) {
            module.register(datapack, DatapackConfig.CUSTOM_NAMESPACE);
        }

        // --- 4. Generate Files ---
        Generator generator = new Generator();

        // Use the static path from DatapackConfig for OS-independent path construction.
        Path absolutePath = Paths.get(DatapackConfig.OUTPUT_DIR_ROOT).toAbsolutePath();

        LOGGER.info("Starting Datapack generation process.");

        try {
            // Execute file generation.
            generator.generate(datapack, absolutePath.toString());
            LOGGER.info("Generation complete. Datapack written to: " + absolutePath);
        } catch (IOException e) {
            // Logging at SEVERE level ensures the full trace is included via the custom formatter.
            LOGGER.log(Level.SEVERE, "FATAL ERROR: Failed to write datapack files. Check file permissions or path.", e);
        }
    }
}