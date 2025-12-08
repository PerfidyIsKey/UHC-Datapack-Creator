package uhc.modules;

import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;

/**
 * Defines and registers the components necessary for world initialization.
 * <p>
 * This module creates the {@code init/load} function and ensures it runs once
 * when the world is loaded by placing the corresponding tag in the required {@code minecraft} namespace.
 */
public class InitializationModule implements DatapackModule {

    /**
     * Registers the load function and its corresponding tag to the appropriate namespaces.
     * * @param datapack The main {@code Datapack} object for accessing namespaces.
     * @param namespaceName The name of the custom namespace (e.g., "uhc_core_pack") where functions reside.
     */
    @Override
    public void register(Datapack datapack, String namespaceName) {

        // 1. Get both namespaces required for the hook-up
        Namespace customNamespace = datapack.getOrCreateNamespace(namespaceName);
        Namespace minecraftNamespace = datapack.getOrCreateNamespace("minecraft");

        // --- 2. Create the Function in the CUSTOM namespace (The executable content) ---
        // Location: data/uhc_core_pack/function/init/load.mcfunction
        String path = "init/load";
        Function loadFunction = new Function(path);

        loadFunction.addCommand("say [UHC] Datapack initializing! Version: 88.0");
        loadFunction.addCommand("scoreboard objectives add uhc_status dummy");
        loadFunction.addCommand("# Add other world setup commands here (e.g., setting gamerules)");

        customNamespace.addComponent(loadFunction);

        // --- 3. Create the Tag in the MINECRAFT namespace (The automatic trigger) ---
        // Location: data/minecraft/tags/function/load.json
        // This tag is automatically executed by the game on world load.
        FunctionTag loadTag = new FunctionTag("load");

        // The tag content must reference the function using its full ID.
        // Example: "uhc_core_pack:init/load"
        String functionId = namespaceName + ":" + path;
        loadTag.addFunction(functionId);

        minecraftNamespace.addComponent(loadTag);
    }
}