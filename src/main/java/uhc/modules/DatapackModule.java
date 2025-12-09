package uhc.modules;

import uhc.core.Datapack;

/**
 * Defines the contract for all functional units or feature sets within the datapack.
 * <p>
 * Implementing classes must contain the logic necessary to create and register
 * all associated {@code DatapackComponent}s (functions, tags, advancements, etc.)
 * into the correct namespaces.
 */
public interface DatapackModule {

    /**
     * Executes the module's registration logic, adding all defined components to the datapack.
     * * @param datapack The main {@code Datapack} container. Used to retrieve or create
     * namespaces (e.g., 'minecraft' for tags, and the custom namespace for functions).
     * @param namespaceName The {@code String} name of the custom namespace
     * (e.g., "uhc_core_pack") where the module's primary content resides.
     */
    void register(Datapack datapack, String namespaceName);
}