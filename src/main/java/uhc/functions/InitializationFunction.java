package uhc.functions;

import uhc.command.commands.Comment;
import uhc.command.commands.SayCommand;
import uhc.components.functions.FunctionPath;
import uhc.components.tags.FunctionTagPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;
import uhc.core.DatapackConfig;

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
 * <li>Overwrites/Appends to the {@code minecraft:load} function tag to trigger
 * the aforementioned function.</li>
 * </ol>
 * </p>
 */
public class InitializationFunction implements DatapackFunction {

    // --- 🏗️ Registration Lifecycle ---

    /**
     * Executes the registration of the load-sequence logic and the engine-level wiring.
     * <p>
     * <b>Validation Policy:</b>
     * This method enforces a strict "No-Fallback" policy. If the {@code minecraft}
     * namespace cannot be acquired or if the {@link FunctionPath} registry is
     * incomplete, a {@link RuntimeException} is thrown to prevent a "silent
     * failure" where the pack loads but the logic never triggers.
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

        // --- 1. Environment Validation ---
        Objects.requireNonNull(datapack, "Initialization Error: Datapack container cannot be null.");
        Objects.requireNonNull(namespace, "Initialization Error: Custom Namespace cannot be null.");

        // Resolve the system namespace required for the 'minecraft:load' hook.
        final Namespace minecraftNamespace = datapack.getOrCreateNamespace(DatapackConfig.MINECRAFT_NAMESPACE);

        if (minecraftNamespace == null) {
            throw new IllegalStateException("CRITICAL: Failed to acquire the mandatory 'minecraft' system namespace.");
        }

        // --- 2. Executable Logic Assembly ---
        this.assembleLoadFunction(namespace);

        // --- 3. Engine Wiring (Function Tag) ---
        this.wireLoadTag(minecraftNamespace);
    }

    // --- 🛠️ Internal Assembly Methods ---

    /**
     * Builds the {@code .mcfunction} file containing the actual initialization commands.
     * * @param namespace The target custom namespace for the function.
     * @throws RuntimeException if command generation or path resolution fails.
     */
    private void assembleLoadFunction(Namespace namespace) {
        final FunctionPath path = Objects.requireNonNull(FunctionPath.LOAD,
                "Registry Error: FunctionPath.LOAD constant is not defined.");

        final Function loadFunction = new Function(path);

        try {
            // Header generation inherited from DatapackFunction interface
            this.appendStandardHeader(loadFunction, "Initialization: World Load Sequence");

            loadFunction.addLine(Comment.create("Note: This is the entry point for all setup logic."));

            // Generate the announcement command
            loadFunction.addLine(SayCommand.create("[UHC] Datapack initializing! Version: "
                    + DatapackConfig.PACK_FORMAT + ".0"));

            loadFunction.addLine(Comment.create(" "));
            loadFunction.addLine(Comment.create("Scoreboard and Gamerule setup should be appended here."));

            // Register the function component to the custom namespace
            namespace.addComponent(loadFunction);

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to assemble 'load.mcfunction' logic. " + e.getMessage(), e);
        }
    }

    /**
     * Wires the custom load function to the Minecraft engine via a Function Tag.
     * * @param minecraftNamespace The system namespace where the tag must reside.
     * @throws RuntimeException if the tag cannot be created or the path cannot be added.
     */
    private void wireLoadTag(Namespace minecraftNamespace) {
        try {
            // FunctionTagPath.LOAD references 'minecraft:load'
            final FunctionTag loadTag = new FunctionTag(FunctionTagPath.LOAD);

            // Directly reference the FunctionPath object to maintain type integrity.
            // This ensures the tag points to 'your_namespace:init/load'.
            loadTag.addFunction(Objects.requireNonNull(FunctionPath.LOAD, "Wiring Error: Missing LOAD path."));

            // Register the tag into the 'minecraft' namespace to trigger the hook.
            minecraftNamespace.addComponent(loadTag);

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to wire custom logic to #minecraft:load. " + e.getMessage(), e);
        }
    }
}