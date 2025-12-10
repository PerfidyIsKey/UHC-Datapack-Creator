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

/**
 * ⚙️ **Initialization Function (World Load Hook)**
 * <p>
 * Defines and registers the components necessary for world initialization, specifically
 * the load sequence that runs once when the world is first loaded or the pack is reloaded
 * (via {@code /reload}).
 * </p>
 * This function creates the executable function in the custom namespace and links it to the
 * automatic {@code minecraft:load} function tag, ensuring guaranteed execution on pack load.
 */
public class InitializationFunction implements DatapackFunction {

    /**
     * Registers the {@code load} function and its corresponding tag to the appropriate namespaces.
     * <p>
     * The method performs the essential wiring, following the structure:
     * 1. **Executable Function**: Creates the primary executable content (the `.mcfunction` file) in the **custom namespace**.
     * 2. **Auto-Trigger Tag**: Creates the {@code minecraft:load} **function tag** in the **{@code minecraft} namespace**.
     * 3. **Wiring**: References the custom executable function from the load tag, setting up the auto-trigger for the Minecraft engine.
     * </p>
     * @param datapack The main {@code Datapack} object used to retrieve or create namespaces.
     * @param customNamespaceName The name of the custom namespace (e.g., "uhc_core_pack") where custom functions are stored.
     * @throws IllegalArgumentException If {@code datapack} is null or {@code customNamespaceName} is invalid.
     * @throws IllegalStateException If a required {@code Namespace} cannot be obtained from the {@code Datapack} container.
     */
    @Override
    public void register(Datapack datapack, String customNamespaceName) {

        // --- 1. Parameter Validation ---
        if (datapack == null) {
            throw new IllegalArgumentException("The Datapack object cannot be null during function registration.");
        }
        if (customNamespaceName == null || customNamespaceName.isBlank()) {
            throw new IllegalArgumentException("The custom namespace name cannot be null or blank.");
        }

        // --- 2. Get both required namespaces for the wiring hook-up ---
        Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);
        Namespace minecraftNamespace = datapack.getOrCreateNamespace(DatapackConfig.MINECRAFT_NAMESPACE);

        // Safety Check: Verify successful creation/retrieval of required namespaces
        if (customNamespace == null || minecraftNamespace == null) {
            // Error message is robust, using the constant name for the system namespace.
            throw new IllegalStateException("Failed to obtain the necessary namespaces ('" + customNamespaceName + "' and '" + DatapackConfig.MINECRAFT_NAMESPACE + "'). Cannot register load components.");
        }


        // --- 3. Create the Function in the CUSTOM namespace (The executable content) ---
        // Resource ID example: uhc_core_pack:init/load
        // File path: data/{customNamespaceName}/function/init/load.mcfunction
        FunctionPath functionPath = FunctionPath.LOAD;
        Function loadFunction = new Function(functionPath);

        // Add comments and commands to the function in sequence
        loadFunction.addLine(Comment.create("--- Datapack Load Function (Triggered by minecraft:load tag) ---"));
        loadFunction.addLine(Comment.create(""));

        // Command 1: Announce pack activation and version.
        loadFunction.addLine(SayCommand.create("[UHC] Datapack initializing! Version: " + DatapackConfig.PACK_FORMAT + ".0"));

        // Command 2: Placeholder for essential setup.
        loadFunction.addLine(Comment.create("Add other world setup commands here (e.g., setting gamerules, scoreboard setup, etc.)"));
        loadFunction.addLine(Comment.create(""));

        // Register the function component with the custom namespace
        customNamespace.addComponent(loadFunction);


        // --- 4. Create the Tag in the MINECRAFT namespace (The automatic trigger) ---
        // Resource ID: minecraft:load
        // File path: data/minecraft/tags/function/load.json
        // This tag is automatically executed by the game engine upon world load or /reload.

        // Instantiates the FunctionTag component using the type-safe enum.
        FunctionTag loadTag = new FunctionTag(FunctionTagPath.LOAD);

        // WIRING: Add the custom function's resource ID (uhc_core_pack:init/load) to the load tag's content list.
        loadTag.addFunction(functionPath);

        // Register the tag component with the target namespace (minecraft)
        minecraftNamespace.addComponent(loadTag);
    }
}