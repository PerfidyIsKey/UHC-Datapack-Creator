package uhc;

import uhc.core.Datapack;
import uhc.core.DatapackConfig;
import uhc.core.Generator;
import uhc.core.Namespace;
import uhc.functions.*;
import uhc.functions.control_point.ControlPointRecords;
import uhc.functions.control_point.ControlPointVisuals;
import uhc.game.control_points.ControlPointRegistry;
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
 * <b>Execution Pipeline:</b>
 * <ol>
 * <li><b>Logging Environment:</b> Bootstraps the custom console formatter.</li>
 * <li><b>Structure Assembly:</b> Instantiates the Datapack and resolves the core Namespace.</li>
 * <li><b>Registry Initialization:</b> Instantiates and populates the {@link ControlPointRegistry}.</li>
 * <li><b>Module Registration:</b> Sequentially executes the logic for every {@link DatapackFunction}.</li>
 * <li><b>IO Generation:</b> Flushes the in-memory components to the filesystem.</li>
 * </ol>
 * </p>
 */
public final class Main {

    // --- 📄 Static Fields ---

    /** * The primary {@link Logger} instance for the application entry point.
     * Configured during the {@link #setupLoggingEnvironment()} phase.
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    // --- 🏗️ Entry Point ---

    /**
     * The main execution loop of the application.
     * <p>
     * Utilizes a top-level try-catch block to ensure that any critical failure
     * during the generation process results in a non-zero exit code and a
     * detailed error report.
     * </p>
     *
     * @param args Command line arguments (reserved for future use).
     */
    public static void main(String[] args) {
        setupLoggingEnvironment();

        try {
            LOGGER.info("Starting Datapack Assembly Pipeline...");

            // Step 1: Build the virtual representation of the Datapack
            Datapack datapack = buildDatapackStructure();

            // Step 2: Write the virtual structure to physical files
            executeGeneration(datapack);

            LOGGER.info("Datapack generation sequence finalized successfully.");
        } catch (Exception e) {
            // Detailed reporting of the specific failure chain
            LOGGER.log(Level.SEVERE, "CRITICAL FAILURE: Datapack generation was aborted.", e);
            System.exit(1);
        }
    }

    // --- ⚙️ Internal Logic Segments ---

    /**
     * Configures the global Java Logging API for clean console output.
     * <p>
     * This method removes default {@link Handler} instances to prevent
     * duplicate logs and injects the {@link CustomConsoleFormatter}.
     * </p>
     *
     * @throws RuntimeException if the logging system cannot be configured.
     */
    private static void setupLoggingEnvironment() {
        try {
            Logger rootLogger = Logger.getLogger("");

            // Purge default handlers to ensure our custom formatter takes precedence
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            ConsoleHandler customHandler = new ConsoleHandler();
            customHandler.setLevel(Level.ALL);
            customHandler.setFormatter(new CustomConsoleFormatter());

            rootLogger.addHandler(customHandler);

            // Configure verbosity thresholds
            LOGGER.setLevel(Level.INFO);
            Logger.getLogger("uhc").setLevel(Level.INFO);

        } catch (Exception e) {
            // System.err is used as a final fallback if the logger is compromised
            System.err.println("FATAL: The logging environment failed to initialize. Aborting.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Orchestrates the creation of the {@link Datapack} and the registration
     * of all functional modules.
     * <p>
     * This method initializes the {@link ControlPointRegistry} before any functions
     * are registered to ensure dependency requirements are met.
     * </p>
     *
     * @return A fully populated and validated {@link Datapack} instance.
     * @throws IllegalStateException if the target namespace or registry cannot be resolved.
     * @throws RuntimeException if any module registration logic fails.
     */
    private static Datapack buildDatapackStructure() {
        Datapack datapack = new Datapack();

        // 1. Resolve the primary Namespace
        final String namespaceName = DatapackConfig.CUSTOM_NAMESPACE;
        Namespace targetNamespace = datapack.getOrCreateNamespace(namespaceName);

        if (targetNamespace == null) {
            throw new IllegalStateException("Namespace Error: The required namespace '" +
                    namespaceName + "' could not be initialized.");
        }

        // 2. Initialize Game Session Data
        try {
            LOGGER.info("Initializing Game Session Registries...");
            ControlPointRegistry registry = new ControlPointRegistry();
            registry.initializeSession();
        } catch (Exception e) {
            throw new IllegalStateException("Registry Error: Failed to bootstrap the Control Point session. " +
                    "Check configurations. Details: " + e.getMessage(), e);
        }

        // 3. Define the registry of modules to be included in this build
        List<DatapackFunction> modules = List.of(
                new InitializeFunction(),
                new ClearEnderChestFunction(),
                new GodModeFunction(),
                new DropHeadsFunction(),
                new InitializationFunction(),
                new PlayerDeathFunction(),
                new ControlPointVisuals(),
                new ControlPointRecords()
        );

        LOGGER.info("Registering " + modules.size() + " functional modules...");

        // 4. Execute registration for every module in the list
        for (DatapackFunction module : modules) {
            Objects.requireNonNull(module, "Registration Error: Attempted to register a null module.");

            try {
                LOGGER.info(" -> Registering: " + module.getClass().getSimpleName());
                module.register(datapack, targetNamespace);
            } catch (Exception e) {
                throw new RuntimeException("Module Error: Failure during registration of " +
                        module.getClass().getSimpleName() + ". " + e.getMessage(), e);
            }
        }

        return datapack;
    }

    /**
     * Translates the virtual {@link Datapack} object tree into physical files.
     * <p>
     * This method performs path normalization and ensures that the
     * root output directory exists and is accessible.
     * </p>
     *
     * @param datapack The populated {@link Datapack} structure to generate.
     * @throws IOException if an I/O exception occurs during file creation.
     * @throws IllegalStateException if the output path configuration is invalid.
     * @throws NullPointerException if the provided datapack is null.
     */
    private static void executeGeneration(Datapack datapack) throws IOException {
        Objects.requireNonNull(datapack, "Generation Error: Target Datapack is null.");

        Generator generator = new Generator();
        String rootDir = DatapackConfig.OUTPUT_DIR_ROOT;

        if (rootDir == null || rootDir.isBlank()) {
            throw new IllegalStateException("Config Error: 'OUTPUT_DIR_ROOT' must be defined in DatapackConfig.");
        }

        // Normalize the path using NIO.2
        Path absolutePath = Paths.get(rootDir).toAbsolutePath();
        Path finalLocation = absolutePath.resolve(DatapackConfig.DATAPACK_FOLDER_NAME);

        LOGGER.info("Syncing virtual structure to filesystem at: " + finalLocation);

        try {
            generator.generate(datapack, absolutePath.toString());
        } catch (IOException e) {
            throw new IOException("FileSystem Error: Failed to write datapack files. " +
                    "Check folder permissions at: " + absolutePath, e);
        }
    }
}