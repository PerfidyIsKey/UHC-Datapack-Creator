package uhc.modules;

import uhc.core.Datapack;

public interface DatapackModule {
    /**
     * Registers components to the datapack.
     * @param datapack The main datapack object (to access 'minecraft' namespace).
     * @param namespaceName Your custom namespace name (e.g. "uhc_core_pack") to put functions in.
     */
    void register(Datapack datapack, String namespaceName);
}