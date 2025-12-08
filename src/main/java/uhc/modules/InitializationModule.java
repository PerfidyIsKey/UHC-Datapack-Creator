package uhc.modules;

import uhc.core.Datapack;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;

public class InitializationModule implements DatapackModule {

    @Override
    public void register(Datapack datapack, String namespaceName) {

        // 1. Get both namespaces
        Namespace customNamespace = datapack.getOrCreateNamespace(namespaceName);
        Namespace minecraftNamespace = datapack.getOrCreateNamespace("minecraft");

        // 2. Create the Function in CUSTOM namespace
        String path = "init/load";
        Function loadFunction = new Function(path);
        loadFunction.addCommand("say [UHC] Datapack initializing! Version: 88.0");
        loadFunction.addCommand("scoreboard objectives add uhc_status dummy");

        customNamespace.addComponent(loadFunction);

        // 3. Create the Tag in MINECRAFT namespace
        // The file will be: data/minecraft/tags/function/load.json
        FunctionTag loadTag = new FunctionTag("load");

        // It must point to "uhc_core_pack:init/load"
        String functionId = namespaceName + ":" + path;
        loadTag.addFunction(functionId);

        minecraftNamespace.addComponent(loadTag);
    }
}