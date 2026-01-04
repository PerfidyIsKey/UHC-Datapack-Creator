package uhc.functions.init;

import uhc.command.commands.SayCommand;
import uhc.components.functions.FunctionPath;
import uhc.components.tags.FunctionTagPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;
import uhc.core.DatapackConfig;
import uhc.functions.DatapackFunction;

import java.util.Objects;

/**
 * ⚙️ **Initialization Function (World Load Hook)**
 * <p>
 * This module is responsible for the "bootstrap" sequence of the UHC Datapack.
 * It ensures that essential setup commands run automatically whenever the world
 * is loaded or the {@code /reload} command is executed.
 * </p>
 * <p>
 * <b>Architectural Role:</b>
 * <ol>
 * <li>Generates the physical {@code load.mcfunction} within the custom namespace.</li>
 * <li>Wires the function to the {@code #minecraft:load} tag to trigger the Minecraft engine hook.</li>
 * </ol>
 * </p>
 */
public class InitializationFunction implements DatapackFunction {

    // --- 📄 Constant Fields ---

    /** * The descriptive title used for the auto-generated file header. */
    private static final String HEADER_TITLE = "Initialization: World Load Sequence";

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Executes the registration of the load-sequence logic and the engine-level wiring.
     * <p>
     * <b>Validation Policy:</b>
     * This method enforces a strict "No-Fallback" policy. If the {@code minecraft}
     * namespace cannot be acquired or if the {@link FunctionPath} registry is
     * incomplete, an exception is thrown to prevent "silent failures."
     * </p>
     *
     * @param datapack  The master {@link Datapack} instance (non-null).
     * @param namespace The primary custom {@link Namespace} for feature storage (non-null).
     * @throws NullPointerException  if {@code datapack} or {@code namespace} is null.
     * @throws IllegalStateException if the system 'minecraft' namespace is missing.
     * @throws RuntimeException      if logic assembly or tag wiring fails.
     */
    @Override
    public void register(Datapack datapack, Namespace namespace) {

        // --- 1. Infrastructure Validation ---
        Objects.requireNonNull(datapack, "Initialization Error: Datapack container cannot be null.");
        Objects.requireNonNull(namespace, "Initialization Error: Custom Namespace cannot be null.");

        // Attempt to acquire the 'minecraft' namespace for the tag hook
        final Namespace minecraftNamespace = datapack.getOrCreateNamespace(DatapackConfig.MINECRAFT_NAMESPACE);

        if (minecraftNamespace == null) {
            throw new IllegalStateException("CRITICAL: The mandatory 'minecraft' system namespace could not be initialized.");
        }

        // --- 2. Phase Execution ---
        try {
            // A. Create the executable function file
            this.assembleLoadFunction(namespace);

            // B. Wire the function to the world-load event tag
            this.wireLoadTag(minecraftNamespace);

        } catch (Exception e) {
            // No-fallback: ensure the developer is notified of the assembly failure immediately
            throw new RuntimeException("Initialization Assembly Failure: " + e.getMessage(), e);
        }
    }

    // --- 🛠️ Internal Assembly Methods ---

    /**
     * Builds the {@code .mcfunction} component containing initialization logic.
     * <p>
     * This method handles the creation of the announcement and setup placeholders.
     * </p>
     *
     * @param namespace The custom namespace where the function will reside (non-null).
     * @throws RuntimeException if path resolution or command building fails.
     */
    private void assembleLoadFunction(Namespace namespace) {
        final FunctionPath path = Objects.requireNonNull(FunctionPath.LOAD,
                "Path Registry Error: FunctionPath.LOAD constant is null.");

        final Function loadFunction = new Function(path);

        try {
            // Append decorative header and developer notes
            this.appendStandardHeader(loadFunction, HEADER_TITLE);
            this.addSafeLine(loadFunction, "Note: This is the entry point for all bootstrap logic.");

            // Construct the announcement string
            final String versionString = DatapackConfig.PACK_FORMAT + ".0";
            loadFunction.addLine(SayCommand.create("[UHC] Datapack initializing! Version: " + versionString));

            this.addSafeLine(loadFunction, "");
            this.addSafeLine(loadFunction, "Scoreboard and Gamerule setup should be appended below.");

            // Persistence
            namespace.addComponent(loadFunction);

        } catch (Exception e) {
            throw new RuntimeException("Failed to construct Load Function logic: " + e.getMessage(), e);
        }
    }

    /**
     * Connects the custom Load Function to the global {@code #minecraft:load} tag.
     * <p>
     * This is the "wiring" that forces Minecraft to execute the pack logic on startup.
     * </p>
     *
     * @param minecraftNamespace The system namespace (non-null).
     * @throws RuntimeException if the tag path is missing or registration fails.
     */
    private void wireLoadTag(Namespace minecraftNamespace) {
        final FunctionTagPath tagPath = Objects.requireNonNull(FunctionTagPath.LOAD,
                "Wiring Error: FunctionTagPath.LOAD (minecraft:load) is undefined.");

        try {
            final FunctionTag loadTag = new FunctionTag(tagPath);

            // Directly link the executable function's path to the tag
            loadTag.addFunction(Objects.requireNonNull(FunctionPath.LOAD,
                    "Wiring Error: Cannot link tag to a null FunctionPath."));

            // Register tag into the system namespace
            minecraftNamespace.addComponent(loadTag);

        } catch (Exception e) {
            throw new RuntimeException("Failed to wire load tag hook: " + e.getMessage(), e);
        }
    }
}