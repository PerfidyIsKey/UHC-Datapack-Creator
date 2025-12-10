package uhc.modules;

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
 * ⚙️ **Initialization Module**
 * <p>
 * Defines and registers the components necessary for world initialization, specifically
 * the load sequence that runs once when the world is first loaded or the pack is reloaded.
 * </p>
 * This module creates the executable function in the custom namespace and links it to the
 * automatic {@code minecraft:load} function tag, ensuring guaranteed execution on pack load.
 */
public class InitializationModule implements DatapackModule {

    /**
     * Registers the {@code load} function and its corresponding tag to the appropriate namespaces.
     * <p>
     * The method performs the essential wiring:
     * 1. Creates the executable function component in the **custom namespace** (e.g., {@code uhc_core_pack:init/load}).
     * 2. Creates the **load function tag** in the required **{@code minecraft} namespace** ({@code data/minecraft/tags/function/load.json}).
     * 3. References the custom executable function from the load tag, setting up the auto-trigger for the Minecraft engine.
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
            throw new IllegalArgumentException("The Datapack object cannot be null during module registration.");
        }
        if (customNamespaceName == null || customNamespaceName.isBlank()) {
            throw new IllegalArgumentException("The custom namespace name cannot be null or blank.");
        }

        // --- 2. Get both required namespaces for the wiring hook-up ---
        Namespace customNamespace = datapack.getOrCreateNamespace(customNamespaceName);
        Namespace minecraftNamespace = datapack.getOrCreateNamespace(DatapackConfig.MINECRAFT_NAMESPACE);

        // Safety Check: Verify successful creation/retrieval of required namespaces
        if (customNamespace == null || minecraftNamespace == null) {
            // CRITICAL FIX: Use the constant in the error message to be robust against a null return from getOrCreateNamespace.
            throw new IllegalStateException("Failed to obtain the necessary namespaces ('" + customNamespaceName + "' and '" + DatapackConfig.MINECRAFT_NAMESPACE + "'). Cannot register load components.");
        }


        // --- 3. Create the Function in the CUSTOM namespace (The executable content) ---
        // Defines the function's path: data/{customNamespaceName}/function/init/load.mcfunction
        FunctionPath functionPath = FunctionPath.LOAD;
        Function loadFunction = new Function(functionPath);

        // Add the commands/lines to the function in sequence
        // Uses the centralized PACK_FORMAT constant for robust version logging.
        loadFunction.addLine(SayCommand.create("[UHC] Datapack initializing! Version: " + DatapackConfig.PACK_FORMAT + ".0"));
        loadFunction.addLine(Comment.create("Add other world setup commands here (e.g., setting gamerules, scoreboard setup, etc.)"));

        // Register the function component with the custom namespace
        customNamespace.addComponent(loadFunction);


        // --- 4. Create the Tag in the MINECRAFT namespace (The automatic trigger) ---
        // Defines the tag's path: data/minecraft/tags/function/load.json
        // This tag is automatically executed by the game engine.

        // Instantiates the FunctionTag component using the type-safe enum.
        FunctionTag loadTag = new FunctionTag(FunctionTagPath.LOAD);

        // WIRING: Add the custom function's resource ID (e.g., "uhc_core_pack:init/load") to the load tag's content.
        // The FunctionTag class handles the conversion from FunctionPath to the full resource ID.
        loadTag.addFunction(functionPath);

        // Register the tag component with the target namespace (minecraft)
        minecraftNamespace.addComponent(loadTag);
    }
}