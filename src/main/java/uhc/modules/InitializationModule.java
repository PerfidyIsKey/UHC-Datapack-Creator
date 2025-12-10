package uhc.modules;

import uhc.command.commands.Comment;
import uhc.command.commands.SayCommand;
import uhc.components.functions.FunctionPath;
import uhc.components.tags.FunctionTagPath;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;
import uhc.core.DatapackConfig; // Assuming this is needed to access version number for SayCommand

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

    private static final String MINECRAFT_NAMESPACE_NAME = "minecraft";

    /**
     * Registers the {@code load} function and its corresponding tag to the appropriate namespaces.
     * <p>
     * The method performs the essential wiring:
     * 1. Creates the executable function component in the **custom namespace**.
     * 2. Creates the **load function tag** in the required **{@code minecraft} namespace** ({@code data/minecraft/tags/function/load.json}).
     * 3. References the custom executable function from the load tag, setting up the auto-trigger.
     * * @param datapack The main {@code Datapack} object used to retrieve or create namespaces.
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
        Namespace minecraftNamespace = datapack.getOrCreateNamespace(MINECRAFT_NAMESPACE_NAME);

        // Safety Check: Verify successful creation/retrieval of required namespaces
        if (customNamespace == null || minecraftNamespace == null) {
            throw new IllegalStateException("Failed to obtain the necessary namespaces ('" + customNamespaceName + "' and '" + MINECRAFT_NAMESPACE_NAME + "'). Cannot register load components.");
        }


        // --- 3. Create the Function in the CUSTOM namespace (The executable content) ---
        // Function path: data/{customNamespaceName}/function/init/load.mcfunction
        FunctionPath functionPath = FunctionPath.LOAD;
        Function loadFunction = new Function(functionPath);

        // Add the commands/lines to the function in sequence
        // NOTE: Hardcoding '88.0' suggests pulling this from DatapackConfig for robustness.
        loadFunction.addLine(SayCommand.create("[UHC] Datapack initializing! Version: " + DatapackConfig.PACK_FORMAT + ".0"));
        loadFunction.addLine(Comment.create("Add other world setup commands here (e.g., setting gamerules, scoreboard setup)"));

        // Register the function component with the custom namespace
        customNamespace.addComponent(loadFunction);


        // --- 4. Create the Tag in the MINECRAFT namespace (The automatic trigger) ---
        // Tag path: data/minecraft/tags/function/load.json
        // This tag is automatically executed by the game when the world loads/reloads.

        // FunctionTag constructor takes the tag name "load". It pulls the custom namespace name
        // internally via DatapackConfig to construct the function ID inside the JSON.
        FunctionTag loadTag = new FunctionTag(FunctionTagPath.LOAD);

        // WIRING: Add the custom function ID (e.g., "uhc_core_pack:init/load") to the load tag's content.
        loadTag.addFunction(functionPath);

        // Register the tag component with the minecraft namespace
        minecraftNamespace.addComponent(loadTag);
    }
}