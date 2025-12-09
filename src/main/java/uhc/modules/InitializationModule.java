package uhc.modules;

import uhc.command.commands.Comment;
import uhc.command.commands.SayCommand;
import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;

/**
 * ⚙️ **Initialization Module**
 * * Defines and registers the components necessary for world initialization, specifically
 * the load sequence that runs once when the world is first loaded or the pack is reloaded.
 * <p>
 * This module creates the {@code init/load} function and ensures it runs automatically
 * by placing the corresponding function tag in the required {@code minecraft} namespace.
 */
public class InitializationModule implements DatapackModule {

    /**
     * Registers the load function and its corresponding tag to the appropriate namespaces.
     * * The method performs the essential wiring:
     * 1. Creates the executable function in the custom namespace.
     * 2. Creates the {@code load} function tag in the {@code minecraft} namespace.
     * 3. References the executable function from the load tag.
     * * @param datapack The main {@code Datapack} object used to retrieve or create namespaces.
     * @param namespaceName The name of the custom namespace (e.g., "uhc_core_pack") where custom functions are stored.
     * @throws IllegalStateException If the necessary namespaces cannot be obtained.
     */
    @Override
    public void register(Datapack datapack, String namespaceName) {

        // 1. Get both namespaces required for the hook-up
        Namespace customNamespace = datapack.getOrCreateNamespace(namespaceName);
        Namespace minecraftNamespace = datapack.getOrCreateNamespace("minecraft");

        // Safety Check: Ensure namespaces were successfully retrieved/created
        if (customNamespace == null || minecraftNamespace == null) {
            throw new IllegalStateException("Failed to obtain the custom namespace ('" + namespaceName + "') or the 'minecraft' namespace. Cannot register load components.");
        }


        // --- 2. Create the Function in the CUSTOM namespace (The executable content) ---
        // Final location on disk: data/{namespaceName}/function/init/load.mcfunction
        String functionPath = "init/load";
        Function loadFunction = new Function(functionPath);

        // Add the commands/lines to the function in sequence
        loadFunction.addLine(SayCommand.create("[UHC] Datapack initializing! Version: 88.0"));
        loadFunction.addLine(Comment.create("Add other world setup commands here (e.g., setting gamerules)"));

        // Register the function component with the custom namespace
        customNamespace.addComponent(loadFunction);


        // --- 3. Create the Tag in the MINECRAFT namespace (The automatic trigger) ---
        // Final location on disk: data/minecraft/tags/function/load.json
        // This tag is automatically executed by the game when the world loads.
        FunctionTag loadTag = new FunctionTag("load");

        // Construct the full function ID (namespace:path) required for the tag content.
        // Example ID: "uhc_core_pack:init/load"
        String functionId = namespaceName + ":" + functionPath;
        loadTag.addFunction(functionId);

        // Register the tag component with the minecraft namespace
        minecraftNamespace.addComponent(loadTag);
    }
}