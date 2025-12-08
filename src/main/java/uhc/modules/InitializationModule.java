package uhc.modules;

import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;

public class InitializationModule implements DatapackModule {

    @Override
    public void register(Namespace namespace) {
        // 1. Define the Function
        String path = "init/load";
        Function loadFunction = new Function(path);

        // You can add as many commands as you want here without cluttering Main
        loadFunction.addCommand("say [UHC] Datapack initializing! Version: 88.0");
        loadFunction.addCommand("scoreboard objectives add uhc_status dummy");
        loadFunction.addCommand("gamerule doDaylightCycle false");

        namespace.addComponent(loadFunction);

        // 2. Define the Tag (to make it run on load)
        // Note: We construct the ID dynamically so it's always correct
        String functionId = namespace.getName() + ":" + path;

        FunctionTag loadTag = new FunctionTag("load");
        loadTag.addFunction(functionId);

        namespace.addComponent(loadTag);
    }
}