package uhc.modules;

import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag;

public class GameLoopModule implements DatapackModule {

    @Override
    public void register(Namespace namespace) {
        String path = "loop/tick";
        Function tickFunction = new Function(path);

        tickFunction.addCommand("# Main Game Loop");
        tickFunction.addCommand("execute as @a at @s run particle minecraft:cloud ~ ~ ~ 0.1 0.1 0.1 0 1 normal");

        namespace.addComponent(tickFunction);

        // Register the tick tag
        FunctionTag tickTag = new FunctionTag("tick");
        tickTag.addFunction(namespace.getName() + ":" + path);
        namespace.addComponent(tickTag);
    }
}