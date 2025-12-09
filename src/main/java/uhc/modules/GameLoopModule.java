package uhc.modules;

import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;

/**
 * Defines and registers the components necessary for the recurring game loop.
 * This module creates a tick function and ensures it runs every game tick
 * by placing the corresponding tag in the required 'minecraft' namespace.
 */
public class GameLoopModule implements DatapackModule {

    /**
     * Registers the tick function and tag components to the appropriate namespaces.
     * * @param datapack The main Datapack object for accessing namespaces.
     * @param namespaceName The name of the custom namespace (e.g., "uhc_core_pack") where functions reside.
     */
    @Override
    public void register(Datapack datapack, String namespaceName) {

        // 1. Get both namespaces required for the hook-up
        Namespace customNamespace = datapack.getOrCreateNamespace(namespaceName);
        Namespace minecraftNamespace = datapack.getOrCreateNamespace("minecraft");

        // 2. Create the Function in the CUSTOM namespace
        // Location: data/uhc_core_pack/function/loop/tick.mcfunction
        String path = "loop/tick";
        Function tickFunction = new Function(path);

        tickFunction.addCommand("# Main Game Loop - Executes 20 times per second");
        tickFunction.addCommand("execute as @a at @s run particle minecraft:cloud ~ ~ ~ 0.1 0.1 0.1 0 1 normal"); // Example command

        customNamespace.addComponent(tickFunction);

        // 3. Create the Tag in the MINECRAFT namespace
        // Location: data/minecraft/tags/function/tick.json
        // This file is the official hook Minecraft uses to run recurring commands.
        FunctionTag tickTag = new FunctionTag("tick");

        // The tag must contain the full resource ID pointing back to the custom function.
        // Example: "uhc_core_pack:loop/tick"
        String functionId = namespaceName + ":" + path;
        tickTag.addFunction(functionId);

        minecraftNamespace.addComponent(tickTag);
    }
}