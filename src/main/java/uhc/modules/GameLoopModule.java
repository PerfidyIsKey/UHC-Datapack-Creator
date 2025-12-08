package uhc.modules;

import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;

public class GameLoopModule implements DatapackModule {

    @Override
    public void register(Datapack datapack, String namespaceName) {

        // 1. Get both namespaces
        Namespace customNamespace = datapack.getOrCreateNamespace(namespaceName);
        Namespace minecraftNamespace = datapack.getOrCreateNamespace("minecraft");

        // 2. Create the Function in CUSTOM namespace
        String path = "loop/tick";
        Function tickFunction = new Function(path);
        tickFunction.addCommand("# Main Game Loop");
        tickFunction.addCommand("execute as @a at @s run particle minecraft:cloud ~ ~ ~ 0.1 0.1 0.1 0 1 normal");

        customNamespace.addComponent(tickFunction);

        // 3. Create the Tag in MINECRAFT namespace
        // The file will be: data/minecraft/tags/function/tick.json
        FunctionTag tickTag = new FunctionTag("tick");

        // It must point to "uhc_core_pack:loop/tick"
        String functionId = namespaceName + ":" + path;
        tickTag.addFunction(functionId);

        minecraftNamespace.addComponent(tickTag);
    }
}