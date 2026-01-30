package uhc;

import uhc.core.*;
import uhc.game.player.PlayerData;
import uhc.game.player.PlayerRegistry;
import uhc.functions.*;
import uhc.functions.control_point.ControlPointRecords;
import uhc.functions.control_point.ControlPointVisuals;
import uhc.functions.fun.EquipGearFunction;
import uhc.functions.fun.GodModeFunction;
import uhc.functions.init.InitializationFunction;
import uhc.functions.init.InitializeFunction;
import uhc.functions.player_death.DropHeadsFunction;
import uhc.functions.player_death.PlayerDeathFunction;
import uhc.functions.startup.StartPotionsFunction;
import uhc.functions.util.ClearEnderChestFunction;
import uhc.game.control_points.ControlPointRegistry;
import uhc.game.world.WorldData;
import uhc.logging.CustomConsoleFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

/**
 * 🚀 **Main Application Entry Point**
 * <p>
 * This class orchestrates the entire UHC datapack generation lifecycle.
 * It manages the transition from high-level Java module definitions to
 * physical {@code .mcfunction} files on disk.
 * </p>
 * <p>
 * <b>Strict Error Policy:</b> Any failure in the registration or generation
 * phase will trigger a {@link System#exit(int)} with a non-zero code to
 * prevent the deployment of corrupted or incomplete datapacks.
 * </p>
 */
public final class Main {

    // --- 📄 Static Fields ---

    /** * The primary {@link Logger} for the application.
     * Used to track the progress of the assembly pipeline and report errors.
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    // --- 🏗️ Entry Point ---

    /**
     * The main execution loop of the application.
     * <p>
     * Orchestrates the setup of the logging environment, the building of the
     * virtual datapack structure, and the final physical file export.
     * </p>
     * @param args Command line arguments (reserved for future configuration).
     */
    public static void main(String[] args) {
        setupLoggingEnvironment();

        try {
            LOGGER.info("Starting Datapack Assembly Pipeline...");

            // Step 1: Build the virtual representation of the Datapack in memory
            Datapack datapack = buildDatapackStructure();

            // Step 2: Flush the virtual structure to physical files on the disk
            executeGeneration(datapack);

            LOGGER.info("Datapack generation sequence finalized successfully.");
        } catch (Exception e) {
            // Non-zero exit code ensures build tools (like Gradle/Maven) detect the failure
            LOGGER.log(Level.SEVERE, "CRITICAL FAILURE: Datapack generation was aborted.", e);
            System.exit(1);
        }
    }

    // --- ⚙️ Internal Logic Segments ---

    /**
     * Configures the global Java Logging API for clean, formatted console output.
     * <p>
     * Purges default handlers to prevent duplicate logging and injects the
     * {@link CustomConsoleFormatter} for improved readability.
     * </p>
     * @throws RuntimeException if the logging system is inaccessible or fails to initialize.
     */
    private static void setupLoggingEnvironment() {
        try {
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();

            // Remove existing handlers to ensure our custom format is the only output source
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            ConsoleHandler customHandler = new ConsoleHandler();
            customHandler.setLevel(Level.ALL);
            customHandler.setFormatter(new CustomConsoleFormatter());
            rootLogger.addHandler(customHandler);

            // Set verbosity thresholds for the application package
            LOGGER.setLevel(Level.INFO);
            Logger.getLogger("uhc").setLevel(Level.INFO);
        } catch (Exception e) {
            // System.err is used as the absolute fallback if the logger itself is broken
            System.err.println("FATAL: The logging environment failed to initialize. Aborting process.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Orchestrates the creation of the {@link Datapack} and the registration
     * of all functional modules.
     * <p>
     * This method synchronizes player data with world size before initializing
     * namespaces and game session registries.
     * </p>
     * @return A fully populated and validated {@link Datapack} instance.
     * @throws IllegalStateException if any registry, namespace, or player data is invalid.
     * @throws RuntimeException if any module fails to register correctly.
     */
    private static Datapack buildDatapackStructure() {
        Datapack datapack = new Datapack();

        // 0. Initialize Game Session Parameters via Singleton Registry
        try {
            LOGGER.info("Querying PlayerRegistry for active participants...");

            // Retrieve participating players from the singleton roster
            List<PlayerData> activePlayers = PlayerRegistry.getParticipating();
            int playerCount = activePlayers.size();

            LOGGER.info(String.format("Found %d participating players in the roster.", playerCount));

            // Calculate and lock the world size scaling based on the headcount
            WorldData.calculateWorldSize(playerCount);
            LOGGER.info("World size locked at radius: " + WorldData.getWorldRadius());

        } catch (Exception e) {
            // No-fallback: We cannot generate a UHC if world size or player data is missing
            throw new IllegalStateException("Startup Error: Failed to synchronize game session data. Details: " + e.getMessage(), e);
        }

        // 1. Resolve the primary Namespace
        final String namespaceName = DatapackConfig.CUSTOM_NAMESPACE;
        Namespace targetNamespace = datapack.getOrCreateNamespace(namespaceName);

        if (targetNamespace == null) {
            throw new IllegalStateException("Namespace Error: The required namespace '" + namespaceName + "' could not be created.");
        }

        // 2. Initialize Control Point Data
        try {
            LOGGER.info("Initializing Game Session Registries...");
            ControlPointRegistry registry = new ControlPointRegistry();
            registry.initializeSession();
        } catch (Exception e) {
            throw new IllegalStateException("Registry Error: Failed to bootstrap the Control Point session registry.", e);
        }

        // 3. Define the functional modules to be compiled into the datapack
        List<DatapackFunction> modules = List.of(
                new InitializeFunction(),
                new ClearEnderChestFunction(),
                new GodModeFunction(),
                new EquipGearFunction(),
                new DropHeadsFunction(),
                new InitializationFunction(),
                new PlayerDeathFunction(),
                new StartPotionsFunction(),
                new ControlPointVisuals(),
                new ControlPointRecords()
        );

        LOGGER.info("Registering " + modules.size() + " functional modules...");

        // 4. Registration Loop: Binds functions to the virtual datapack structure
        for (DatapackFunction module : modules) {
            // Strict null check for the module list
            Objects.requireNonNull(module, "Registration Error: Attempted to register a null module entry.");

            try {
                LOGGER.info(" -> Registering: " + module.getClass().getSimpleName());
                module.register(datapack, targetNamespace);
            } catch (Exception e) {
                // Wrap and rethrow to trigger the main try-catch exit
                throw new RuntimeException("Module Error: Failure during registration of " + module.getClass().getSimpleName(), e);
            }
        }

        return datapack;
    }

    /**
     * Translates the virtual {@link Datapack} object tree into physical files on the OS.
     * <p>
     * Validates the existence of the root directory and converts the
     * virtual memory structure into the standard Minecraft Datapack directory format.
     * </p>
     * @param datapack The populated {@link Datapack} to export.
     * @throws IOException if a filesystem permission error or write failure occurs.
     * @throws IllegalStateException if the output configuration is missing or blank.
     * @throws NullPointerException if the provided datapack object is null.
     */
    private static void executeGeneration(Datapack datapack) throws IOException {
        Objects.requireNonNull(datapack, "Generation Error: The provided Datapack object is null.");

        Generator generator = new Generator();
        String rootDir = DatapackConfig.OUTPUT_DIR_ROOT;

        // Ensure the configuration provides a valid target path
        if (rootDir == null || rootDir.isBlank()) {
            throw new IllegalStateException("Config Error: 'OUTPUT_DIR_ROOT' is not defined in DatapackConfig.");
        }

        // Use NIO.2 Paths for cross-platform compatibility and absolute path logging
        Path absolutePath = Paths.get(rootDir).toAbsolutePath();
        LOGGER.info("Syncing virtual structure to filesystem at: " + absolutePath);

        try {
            // The generator handles the recursive creation of folders and .mcfunction files
            generator.generate(datapack, absolutePath.toString());
        } catch (IOException e) {
            throw new IOException("FileSystem Error: Failed to write datapack files. Ensure the path is writable: " + absolutePath, e);
        }
    }
}